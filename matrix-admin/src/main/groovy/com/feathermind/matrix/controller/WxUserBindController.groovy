package com.feathermind.matrix.controller

import com.feathermind.matrix.controller.bean.LoginInfo
import com.feathermind.matrix.controller.bean.UserBindRes
import com.feathermind.matrix.domain.sys.UserBind
import com.feathermind.matrix.service.AbstractService
import com.feathermind.matrix.service.UserBindService
import com.feathermind.matrix.wechat.WxUserBinder
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/userBind")
class WxUserBindController extends DomainController<UserBind> implements WxUserBinder<UserBindRes> {
    @Autowired
    UserBindService userBindService
    @Autowired
    LoginController loginController


    /**
     * 微信公众号、小程序、开放平台OAuth用户绑定
     * 支持以下对象
     * @param userInfo
     * @see me.chanjar.weixin.mp.bean.result.WxMpUser<br>
     * @see cn.binarywang.wx.miniapp.bean.WxMaUserInfo<br>
     * @see me.chanjar.weixin.common.bean.WxOAuth2UserInfo<br>
     *
     * @return 是否成功：loginInfo.success
     */
    @Override
    UserBindRes bindUser(Object userInfo) {
        if(!userInfo)
            return afterFail('请传入userInfo')
        def bind = new UserBind();
        BeanUtils.copyProperties(userInfo, bind);
        bind.source = 'wechat'
        return bindUser(bind)
    }

    /**
     * 根据openId或unionId查找绑定信息，返回登录结果
     * @param openId
     * @param unionId
     * @return
     */
    @Override
    UserBindRes login(String openId, String unionId) {
        UserBind bind = userBindService.findBind(openId, unionId, null)
        if (!bind)
            return afterFail('未绑定帐号', new UserBind(openid: openId, unionid: unionId));
        if (!bind.user.enabled)
            return afterFail('用户帐号失效', bind);
        return afterSuccess(bind)
    }

    /**
     * 根据openId或unionId查找绑定信息，更新电话号码，返回登录结果
     *
     */
    @Override
    UserBindRes bindPhone(String openId, String unionId, String phoneNumber) {
        if (!openId) return afterFail('请传入openId')
        UserBind bind = userBindService.bindPhone(openId, unionId, phoneNumber)
        if (!bind)
            return afterFail('未绑定帐号')
        if (!bind.user.enabled)
            return afterFail('用户帐号失效')
        return afterSuccess(bind)
    }

    UserBindRes bindUser(UserBind bind) {
        if (!bind.openid) return afterFail('请传入openId')
        UserBind newBind = userBindService.getOrCreateUser(bind)
        if (!newBind.user.enabled)
            return afterFail('用户帐号失效')
        return afterSuccess(newBind)
    }

    UserBindRes afterSuccess(UserBind bind) {
        LoginInfo loginInfo = loginController.afterLogin(bind.user);
        return new UserBindRes([loginInfo: loginInfo, userBind: bind]);
    }

    UserBindRes afterFail(String error, UserBind bind = null) {
        return new UserBindRes([loginInfo: [success: false, error: error], userBind: bind]);
    }

    @Override
    AbstractService<UserBind> getDomainService() {
        return userBindService
    }
}
