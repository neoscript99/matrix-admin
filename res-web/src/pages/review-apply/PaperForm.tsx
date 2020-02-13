import React from 'react';
import { InputField, commonRules, UploadField, SelectField, Entity } from 'oo-rest-mobx';
import { Form } from 'antd';
import { adminServices, loginService } from '../../services';
import { DeptUserForm, DeptUserFormState } from '../../components';
import { config } from '../../utils';

export class PaperForm extends DeptUserForm {
  state = {} as DeptUserFormState;
  saveEntity(saveItem: Entity) {
    saveItem.dept = { id: loginService.dept!.id };
    return super.saveEntity(saveItem);
  }
  getForm() {
    const { form, readonly } = this.props;
    const req = { rules: [commonRules.required] };
    return (
      <Form>
        <InputField
          fieldId="title"
          formItemProps={{ label: '论文题目' }}
          formUtils={form}
          decorator={req}
          readonly={readonly}
        />
        <SelectField
          fieldId="personInCharge.id"
          formItemProps={{ label: '论文作者' }}
          formUtils={form}
          dataSource={this.state.deptUserList}
          valueProp="id"
          labelProp="name"
          decorator={req}
          showSearch={true}
          defaultSelectFirst={config.isDev()}
          readonly={readonly}
        />
        <UploadField
          fieldId="paperFile"
          formUtils={form}
          formItemProps={{ label: '正文' }}
          required
          readonly={readonly}
          maxNumber={1}
          attachmentService={adminServices.attachmentService}
        />
      </Form>
    );
  }
}
