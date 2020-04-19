package com.feathermind.matrix.controller

import com.feathermind.matrix.domain.sys.AttachmentInfo
import com.feathermind.matrix.service.AttachmentService
import com.feathermind.matrix.service.CasClientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.view.RedirectView

import javax.servlet.http.HttpSession

@RestController
@CrossOrigin(origins = ["http://localhost:3000", "null"], allowCredentials = "true")
class GormController {
    @Autowired
    AttachmentService attachmentService
    @Autowired
    CasClientService casClientService
    @Autowired
    LoginController loginController

    @GetMapping("download/{id}")
    public ResponseEntity<byte[]> getAttach(@PathVariable("id") String id) throws IOException {
        def info = attachmentService.get(id)
        if (info) {
            def file = attachmentService.getFile(info.fileId)
            HttpHeaders headers = new HttpHeaders();
            //指定文件名
            headers.setContentDispositionFormData("attachment", new String(info.name.getBytes(), "ISO-8859-1"));
            //指定以流的形式下载文件
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return ResponseEntity.ok().headers(headers).body(file.data);
        } else
            return ResponseEntity.notFound().build()
    }

    @PostMapping("upload")
    public ResponseEntity<AttachmentInfo> handleFileUpload(@RequestParam MultipartFile file) {

        def fileInfo = attachmentService.saveWithMultipartFile(file, null, null);

        return ResponseEntity.ok(fileInfo)
    }

    @GetMapping("logout")
    public RedirectView logout(HttpSession session) {
        loginController.logout(session)
        String redirectUrl = casClientService.clientEnabled ? casClientService.getLogoutUrl() : '/index.html'
        return new RedirectView(redirectUrl);
    }
}
