package com.feathermind.matrix.domain.sys

import com.feathermind.matrix.trait.AutoTime
import grails.gorm.annotation.Entity
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 地方方用户绑定
 */
@Entity
@ToString(includes = 'nickname')
@EqualsAndHashCode(includes = 'id')
class UserBind implements AutoTime {
    String id
    User user
    String openid
    String unionid
    String nickname
    String headimgurl
    //	用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
    String sex
    //用户所在城市
    String city
    //用户所在国家
    String country
    //用户所在省份
    String province
    //wechat, dingtalk等
    String source
    static mapping = {
        user lazy: false, fetch: 'join'
        tablePerHierarchy true
    }
    static constraints = {
        openid unique: true
        headimgurl nullable: true, maxSize: 256
        unionid nullable: true
        sex nullable: true
        city nullable: true
        country nullable: true
        province nullable: true
    }
}
