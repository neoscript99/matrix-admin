package com.feathermind.matrix.domain.sys

import com.feathermind.matrix.initializer.InitializeDomain
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 把附件文件信息和属性分开，附件最大20M
 * @author neo*
 */
@Entity
@ToString(includes = 'fileId,refCount')
@EqualsAndHashCode(includes = 'fileId')
@InitializeDomain(profiles = 'dev')
class AttachmentFile {
    String fileId
    Integer refCount = 0 //引用计数
    byte[] data;
    Date dateCreated
    static mapping = {
        id name: 'fileId', generator: 'assigned'
        /**
         * todo 默认类型db2报错（varchar(20170000) is not valid ），
         * 如果配置blob无法自动转换，后续再优化
         */
        //data type: 'blob'
    }

    static constraints = {
        fileId maxSize: 80
        data maxSize: 1024 * 1024 * 20
    }
    static DemoData = 'empty file for test'
    static DemoFile = new AttachmentFile(fileId: DemoData, refCount: 1, data: DemoData.bytes)
    static initList = [DemoFile]
}
