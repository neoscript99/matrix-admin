import React from 'react';
import { Button, message, Transfer } from 'antd';
import {
  Entity,
  UserFormProps,
  AdminPageProps,
  EntityListState,
  EntityFormProps,
  ListOptions,
  ListResult,
} from 'oo-rest-mobx';
import { ResUserService } from '../../services/ResUserService';
import { ResUserList } from '../res-user';
import { resTopicUserService, topicMemberService, topicService, loginService } from '../../services';
import { Redirect } from 'react-router';
interface S extends EntityListState {
  targetKeys: string[];
}
export class TopicMember extends ResUserList<AdminPageProps, S> {
  state = { targetKeys: [] as string[], dataList: [] } as S;
  /**
   * 忽略EntityList处理
   *
   * Transfer数据源为同部门下所有用户
   * 原来已选择的用户，存放到state.targetKeys
   */
  async componentDidMount() {
    const { currentItem: topic } = topicService.store;
    const targetKeys = await topicMemberService.getMembers(topic.id!).then(members => members.map(member => member.id));
    this.query();
    this.setState({ targetKeys });
  }
  render() {
    const { currentItem: topic } = topicService.store;
    if (!topic || !topic.id) return <Redirect to="/" />;
    const { formProps, dataList, targetKeys } = this.state;
    return (
      <div>
        {this.getEntityFormPop(formProps)}
        <div className="flex-row" style={{ justifyContent: 'flex-start' }}>
          <Button type="primary" onClick={this.handleCreate.bind(this)}>
            新增成员
          </Button>
        </div>
        <Transfer
          style={{ display: 'flex', alignItems: 'center', margin: '0.5rem 0' }}
          listStyle={{ flexGrow: 1, height: '70vh' }}
          dataSource={dataList.map(item => ({ key: item.id as string, ...item }))}
          targetKeys={targetKeys}
          onChange={this.handleChange.bind(this)}
          render={item => `${item.name} - ${item.idCard || ''}`}
          showSearch
        />
        <div className="flex-row" style={{ justifyContent: 'center' }}>
          <Button size="large" type="primary" onClick={this.handleSave.bind(this)}>
            保存
          </Button>
          <div style={{ width: '2em' }} />
          <Button size="large" onClick={this.handleReturn.bind(this)}>
            取消
          </Button>
        </div>
      </div>
    );
  }
  get domainService(): ResUserService {
    return resTopicUserService;
  }
  query(): Promise<ListResult> {
    return this.domainService
      .listAll(this.getQueryParam())
      .then(res => (this.setState({ dataList: res.results }), res));
  }
  getQueryParam(): ListOptions {
    return {
      criteria: { eq: [['dept.id', loginService.store.loginInfo.user!.dept.id]], isNotNull: [['idCard']] },
      orders: ['name'],
    };
  }

  handleChange(targetKeys, direction, moveKeys) {
    this.setState({ targetKeys });
  }
  handleReturn() {
    const { history } = this.props;
    history && history.goBack();
  }
  handleFormSuccess(item: Entity): void {
    super.handleFormSuccess(item);
    const { targetKeys } = this.state;
    this.setState({ targetKeys: [...targetKeys, item.id as string] });
  }

  handleSave() {
    const { history } = this.props;
    const { currentItem: topic } = topicService.store;
    topicMemberService
      .saveMembers(topic.id as string, this.state.targetKeys)
      .then(() => {
        message.success('保存成功');
        history && history.goBack();
      })
      .catch(err => {
        console.error(err);
        message.error(err);
      });
  }
  getInitItem(): Entity {
    return { enabled: true };
  }

  genFormProps(action: string, item?: Entity, exProps?: Partial<EntityFormProps>): UserFormProps {
    const props = super.genFormProps(action, item, exProps);
    props.hideRoles = true;
    props.justSameDept = true;
    props.autoGenerateAccount = true;
    props.hideEnabled = true;
    return props;
  }
}
