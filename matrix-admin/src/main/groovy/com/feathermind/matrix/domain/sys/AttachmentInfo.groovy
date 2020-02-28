package com.feathermind.matrix.domain.sys

import com.feathermind.matrix.initializer.InitializeDomain
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import static com.feathermind.matrix.domain.sys.AttachmentFile.*

@Entity
@ToString(includes = 'name,fileSize')
@EqualsAndHashCode(includes = 'id')
@InitializeDomain(profiles = 'dev', depends = AttachmentFile)
class AttachmentInfo {
    String id
    String name
    Long fileSize
    String fileId
    String ownerId
    String ownerName

    Date dateCreated

    static mapping = {
        ownerId index: 'idx_attach_owner'
        fileId index: 'idx_attach_file_id'
        dateCreated index: 'idx_attach_date'
    }

    static constraints = {
        fileId maxSize: 80
        ownerId maxSize: 80, nullable: true
        ownerName nullable: true
        name maxSize: 256
    }
    static graphql = true
    static DemoFileInfo = new AttachmentInfo(name: 'test.txt', fileSize: 19, fileId: DemoData)
    static initList = [DemoFileInfo]
}
