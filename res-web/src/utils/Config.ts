abstract class Config {
  dateFormat = 'MM-DD';
  timeFormat = 'YYYY-MM-DD HH:mm';
  abstract isDev(): boolean;
  demoUsers = [
    { name: '系统管理员', username: 'sys-admin', password: 'abc000' },
    { name: '单位管理员', username: 'dept-admin', password: 'abc000' },
    { name: '评审专家', username: 'expert', password: 'abc000' },
  ];
}

export class DevConfig extends Config {
  serverRoot = 'http://localhost:8080';
  serverLogout = false;
  env = 'dev';
  isDev() {
    return true;
  }
}
export class ProdConfig extends Config {
  serverRoot = '';
  serverLogout = true;
  env = 'prod';
  isDev() {
    return false;
  }
}
