import React from 'react';
import {
  EntityForm,
  StyleUtil,
  InputField,
  commonRules,
  DatePickerField,
  transforms,
  EntityFormProps,
  Entity,
  StringUtil,
  DictSelectField,
} from 'oo-rest-mobx';
import { Form } from 'antd';
import { dictService, topicService, loginService } from '../../services';
import moment from 'moment';
export interface InitialApplyFormProps extends EntityFormProps {
  inputItem: Entity;
}
export class InitialApplyForm extends EntityForm<InitialApplyFormProps> {
  async saveEntity(saveItem) {
    const {
      inputItem: {
        plan: { topicCateCode, planYear },
      },
    } = this.props;
    const day = moment().format('MMDD');
    const { topic } = saveItem;
    topic.dept = { id: loginService.dept!.id };
    topic.topicCateCode = topicCateCode;
    topic.topicStatusCode = 'created';
    topic.topicCode = `${topicCateCode}-${planYear}-${day}-${StringUtil.randomString(4)}`;
    const user = { id: loginService.user!.id };
    topic.personInCharge = user;
    const savedTopic = await topicService.save(topic);
    return await super.saveEntity({
      name: `${topic.topicName}立项申请`,
      applier: user,
      statusCode: 'draft',
      topic: { id: savedTopic.id },
    });
  }
  getForm() {
    const itemCss: React.CSSProperties = { width: '22em', marginBottom: '10px' };
    const {
      form,
      readonly,
      inputItem: { topic },
    } = this.props;
    const important = topic.topicCateCode === 'YZZD';
    const req = { rules: [commonRules.required] };
    return (
      <Form style={StyleUtil.flexForm()}>
        <InputField
          fieldId="topic.topicName"
          formItemProps={{ label: '课题名', style: { ...itemCss, width: '44em' } }}
          formUtils={form}
          maxLength={30}
          decorator={req}
          disabled={readonly}
        />
        {important && (
          <InputField
            fieldId="originTopicName"
            formItemProps={{ label: '原课题名', style: { ...itemCss, width: '44em' } }}
            formUtils={form}
            maxLength={36}
            disabled={readonly}
          />
        )}
        {important && (
          <InputField
            fieldId="originTopicCode"
            formItemProps={{ label: '原课题编号', style: itemCss }}
            formUtils={form}
            maxLength={36}
            disabled={readonly}
          />
        )}
        <DictSelectField
          fieldId="topic.topicSourceCode"
          formItemProps={{ label: '课题来源', style: itemCss }}
          dictService={dictService}
          dictType="res-topic-source"
          formUtils={form}
          decorator={req}
          defaultSelectFirst
          disabled={readonly}
        />
        <DictSelectField
          fieldId="topic.researchContentCode"
          formItemProps={{ label: '研究内容', style: itemCss }}
          dictService={dictService}
          dictType="res-content"
          formUtils={form}
          decorator={req}
          defaultSelectFirst
          disabled={readonly}
        />
        <DictSelectField
          fieldId="topic.researchSubjectCode"
          formItemProps={{ label: '涉及学科', style: itemCss }}
          dictService={dictService}
          dictType="res-subject"
          formUtils={form}
          decorator={req}
          defaultSelectFirst
          disabled={readonly}
        />
        <DictSelectField
          fieldId="topic.researchTargetCode"
          formItemProps={{ label: '研究对象', style: itemCss }}
          dictService={dictService}
          dictType="res-target"
          formUtils={form}
          decorator={req}
          defaultSelectFirst
          disabled={readonly}
        />
        <DictSelectField
          fieldId="topic.prepareAchieveFormCode"
          formItemProps={{ label: '成果拟形式', style: itemCss }}
          dictService={dictService}
          dictType="res-achieve-form"
          formUtils={form}
          decorator={req}
          defaultSelectFirst
          disabled={readonly}
        />
        <DatePickerField
          fieldId="topic.prepareFinishDay"
          formItemProps={{ label: '计划完成时间', style: itemCss }}
          formUtils={form}
          required={true}
          defaultDiffDays={365}
          disabled={readonly}
        />
      </Form>
    );
  }
}
