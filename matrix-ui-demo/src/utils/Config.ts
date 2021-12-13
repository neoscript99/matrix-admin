export class Config {
  env = process.env.NODE_ENV;
  isDev() {
    return this.env === 'development';
  }
  get serverRoot() {
    return this.isDev() ? 'http://localhost:8080' : '';
  }
  dateFormat = 'MM-DD';
  timeFormat = 'YYYY-MM-DD HH:mm';
  demoUsers = [
    {
      name: '管理员',
      username: 'admin',
    },
    {
      name: '普通用户',
      username: 'test.user',
    },
    {
      name: '匿名用户',
      username: 'anonymous',
    },
  ];
}
