declare let SERVER_ROOT;
export class Config {
  timeFormat = 'YYYY-MM-DD HH:mm';
  serverRoot = SERVER_ROOT;
  get isDev() {
    return this.serverRoot == 'http://localhost:8080' || this.serverRoot.endsWith('ngrok.io');
  }
  get isTest() {
    return this.serverRoot == 'https://shop.feathermind.cn:1443';
  }
  get uploadUrl() {
    return `${this.serverRoot}/upload`;
  }
  get isWeapp() {
    return process.env.TARO_ENV === 'weapp';
  }
  get isH5() {
    return process.env.TARO_ENV === 'h5';
  }
  asset = {
    logoIcon: 'https://cdn.feathermind.cn/fmind/fmind-logo.png',
  };
}
