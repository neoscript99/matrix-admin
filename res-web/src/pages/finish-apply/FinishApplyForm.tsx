import React from 'react';
import {
  EntityForm,
  InputField,
  StyleUtil,
  DictSelectField,
  UploadField,
  commonRules,
  DatePickerField,
} from 'oo-rest-mobx';
import { Form } from 'antd';
import { applyService, dictService, loginService } from '../../services';

export class FinishApplyForm extends EntityForm {
  async saveEntity(saveItem) {
    const { inputItem } = this.props;
    const user = { id: loginService.user!.id };
    const finishApply = await applyService.save({
      name: `${inputItem!.topicName}结题申请`,
      type: 'topic_finish_apply',
      applier: user,
      statusCode: 'draft',
    });
    return await super.saveEntity({ ...saveItem, finishApply });
  }
  getForm() {
    const itemCss: React.CSSProperties = { width: '22em', marginBottom: '10px' };
    const { form, readonly } = this.props;
    const req = { rules: [commonRules.required] };
    return (
      <Form style={StyleUtil.flexForm()}>
        <DictSelectField
          fieldId="achieveFormCode"
          formItemProps={{ label: '最后成果形式', style: itemCss }}
          dictService={dictService}
          dictType="res-achieve-form"
          formUtils={form}
          decorator={req}
          defaultSelectFirst
          readonly={readonly}
        />
        <InputField
          fieldId="topicCert"
          formItemProps={{ label: '结题证书编号', style: itemCss }}
          maxLength={30}
          readonly={true}
        />
        <UploadField
          fieldId="mainReport"
          formItemProps={{ label: '主报告', style: itemCss }}
          decorator={req}
          readonly={readonly}
        />
        <UploadField
          fieldId="topicSupport"
          formItemProps={{ label: '支撑材料', style: itemCss }}
          decorator={req}
          readonly={readonly}
        />
        <DatePickerField
          fieldId="finishDay"
          formItemProps={{ label: '完成时间', style: itemCss }}
          formUtils={form}
          required={true}
          readonly={readonly}
        />
      </Form>
    );
  }
}
