package org.yzedu.research.domain.res

import com.feathermind.matrix.domain.sys.AttachmentInfo
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 论文基本信息
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'title')
@EqualsAndHashCode(includes = 'id')
class Paper {
    String id
    String title
    YzUser author
    AttachmentInfo paperFile
    String paperStatusCode

    Date dateCreated
    Date lastUpdated

    static mapping = {
        author fetch: 'join', lazy: false
        paperFile fetch: 'join', lazy: false
    }
    static constraints = {
        paperFile nullable: true
    }
}
