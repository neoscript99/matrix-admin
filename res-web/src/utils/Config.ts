abstract class Config {
  dateFormat = 'MM-DD';
  timeFormat = 'YYYY-MM-DD HH:mm';
  abstract isDev(): boolean;
  demoUsers = [
    {
      name: '系统管理员',
      username: 'sys-admin',
      passwordHash: '8224b8e566ef89f147c58d6b07739a74cf05e2a56ce4bc4b227bd59858e9b6e9',
    },
    {
      name: '单位管理员',
      username: 'dept-admin',
      passwordHash: '8224b8e566ef89f147c58d6b07739a74cf05e2a56ce4bc4b227bd59858e9b6e9',
    },
    {
      name: '评审专家',
      username: 'expert',
      passwordHash: '8224b8e566ef89f147c58d6b07739a74cf05e2a56ce4bc4b227bd59858e9b6e9',
    },
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
