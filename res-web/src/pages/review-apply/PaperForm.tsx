import React from 'react';
import { InputField, commonRules, UploadField, SelectField } from 'oo-rest-mobx';
import { Form } from 'antd';
import { adminServices } from '../../services';
import { DeptUserForm } from '../../components';

export class PaperForm extends DeptUserForm {
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
          fieldId="author.id"
          formItemProps={{ label: '论文作者' }}
          formUtils={form}
          dataSource={this.state.deptUserList}
          valueProp="id"
          labelProp="name"
          decorator={req}
          showSearch={true}
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
