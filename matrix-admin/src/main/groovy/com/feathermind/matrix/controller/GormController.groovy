package com.feathermind.matrix.controller

import cn.hutool.core.io.FileUtil
import com.feathermind.matrix.config.MatrixConfigProperties
import com.feathermind.matrix.controller.bean.ResBean
import com.feathermind.matrix.domain.sys.AttachmentInfo
import com.feathermind.matrix.service.AttachmentService
import com.feathermind.matrix.service.CasClientService
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.CacheControl
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.Base64Utils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.view.RedirectView

import javax.activation.FileTypeMap
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpSession
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

@RestController
@Slf4j
class GormController {
    @Autowired
    MatrixConfigProperties matrixConfigProperties
    @Autowired
    AttachmentService attachmentService
    @Autowired
    CasClientService casClientService
    @Autowired
    LoginController loginController
    @Autowired
    RestTemplate restTemplate

    @GetMapping("download/{id}")
    public ResponseEntity<byte[]> getAttach(@PathVariable("id") String id) throws IOException {
        def info = attachmentService.get(id)
        if (info) {
            def file = attachmentService.getFile(info.fileId)
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename* = UTF-8''" + URLEncoder.encode(info.name, 'UTF-8'))
                    .cacheControl(CacheControl.maxAge(10, TimeUnit.DAYS).cachePublic().noTransform())
                    .contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(info.name)))
                    .body(file.data);
        } else
            return ResponseEntity.notFound().build()
    }

    /**
     * @see https://kkfileview.keking.cn/zh-cn/docs/usage.html
     * @param id
     * @param res
     * @throws IOException
     */
    @GetMapping("preview/{id}")
    public void filePreview(@PathVariable("id") String id, HttpServletResponse res) throws IOException {
        def info = attachmentService.get(id)
        if (info) {
            String fileUrl = "${matrixConfigProperties.fileDownloadUrl}/${info.id}?fullfilename=${info.fileId}.${FileUtil.extName(info.name)}".toString()
            String fileUrlEncode = URLEncoder.encode(Base64Utils.encodeToString(fileUrl.getBytes(StandardCharsets.UTF_8)), "UTF-8");

            def url = "${matrixConfigProperties.filePreviewRoot}/onlinePreview?url=${fileUrlEncode}"
            res.sendRedirect(url)
        }
    }

    @PostMapping("previewCheck")
    public ResBean previewCheck() {
        return new ResBean(matrixConfigProperties.filePreviewEnable)
    }

    @PostMapping("upload")
    public AttachmentInfo handleFileUpload(@RequestParam MultipartFile file) {

        def fileInfo = attachmentService.saveWithMultipartFile(file, null, null);

        return fileInfo
    }

    @GetMapping("logout")
    public RedirectView logout(HttpSession session) {
        loginController.logout(session)
        String redirectUrl = matrixConfigProperties.casClientEnabled ? casClientService.getLogoutUrl() : '/index.html'
        return new RedirectView(redirectUrl);
    }
}
