import React from 'react';
import { Transfer } from 'antd';
import { OperatorBar, EntityListState, Entity, UserFormProps } from 'oo-rest-mobx';
import { YzUserService } from '../../services/YzUserService';
import { YzUserList } from '../yz-user';
import { topicMemberService, topicService, userService } from '../../services';

export class TopicMember extends YzUserList {
  targetKeys?: string[];
  componentDidUpdate() {
    const { history } = this.props;
    const { currentItem: topic } = topicService.store;
    if (!topic || !topic.id) history!.push('/');
    return null;
  }

  render() {
    const { dataList, formProps } = this.state;
    return (
      <div>
        {this.getEntityFormPop(formProps)}
        <OperatorBar
          onCreate={this.handleCreate.bind(this)}
          operatorVisible={{ create: true }}
          operatorEnable={{ create: true }}
        />
        <Transfer
          style={{ display: 'flex', alignItems: 'center', marginTop: '0.2rem' }}
          listStyle={{ flexGrow: 1, height: '70vh' }}
          dataSource={dataList.map(item => ({ key: item.id as string, ...item }))}
          targetKeys={this.targetKeys}
          onChange={this.handleChange}
          render={item => `${item.name} - ${item.idCard}`}
        />
      </div>
    );
  }
  get domainService(): YzUserService {
    return topicMemberService;
  }

  /**
   * 数据源为部门下所有用户
   */
  query() {
    console.debug(`${this.className}(${this.toString()}).query`);
    const p = this.domainService.listAll({
      criteria: { eq: [['dept.id', userService.store.loginInfo.user!.dept.id]] },
    });
    this.updateTableProps(p);
    return p;
  }

  handleChange = (targetKeys, direction, moveKeys) => {
    this.targetKeys = targetKeys;
    this.forceUpdate();
  };

  getInitItem() {
    return { account: '' };
  }
  getFormProps(action: string, item?: Entity): UserFormProps {
    const props = super.getFormProps(action, item);
    props.hideRoles = true;
    props.justSameDept = true;
    return props;
  }
}
