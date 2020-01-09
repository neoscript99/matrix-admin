import React from 'react';
import { Typography, Divider } from 'antd';

const { Title, Paragraph, Text } = Typography;

export const Welcome = (props: any) => (
  <Typography style={{ fontSize: '1.1em' }}>
    <Title>科研项目管理系统</Title>
    <Title level={2}>介绍</Title>
    <Paragraph>
      本系统目标是实现科研项目管理流程的电子化，支持历史数据的查询、统计，并为进一步的数据分析、流程优化提供支撑。
    </Paragraph>
    <Title level={2}>立项管理流程</Title>
    <Paragraph>
      <ul>
        <li>
          <Text code>系统管理员</Text>发布立项计划
        </li>
        <li>
          <Text code>单位管理员</Text>录入课题基本信息，提交立项申请
        </li>
        <li>
          <Text code>系统管理员</Text>审核立项申请
        </li>
      </ul>
    </Paragraph>
    <Title level={2}>课题结题流程</Title>
    <Paragraph>
      <ul>
        <li>
          <Text code>单位管理员</Text>录入课题结题资料，提交结题申请
        </li>
        <li>
          <Text code>系统管理员</Text>审核结题申请
        </li>
      </ul>
    </Paragraph>
    <Title level={2}>评比流程</Title>
    <Paragraph>
      <ul>
        <li>
          <Text code>系统管理员</Text>发布评比计划
        </li>
        <li>
          <Text code>单位管理员</Text>录入成果、论文基本信息，提交参评申请
        </li>
        <li>
          <Text code>系统管理员</Text>审核参评申请，邀请评审专家
        </li>
        <li>
          <Text code>评审专家</Text>对成果、论文进行打分
        </li>
        <li>
          <Text code>系统后台</Text>自动计算平均分，并统计排名
        </li>
        <li>
          <Text code>系统管理员</Text>导出评比结果
        </li>
      </ul>
    </Paragraph>
  </Typography>
);
