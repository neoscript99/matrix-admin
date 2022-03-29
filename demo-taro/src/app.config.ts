import Taro from '@tarojs/taro';

const config: Taro.Config = {
  pages: ['pages/Home'],
  window: {
    backgroundTextStyle: 'light',
    navigationBarBackgroundColor: '#2A8CE5',
    navigationBarTitleText: 'Matrix Taro',
    navigationBarTextStyle: 'white',
  },
  tabBar: {
    color: '#626567',
    selectedColor: '#2A8CE5',
    backgroundColor: '#FBFBFB',
    borderStyle: 'white',
    list: [
      {
        pagePath: 'pages/Home',
        text: '首页',
        iconPath: './asset/home.png',
        selectedIconPath: './asset/home_focus.png',
      },
      {
        pagePath: 'pages/Home',
        text: '个人',
        iconPath: './asset/more.png',
        selectedIconPath: './asset/more_focus.png',
      },
    ],
  },
};
export default config;
