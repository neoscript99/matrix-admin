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
}
