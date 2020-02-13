import React from 'react';
import { EntityForm, StyleUtil, DictSelectField, UploadField, commonRules } from 'oo-rest-mobx';
import { Form } from 'antd';
import { adminServices, applyService, dictService, loginService } from '../../services';
import { config } from '../../utils';

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
    const { form, readonly } = this.props;
    const req = { rules: [commonRules.required] };
    return (
      <Form>
        <DictSelectField
          fieldId="achieveFormCodes"
          formItemProps={{ label: '最后成果形式' }}
          dictService={dictService}
          dictType="res-achieve-form"
          formUtils={form}
          decorator={req}
          mode="multiple"
          defaultSelectFirst={config.isDev()}
          readonly={readonly}
        />
        <UploadField
          fieldId="mainReport"
          formUtils={form}
          formItemProps={{ label: '主报告' }}
          required
          readonly={readonly}
          maxNumber={1}
          attachmentService={adminServices.attachmentService}
        />
        <UploadField
          fieldId="supports"
          formUtils={form}
          formItemProps={{ label: '支撑材料' }}
          readonly={readonly}
          required
          listType="picture"
          maxNumber={4}
          attachmentService={adminServices.attachmentService}
        />
      </Form>
    );
  }
}
