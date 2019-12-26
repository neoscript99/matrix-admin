import React, { Component } from 'react';
import { YzUserForm } from '../yz-user';
import { EntityForm, UserFormProps } from 'oo-rest-mobx';
import { adminServices, yzUserService } from '../../services';
import { Modal, Form } from 'antd';

interface P {
  topic: any;
  visible: boolean;
}

export class TopicMember extends Component<P> {
  render() {
    const { topic, visible } = this.props;
    const YzUserFormWrap = EntityForm.formWrapper(YzUserForm);
    return (
      <Modal width="76em" visible={visible} title={`${topic.topicName}成员管理`} okText="提交" maskClosable={false}>
        <YzUserFormWrap
          domainService={yzUserService}
          title="新增成员"
          services={adminServices}
          hideRoles
          justSameDept
        />
      </Modal>
    );
  }
}
