import React from 'react';
import { Typography } from 'antd';
const { Title, Paragraph, Text } = Typography;

export const Welcome = (props: any) => (
  <Typography style={{ fontSize: '1.1em' }}>
    <Title level={2}>后台框架</Title>
    <Paragraph>
      <ul>
        <li>
          <Text code>语言</Text>Groovy、Java
        </li>
        <li>
          <Text code>容器</Text>SpringBoot单体应用，暂不支持微服务
        </li>
        <li>
          <Text code>持久层</Text>使用grails的gorm模块，底层还是基于Jpa-Hibernate
        </li>
        <li>
          <Text code>数据库版本管理</Text>Flyway
        </li>
        <li>
          <Text code>Service</Text>通用基类AbstractService
        </li>
        <li>
          <Text code>Controller</Text>通用基类DomainController
        </li>
        <li>
          <Text code>权限控制</Text>通用基类SecureController
        </li>
        <li>
          <Text code>认证</Text>支持传统session，也支持前后端分离的jwt模式
        </li>
      </ul>
    </Paragraph>
    <Title level={2}>前台框架</Title>
    <Paragraph>
      <ul>
        <li>
          <Text code>语言</Text>Typescript、React
        </li>
        <li>
          <Text code>状态管理</Text>React-Hook + useServiceStore + DomainStore
        </li>
        <li>
          <Text code>前端逻辑</Text>通用基类DomainService.ts
        </li>
        <li>
          <Text code>组件</Text>Ant Design & ProComponents
        </li>
      </ul>
    </Paragraph>
  </Typography>
);
