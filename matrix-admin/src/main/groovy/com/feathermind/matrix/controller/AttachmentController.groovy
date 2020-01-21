package com.feathermind.matrix.controller

import com.feathermind.matrix.domain.sys.AttachmentInfo
import com.feathermind.matrix.service.AbstractService
import com.feathermind.matrix.service.AttachmentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/attachment")
class AttachmentController extends DomainController<AttachmentInfo> {
    @Autowired
    AttachmentService attachmentService

    @Override
    AbstractService<AttachmentInfo> getDomainService() {
        return attachmentService
    }
}
