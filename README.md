## Modules

- `matrix-admin`: Backend server, developed with Springboot.
- `matrix-ui-service`: Frontend lib, interact with backend restful service, manage state with event Listener.
- `matrix-ui-com`: React component base on Ant Design and ProComponent.

## Taks

- [ ] 通知功能：待办、已阅、已办
- [ ] 支持微信小程序登录（获取手机）

## 更新日志
- matrix-admin@1.6.9
  - 小程序登录，绑定手机
  - 新增附件压缩功能
- matrix-admin@1.6.8
  - 支持json字段处理
  - 支持文件预览功能
  - 微信登录优化
- matrix-admin@1.6.7
  - 增加MatrixConfigProperties，配置改为matrix.default-roles, matrix.cas-client-enabled, matrix.token-expire-minutes
  - 微信登录优化

