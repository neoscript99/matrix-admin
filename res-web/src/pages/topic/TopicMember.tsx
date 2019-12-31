import React from 'react';
import { Button, message, Transfer } from 'antd';
import { OperatorBar, Entity, UserFormProps, UserListProps, EntityListState } from 'oo-rest-mobx';
import { ResUserService } from '../../services/ResUserService';
import { ResUserList } from '../res-user';
import { resTopicUserService, topicMemberService, topicService, loginService } from '../../services';
import { Redirect } from 'react-router';
interface S extends EntityListState {
  targetKeys: string[];
}
export class TopicMember extends ResUserList<UserListProps, S> {
  state = { targetKeys: [] as string[], dataList: [] } as S;

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

  /**
   * 忽略EntityList处理
   *
   * Transfer数据源为同部门下所有用户
   * 原来已选择的用户，存放到state.targetKeys
   */
  async componentDidMount() {
    const { currentItem: topic } = topicService.store;
    const targetKeys = await topicMemberService
      .listAll({ criteria: { eq: [['topic.id', topic.id]] } })
      .then(res => res.results.map(tm => tm.member.id));
    const dataList = await resTopicUserService
      .listAll({
        criteria: { eq: [['dept.id', loginService.store.loginInfo.user!.dept.id]] },
        orders: ['name'],
      })
      .then(res => res.results);
    this.setState({ targetKeys, dataList });
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

  getFormProps(action: string, item?: Entity): UserFormProps {
    const props = super.getFormProps(action, item);
    props.hideRoles = true;
    props.justSameDept = true;
    props.autoGenerateAccount = true;
    props.hideEnabled = true;
    return props;
  }
}
