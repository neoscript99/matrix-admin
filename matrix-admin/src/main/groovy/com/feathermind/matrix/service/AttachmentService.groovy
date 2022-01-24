package com.feathermind.matrix.service

import cn.hutool.core.date.DateUtil
import cn.hutool.core.io.FileUtil
import cn.hutool.core.util.ZipUtil
import com.feathermind.matrix.domain.sys.AttachmentFile
import com.feathermind.matrix.domain.sys.AttachmentInfo
import com.feathermind.matrix.util.EncoderUtil
import com.feathermind.matrix.util.MatrixException
import grails.gorm.transactions.ReadOnly
import org.apache.poi.util.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.core.env.Environment
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.support.ResourcePatternResolver
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class AttachmentService extends AbstractService<AttachmentInfo> {
    @Autowired
    ApplicationContext applicationContext
    @Autowired
    private Environment env;

    AttachmentInfo saveWithMultipartFile(MultipartFile file, String ownerId, String ownerName) {
        return this.saveWithByte(file.originalFilename, ownerId, ownerName, file.bytes)
    }

    AttachmentInfo saveWithFile(File file, String ownerId, String ownerName) {
        if (file && file.isFile())
            return saveWithByte(file.name, ownerId, ownerName, file.getBytes());
        else
            throw new MatrixException('NoFile', "$file,该文件不存在")

    }

    AttachmentInfo saveWithByte(String name, String ownerId, String ownerName, byte[] data) {
        def fileHash = EncoderUtil.sha256(data);
        def attInfo = new AttachmentInfo(name: name, ownerId: ownerId, ownerName: ownerName, fileSize: data.length);
        def existFile = getFile(fileHash)
        if (existFile) {
            existFile.refCount++;
            attInfo.fileId = existFile.fileId;
        } else {
            //fileId指定用fileHash
            AttachmentFile newFile = generalRepository.saveEntity(
                    new AttachmentFile(fileId: fileHash, data: data, refCount: 1));
            attInfo.fileId = newFile.fileId;
            //同一事务可能多次引用同一个file
            generalRepository.flush();
        }
        saveEntity(attInfo)
    }

    AttachmentFile getFile(String fileId) {
        generalRepository.get(AttachmentFile, fileId);
    }

    File getZipFile(List<AttachmentInfo> infoList) {
        //根据包含的所有文件信息，创建唯一性的zip
        //由于要保存所有文件的排序信息，如果顺序不同需要重新打包
        String zipName = EncoderUtil.sha256(String.join(',', infoList.collect { it.id }))
        //保存到当前目录
        File file = FileUtil.file("${System.getProperty("user.dir")}/zipTemp/${zipName}.zip")
        if (FileUtil.exist(file)) {
            log.info('文件已存在，直接返回：{}', zipName)
            return file
        }
        String[] paths = new String[infoList.size()];
        InputStream[] ins = new InputStream[infoList.size()];
        infoList.eachWithIndex { info, idx ->
            def infoFile = generalRepository.get(AttachmentFile, info.fileId);
            paths[idx] = "${idx + 1}.$info.name".toString()
            ins[idx] = new ByteArrayInputStream(infoFile.data)
        }
        return ZipUtil.zip(file, paths, ins)
    }

    InfoAndFile getInfoAndFile(String ownerId, String fileId) {
        log.info "getInfoAndFile ownerId: $ownerId fileId: $fileId"
        new InfoAndFile([info: findFirst([eq: [['ownerId', ownerId], ['fileId', fileId]]]),
                         file: generalRepository.get(AttachmentFile, fileId)])
    }

    InfoAndFile getInfoAndFile(String infoId) {
        log.info "getInfoAndFile infoId: $infoId"
        def info = get(infoId)
        new InfoAndFile([info: info,
                         file: generalRepository.get(AttachmentFile, info.fileId)])
    }

    void deleteInfoByOwners(List ownerList) {
        log.info "deleteInfoByOwners $ownerList"
        if (ownerList)
            list(['in': [['ownerId', ownerList]]]).each { info ->
                deleteInfo(info)
            }
    }

    void deleteInfoByOwnerAndFileId(String ownerId, String fileId) {
        log.info "deleteInfoByOwnerAndFileId $ownerId $fileId"
        def info = findFirst([eq: [['ownerId', ownerId], ['fileId', fileId]]])
        if (info)
            deleteInfo(info)
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void deleteInfo(AttachmentInfo info) {
        log.info('delete AttachmentInfo {}', info.id)
        generalRepository.delete(info)
        //手工flush，触发数据库操作，如果出现异常不做后续操作
        generalRepository.flush()
        def file = generalRepository.get(AttachmentFile, info.fileId);
        if (file.refCount <= 1)
            generalRepository.delete(file)
        else
            file.refCount--;
        generalRepository.flush()
    }

    @ReadOnly
    List queryByOwner(String ownerId) {
        log.info "queryByOwner $ownerId"
        list([eq: [['ownerId', ownerId]]]);
    }

    /**
     * 将本地资源文件保存到数据库附件
     * @param resPath
     * @return
     */
    AttachmentInfo saveFromResource(String resPath, String ownerName = null, String ownerId = null) {
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        def res = resourcePatternResolver.getResource(resPath);
        //打成jar后，直接取file失败
        //def file = resourcePatternResolver.getResource(resPath).file;
        def data = IOUtils.toByteArray(res.getInputStream())
        saveWithByte(res.filename, ownerId, ownerName, data);
    }


    /**
     * 在操作界面上，附件上传后，有可能未owner信息，这部分附件没有用处，可以删除
     * 无效附件清理可以通过检查所有AttachmentInfo.id的外键，都未被依赖的id，而且ownerId为空可以删除
     * 根据本身系统指定依赖策略
     * 如果本身就不设置owner，那本方法逻辑不适用
     */
    //@Scheduled(cron = "0 0 * * * *")
    void cleanTemp() {
        def deleteDate = env.activeProfiles.contains('dev') ? DateUtil.offsetMinute(DateUtil.date(), -2) : DateUtil.yesterday()
        log.info("定时删除一些没有owner的附件，删除${deleteDate.toString()}之前的临时附件")
        list([isNull: ['ownerId'],
              lt    : [['dateCreated', deleteDate]]]).each {
            try {
                //deleteInfo必须手工flush，触发数据库操作，否则在事务切面才能捕捉异常，此处异常处理无效
                //捕获异常后，提交结果还是不对，原因不明，可能flush出现过异常，事务就不行
                //目前通过新事务来解决，必须getBean才能启动新事务
                applicationContext.getBean(AttachmentService).deleteInfo(it)
            } catch (Exception e) {
                //出现异常清理缓存，不能重复同步数据库，否则一直异常
                generalRepository.clear()
                log.error("附件${it.id}删除失败，原因可能是ownerId为null，但存在外键关联", e)
            }
        }
    }

    static class InfoAndFile {
        AttachmentInfo info
        AttachmentFile file
    }
}
