package com.feathermind.matrix.controller


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.CacheControl
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView

import javax.activation.FileTypeMap
import javax.servlet.http.HttpSession
import java.util.concurrent.TimeUnit

@RestController
class GormController {
    @Autowired
    GormSessionBean gormSessionBean
    @Autowired
    com.feathermind.matrix.service.AttachmentService attachmentService
    @Autowired
    com.feathermind.matrix.service.CasClientService casClientService

    @GetMapping("attach/{id}")
    public ResponseEntity<byte[]> getAttach(@PathVariable("id") String id) throws IOException {
        def info = attachmentService.get(id)
        if (info) {
            def file = attachmentService.getFile(info.fileId)
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + info.getName())
                    .cacheControl(CacheControl.maxAge(10, TimeUnit.DAYS).cachePublic().noTransform())
                    .contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(info.name)))
                    .body(file.data);
        } else
            return ResponseEntity.notFound().build()
    }

    @GetMapping("logout")
    public RedirectView logout(HttpSession session) {
        gormSessionBean.token = null;
        session.invalidate()
        String redirectUrl = casClientService.clientEnabled ? casClientService.getLogoutUrl() : '/index.html'
        return new RedirectView(redirectUrl);
    }
}
