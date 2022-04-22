import React, { useCallback, useState } from 'react';
import { View } from '@tarojs/components';
import { useServiceStore, yard } from '../services';
import { PageHeader, Auth } from '../component';
import { AtCard, AtSwitch } from 'taro-ui';

const Home: React.FC = () => {
  const paramStore = useServiceStore(yard.adminServices.paramService);
  const [showAvatar, setShowAvatar] = useState(false);
  const onSwitchChange = useCallback((v: boolean) => {
    console.log('onSwitchChange: ', v);
    setShowAvatar(v);
  }, []);
  return (
    <View>
      <PageHeader showAvatar={showAvatar} />
      <AtCard>
        {paramStore.allList &&
          paramStore.allList.map((p) => (
            <View key={p.code}>
              {p.name} - {p.code}
            </View>
          ))}
        <AtSwitch border onChange={onSwitchChange} title="头像" />
      </AtCard>
    </View>
  );
};

const HomeAuth: React.FC = () => (
  <Auth>
    <Home />
  </Auth>
);
export default HomeAuth;
