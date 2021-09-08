package com.feathermind.matrix.controller

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo
import cn.hutool.core.bean.BeanUtil
import com.feathermind.matrix.domain.sys.UserBind
import com.feathermind.matrix.service.AbstractService
import com.feathermind.matrix.service.UserBindService
import com.feathermind.matrix.wechat.mp.bean.WxUserInfo
import com.feathermind.matrix.wechat.WxMaBinder
import com.feathermind.matrix.wechat.WxMpBinder
import com.feathermind.matrix.wechat.WxPhoneBinder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/userBind")
class UserBindController extends DomainController<UserBind> implements WxMpBinder, WxMaBinder, WxPhoneBinder {
    @Autowired
    UserBindService userBindService
    @Autowired
    LoginController loginController


    @Override
    Map bindWxMpUser(WxUserInfo wxUserInfo) {
        def bind = BeanUtil.toBean(wxUserInfo, UserBind);
        bind.source = 'wechat'
        return bindUser(bind)
    }
    /**
     * 绑定微信小程序用户信息，并返回登录结果
     * @param userInfo
     * @return
     */
    @Override
    Map bindWxMaUser(WxMaUserInfo userInfo) {
        def bind = BeanUtil.toBean(wxUserInfo, UserBind);
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
    Map wxMaLogin(String openId, String unionId) {
        UserBind bind = userBindService.findBind(openId, unionId, null)
        if (!bind)
            return afterFail('未绑定帐号', bind);
        if (!bind.user.enabled)
            return afterFail('用户帐号失效', bind);
        return afterSuccess(bind)
    }

    /**
     * 根据openId或unionId查找绑定信息，更新电话号码，返回登录结果
     *
     */
    @Override
    Map bindWxPhone(String openId, String unionId, String phoneNumber) {
        UserBind bind = userBindService.bindPhone(openId, unionId, phoneNumber)
        if (!bind)
            return afterFail('未绑定帐号')
        if (!bind.user.enabled)
            return afterFail('用户帐号失效')
        return afterSuccess(bind)
    }

    Map bindUser(UserBind bind) {
        UserBind newBind = userBindService.getOrCreateUser(bind)
        if (!newBind.user.enabled)
            return afterFail('用户帐号失效')
        return afterSuccess(newBind)
    }

    Map afterSuccess(UserBind bind) {
        Map loginInfo = loginController.afterLogin(bind.user);
        return [loginInfo: loginInfo, userBind: bind];
    }

    Map afterFail(String error, UserBind bind = null) {
        return [loginInfo: [success: false, error: error], userBind: bind];
    }

    @Override
    AbstractService<UserBind> getDomainService() {
        return userBindService
    }
}
