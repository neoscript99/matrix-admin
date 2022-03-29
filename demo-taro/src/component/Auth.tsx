import { authService, useServiceStore, wxMaService } from '../services';
import { AtButton, AtNoticebar, AtCard } from 'taro-ui';
import React, { useCallback } from 'react';
import Taro from '@tarojs/taro';
import { View } from '@tarojs/components';
import { PageHeader } from './PageHeader';
import { ButtonProps } from '@tarojs/components/types/Button';

const wxGender = {
  0: '未知',
  1: '男性',
  2: '女性',
};
export const Auth: React.FC<any> = (props) => {
  const authStore = useServiceStore(authService);
  const wxMaStore = useServiceStore(wxMaService);
  const onGetProfile = useCallback(() => {
    Taro.getUserProfile({
      desc: '用于完善会员资料', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
      success: ({ userInfo }) => {
        console.log('Taro.getUserProfile: ', userInfo);
        wxMaService.bindUser({
          ...userInfo,
          gender: wxGender[userInfo.gender],
        });
      },
    });
  }, []);
  //本系统要求必须绑定手机
  const onGetPhoneNumber = useCallback<ButtonProps['onGetPhoneNumber']>((e) => {
    //Taro.login的code只能使用一次;
    Taro.login().then(({ code }) => wxMaService.bindPhone(code, e.detail.encryptedData, e.detail.iv));
  }, []);
  const { isWxBind, isPhoneBind } = wxMaStore;
  const { isLogin } = authStore;
  if (isPhoneBind) return props.children;
  return (
    <View>
      <PageHeader />
      <AtCard title="请登录">
        {isWxBind && !isPhoneBind && (
          <AtButton type="primary" openType="getPhoneNumber" onGetPhoneNumber={onGetPhoneNumber}>
            绑定手机
          </AtButton>
        )}
        {!isWxBind && (
          <AtButton type="primary" onClick={onGetProfile}>
            微信认证
          </AtButton>
        )}
        <AtNoticebar icon="alert-circle">本系统仅限内部员工使用</AtNoticebar>
        {/*已登录，但不是cms系统用户提示绑定手机，但是不绑定后台也可以设置权限*/}
        {isWxBind && <AtNoticebar icon="alert-circle">如果您是员工，请联系业务负责人设置权限</AtNoticebar>}
        {isLogin && !isPhoneBind && <AtNoticebar icon="alert-circle">请绑定手机，方便管理员设置权限</AtNoticebar>}
      </AtCard>
    </View>
  );
};
