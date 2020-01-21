import React from 'react';
import {
  EntityForm,
  StyleUtil,
  DictSelectField,
  UploadField,
  commonRules,
} from 'oo-rest-mobx';
import { Form } from 'antd';
import { adminServices, applyService, dictService, loginService } from '../../services';

export class FinishApplyForm extends EntityForm {
  async saveEntity(saveItem) {
    const { inputItem } = this.props;
    const user = { id: loginService.user!.id };
    saveItem.finishApply = await applyService.save({
      name: `${inputItem!.topicName}结题申请`,
      type: 'topic_finish_apply',
      applier: user,
      statusCode: 'draft',
    });
    return await super.saveEntity(saveItem);
  }
  getForm() {
    const itemCss: React.CSSProperties = { width: '22em', marginBottom: '10px' };
    const { form, readonly } = this.props;
    const req = { rules: [commonRules.required] };
    return (
      <Form style={StyleUtil.flexForm()}>
        <DictSelectField
          fieldId="achieveFormCodes"
          formItemProps={{ label: '最后成果形式', style: itemCss }}
          dictService={dictService}
          dictType="res-achieve-form"
          formUtils={form}
          decorator={req}
          mode="multiple"
          readonly={readonly}
        />
        <UploadField
          fieldId="mainReport"
          formUtils={form}
          formItemProps={{ label: '主报告', style: itemCss }}
          required
          readonly={readonly}
          maxNumber={1}
          attachmentService={adminServices.attachmentService}
        />
        <UploadField
          fieldId="supports"
          formUtils={form}
          formItemProps={{ label: '支撑材料', style: itemCss }}
          readonly={readonly}
          required
          maxNumber={4}
          attachmentService={adminServices.attachmentService}
        />
      </Form>
    );
  }
}
