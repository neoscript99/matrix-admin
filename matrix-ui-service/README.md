## 更新日志
- v1.1.1 （兼容）
  - 支持pnpm  
- v1.1.0 （兼容）
  - 增加DomainService.query作为前台表格控件通用查询方法
  - 支持Criteria嵌套属性的处理
- v1.0.7 （不兼容上一个版本）
  - 支持微信小程序登录、绑定手机号
- v1.0.6 （兼容上一个版本）
  - 增加taroFetch和useServiceStore的初始化功能
  - service增加 setStore 和 setCurrentItem
  - 默认错误处理：MessageUtil.error 和 loginService.logout  
- v1.0.5
  - CasConfig合并到MatrixConfig
  - 微信登录优化
- v1.0.4
    - AfterLogin改造
    - 新增ServiceYard基类
    - 微信登录优化