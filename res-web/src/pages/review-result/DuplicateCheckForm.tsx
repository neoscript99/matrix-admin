import React from 'react';
import { EntityForm, InputField, CheckboxField, EntityFormProps, TextAreaField } from 'oo-rest-mobx';
import { Form } from 'antd';
import { paperService, topicAchieveService } from '../../services';
export interface DuplicateCheckFormProps extends EntityFormProps {
  achieve: any;
}
export class DuplicateCheckForm extends EntityForm<DuplicateCheckFormProps> {
  async saveEntity(saveItem) {
    const { id, reviewPlan } = this.props.achieve;
    const duplicateCheck = await super.saveEntity(saveItem);
    const achieveService = reviewPlan.reviewTypeCode === 'topic' ? topicAchieveService : paperService;
    await achieveService.save({ id, duplicateCheck });
    return duplicateCheck;
  }

  getForm() {
    const { form } = this.props;
    return (
      <Form>
        <InputField formItemProps={{ label: '标题' }} fieldId="name" value={this.props.achieve.name} readonly />
        <CheckboxField formUtils={form} fieldId="success" formItemProps={{ label: '是否通过' }} />
        <TextAreaField formUtils={form} fieldId="desc" maxLength={512} rows={3} formItemProps={{ label: '说明' }} />
      </Form>
    );
  }
}
