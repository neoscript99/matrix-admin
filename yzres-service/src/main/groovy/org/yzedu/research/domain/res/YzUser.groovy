package org.yzedu.research.domain.res

import com.feathermind.matrix.domain.sys.User
import grails.gorm.annotation.Entity

@Entity
class YzUser extends User{
    YzDept dept
}
