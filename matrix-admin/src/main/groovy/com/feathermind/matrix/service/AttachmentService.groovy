package com.feathermind.matrix.service

import cn.hutool.core.lang.UUID
import com.feathermind.matrix.domain.sys.AttachmentFile
import com.feathermind.matrix.domain.sys.AttachmentInfo
import com.feathermind.matrix.util.EncoderUtil
import grails.gorm.transactions.ReadOnly
import org.apache.poi.util.IOUtils
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.core.io.support.ResourcePatternResolver
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

import java.time.LocalDateTime

@Service
class AttachmentService extends AbstractService<AttachmentInfo> {

    static public String ATTACH_TEMP_ID_PREFIX = 'MatrixTempFile_';

    AttachmentInfo saveWithFile(File file, String ownerId, String ownerName) {
        if (file && file.isFile())
            return saveWithByte(file.name, ownerId, ownerName, file.getBytes());
        else
            throw new RuntimeException("$file,该文件不存在")

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

    void deleteInfo(AttachmentInfo info) {
        generalRepository.delete(info)
        def file = generalRepository.get(AttachmentFile, info.fileId);
        if (file.refCount == 1)
            generalRepository.delete(file)
        else
            file.refCount--;
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

    String genTempOwnerId() {
        return "${ATTACH_TEMP_ID_PREFIX}${UUID.fastUUID().toString()}"
    }

    @Scheduled(cron = "0 0 * * * *")
    void cleanTemp() {
        log.info("定时删除一些没有所属对象的附件，正式环境删除一天前的临时附件")
        def date = LocalDateTime.now().minusDays(1)
        def list = list([like: [['ownerId', "${ATTACH_TEMP_ID_PREFIX}%".toString()]],
                         lt  : [['dateCreated', Date.from(date.toInstant())]]])
        deleteInfoByOwners(list*.ownerId)
    }

    static class InfoAndFile {
        AttachmentInfo info
        AttachmentFile file
    }
}
