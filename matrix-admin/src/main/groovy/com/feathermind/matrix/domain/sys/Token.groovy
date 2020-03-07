package com.feathermind.matrix.domain.sys

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import com.feathermind.matrix.initializer.InitializeDomain

import java.time.LocalDateTime

@Deprecated
@ToString(includePackage = false, includes = 'expireTime,lastUpdated')
@EqualsAndHashCode(includes = 'id')
@InitializeDomain(profiles = ['dev', 'test'])
class Token {

    String id
    //如果一直未访问的到期时间
    LocalDateTime expireTime
    Boolean destroyed = false
    //到期之前如有访问，可刷新次数
    Integer maxRefreshTimes
    Integer refreshTimes = 0
    String username
    //用户类型，sys - 后台管理用户，pub - 公网用户
    //String userType = 'sys'
    String roles

    Date dateCreated
    Date lastUpdated

    static mapping = {
        id generator: 'assigned'
    }

    static constraints = {
    }

    static initList = [new Token([
            id             : 'gorm-dev-token',
            expireTime     : LocalDateTime.of(2021, 1, 1, 1, 1),
            maxRefreshTimes: 10,
            username       : 'admin',
            roles          : 'Administrators,NormalUsers,Public'])]
}
