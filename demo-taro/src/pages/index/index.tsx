import React, { useCallback, useState, useEffect } from 'react';
import { View, Image, ScrollView, ViewProps } from '@tarojs/components';
import Taro, { useDidShow } from '@tarojs/taro';
import { AtActionSheet, AtActionSheetItem, AtDivider } from 'taro-ui';
import './index.scss';
import { mimService, useServiceStore, ProductEntity } from '../../services';
import { ChooseOption, TaroUtil, config } from '../../utils';
import template from 'url-template';

const Index: React.FC = () => {
  const mimStore = useServiceStore(mimService);
  const [isOpened, setIsOpened] = useState<boolean>(false); //指示面板开关
  const [showImages, setShowImages] = useState<any[]>(); //当前展示的商品列表
  const [unMoMoreState, setUnMoMoreState] = useState<boolean>(false); //没有更多数据开关
  const [searchImageItem, setSearchImageItem] = useState<string>(); //当前搜索的图片
  const [scrollTop, setScrollTop] = useState(0); //页面滚动条

  useEffect(() => {
    //每次首页的加载清除掉上一次裁剪图片留下的数据 防止混淆
    Taro.removeStorage({
      key: 'imageCropperBefore',
    });
    Taro.removeStorage({
      key: 'imageCropperAfter',
    });

    //开启分享和发送朋友
    Taro.showShareMenu({});
  }, []);

  //页面第一次加载不触发 第二次加载触发更新视图
  useDidShow(() => {
    const path = Taro.getStorageSync('imageCropperAfter');
    console.log('显示');
    console.log(path, 'path');
    if (path !== '') {
      setSearchImageItem(path);
      Taro.removeStorage({
        key: 'imageCropperAfter',
      });
      TaroUtil.onloadCropperAfter(path).then(imageSearch);
    }
  });

  const append = useCallback(
    (e: any, first?: boolean) => {
      console.log('到底部加载更多', e);
      console.log('    是否重新开始：', first);
      const c = first || !showImages ? 0 : showImages.length;
      const a = mimStore.allList ? mimStore.allList.length : 0;
      if (c < a) setShowImages(c + 10 > a ? mimStore.allList : mimStore.allList.slice(0, c + 10));
      else if (c === a && c !== 0) setUnMoMoreState(true);
    },
    [showImages, mimStore.allList],
  );

  //上传文件
  const imageSearch = useCallback((imageSearchReturn) => {
    console.log(imageSearchReturn, 'imageSearchReturn');
    const { attaches, src } = imageSearchReturn;
    setSearchImageItem(src);
    Taro.showLoading({
      title: '加载中',
    });
    if (attaches && attaches.length > 0) {
      const attach = attaches[0];
      mimService.imageSearch(attach.id).then((res) => {
        Taro.hideLoading();
        append(null, true);
        goTop();
        setUnMoMoreState(false); //重新查询的时候关闭没有更多提示
        console.log(res, 'imageSearch');
      });
      console.log(`${config.serverRoot}/download/${attach.id}`, '${config.serverRoot}/download/${attach.id}');
    }
  }, []);

  //调取图片的接口
  const chooseImage = useCallback((source: ChooseOption['source']) => {
    return () => {
      console.log(imageSearch, 'imageSearch');
      TaroUtil.chooseUploadImage({ source }).then(imageSearch);
      //选择之后关闭指示版
      setIsOpened(false);
    };
  }, []);

  //跳转目标小程序
  const targetMinProgram = useCallback((product: ProductEntity) => {
    return () => {
      let path = template.parse(product.shop.urlTemplate);
      path = path.expand(product);
      console.log(path, 'path');
      Taro.navigateToMiniProgram({
        appId: product.shop.appId,
        path,
        envVersion: 'release', // 打开正式版
      });
    };
  }, []);

  //裁剪图片
  const handleCropperImage = useCallback<ViewProps['onClick']>(
    (e) => {
      e.stopPropagation();
      //先存储当前选择的图片 给后面裁剪取出用
      Taro.setStorage({
        key: 'imageCropperBefore',
        data: searchImageItem,
      });
      //跳转原生裁剪页面
      Taro.navigateTo({
        url: '/pages/cropper/cropper',
      });
    },
    [searchImageItem],
  );

  //回到顶部，使用随机负值，防止不刷新
  const goTop = useCallback(() => setScrollTop(-Math.random()), []);
  return (
    <ScrollView
      enableBackToTop
      scrollY
      onScrollToLower={append}
      lowerThreshold={200}
      style={{ height: '100vh' }}
      scrollTop={scrollTop}
    >
      <View className="indexHead">
        <View className="uploadButton" onClick={() => setIsOpened(true)}>
          <Image
            src={searchImageItem || require('../../Images/uploadFileIconCopy.png')}
            className="uploadIcon"
            mode="widthFix"
            style={{ width: searchImageItem && '100%', height: searchImageItem && '100%' }}
          />
        </View>
        {searchImageItem && (
          <View className="imageCropper" onClick={handleCropperImage}>
            <Image src={require('../../Images/card.png')} />
          </View>
        )}
      </View>
      <View className="main">
        <View className="titleName">
          <View>商品列表</View>
        </View>
        {showImages ? (
          <View className="mainListBox">
            <View style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap', width: '100%' }}>
              {showImages?.map((item, index) => (
                <View className="TB_box" key={item.product.code} onClick={targetMinProgram(item.product)}>
                  <Image src={item.scoreImgUrl} />
                  <View className="infoBox">
                    <View className="shopName">{item.product?.name}</View>
                    <View className="title">
                      <View className="icon">{item.product?.shop.name}</View>
                      <View className="icon">{item.score.toFixed(2) * 100}%相似度</View>
                    </View>
                    <View className="shopName">
                      参考价：￥{item.product.price}
                      {/*总店没有/库存:{item.product.quantity || 100} 件*/}
                    </View>
                  </View>
                </View>
              ))}
            </View>
          </View>
        ) : (
          <Image src={require('../../Images/empty.png')} className="empty" />
        )}
      </View>

      {/*两个模态框*/}
      {/*回到顶部*/}
      <View className="motBtn returnTop" onClick={goTop}>
        <Image src={require('../../Images/returnTop.png')} />
      </View>
      {/*上传文件*/}
      <View className="motBtn" onClick={() => setIsOpened(true)}>
        <Image src={require('../../Images/uploadFileIconCopy.png')} />
      </View>

      {/*没有更多提示*/}
      {unMoMoreState && (
        <View style={{ width: '690rpx', margin: 'auto' }}>
          <AtDivider content="没有更多了" fontColor="#CCC" />
        </View>
      )}

      {/*指示面板*/}
      <AtActionSheet isOpened={isOpened} cancelText="取消" onClose={() => setIsOpened(false)}>
        <AtActionSheetItem onClick={chooseImage('message')}>从聊天记录中选择</AtActionSheetItem>
        <AtActionSheetItem onClick={chooseImage('album')}>从相册中选择</AtActionSheetItem>
        <AtActionSheetItem onClick={chooseImage('camera')}>拍照</AtActionSheetItem>
      </AtActionSheet>
    </ScrollView>
  );
};

export default Index;
