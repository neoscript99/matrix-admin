import React from 'react';
import { Typography, Divider } from 'antd';

const { Title, Paragraph, Text } = Typography;

export const Welcome = (props: any) => (
  <Typography>
    <Title>科研项目管理系统</Title>
    <Title level={2}>介绍</Title>
    <Paragraph>
      本系统目前支持模拟登录方式包括：<Text mark>Post（form）</Text>、<Text mark>Post（json）</Text>、
      <Text mark>Get</Text>， 管理员通过<Text code>登录系统管理</Text>
      页面，录入系统的名称、主页、登录Action、用户域名、密码域名等基本信息， 普通用户可以通过
      <Text code>http://模拟登录系统/go?url=被登录系统</Text>这样的地址格式，进行模拟登录。
    </Paragraph>
    <Paragraph>本系统模拟登录保存的密码信息进行了加密处理，规避了数据泄漏的风险。</Paragraph>
    <Title level={2}>流程说明</Title>
    <Paragraph>
      <ul>
        <li>用户第一次打开系统时需要录入登录信息，之后每次打开对应系统时就会自动登录</li>
        <li>
          当用户在原系统修改了密码会导致登录失败，这种情况下请打开本系统<Text code>个人密码管理</Text>
          ，针对登录失败的系统进行修改更新
        </li>
        <li>
          如果选择了直接跳转，该系统将不再做模拟登录，如需修改打开本系统<Text code>个人密码管理</Text>
        </li>
        <li>
          模拟登录管理员也可以通过<Text code>登录信息管理</Text>菜单，管理所有用户的登录信息，
          对于以上两种情况，管理员可帮用户删除对应的登录信息，用户下次打开时就可以重新录入
        </li>
      </ul>
    </Paragraph>
  </Typography>
);
