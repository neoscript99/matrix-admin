import { View } from '@tarojs/components';
import Taro from '@tarojs/taro';
import { AtAvatar, AtMessage } from 'taro-ui';
import { config } from '../utils';
import React, { CSSProperties, useMemo } from 'react';
import { wxMaService, useServiceStore } from '../services';

const css: CSSProperties = { alignSelf: 'flex-start', alignItems: 'center', padding: '0.8rem' };
const titleCss: CSSProperties = { marginLeft: 10, fontSize: '1.2rem', fontWeight: 'bold', color: 'white' };
export interface PageHeaderProps {
  showAvatar?: boolean;
}
export const PageHeader: React.FC<PageHeaderProps> = ({ showAvatar }) => {
  const { userBind } = useServiceStore(wxMaService);
  const { asset } = config;
  const showTitle = useMemo(() => !showAvatar || !userBind, [showAvatar, userBind]);
  if (config.isWeapp) Taro.showShareMenu({});
  return (
    <View className="flex-col at-row__align--center">
      <View className="nav-bg" />
      <AtMessage />
      {showTitle && (
        <View className="flex-row" style={css}>
          <AtAvatar image={asset.logoIcon}></AtAvatar>
          <View style={titleCss}>Matrix Taro</View>
        </View>
      )}
      {!showTitle && (
        <View className="flex-col at-row__align--center" style={{ margin: '1rem 0' }}>
          <AtAvatar openData={{ type: 'userAvatarUrl' }} image={userBind.headimgurl} />
          <View style={{ color: 'white' }}>
            {userBind.nickname}
            {userBind.phoneNumber && `(${userBind.phoneNumber})`}
          </View>
        </View>
      )}
    </View>
  );
};
