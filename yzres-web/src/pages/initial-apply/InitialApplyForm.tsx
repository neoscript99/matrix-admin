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
  DictSelectField,
} from 'oo-rest-mobx';
import { Form } from 'antd';
import { dictService, topicService, userService } from '../../services';
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
    const uuid = moment().format('MMDDHHmmSSS');
    saveItem.prepareFinishDay = transforms.momentToDayString(saveItem.prepareFinishDay);
    saveItem.dept = userService.store.loginInfo.user!.dept;
    saveItem.topicCateCode = topicCateCode;
    saveItem.topicStatusCode = 'created';
    saveItem.topicCode = `${topicCateCode}${planYear}-${uuid}`;
    const user = { id: userService.store.loginInfo.user!.id };
    saveItem.personInCharge = user;
    const topic = await topicService.save(saveItem);
    return await super.saveEntity({ name: `${saveItem.topicName}立项申请`, applier: user, statusCode: 'draft', topic });
  }
  getForm() {
    const itemCss: React.CSSProperties = { width: '22em', marginBottom: '10px' };
    const {
      form,
      inputItem: { plan },
    } = this.props;
    const important = plan.topicCateCode === 'YZZD';
    const req = { rules: [commonRules.required] };
    return (
      <Form style={StyleUtil.flexForm()}>
        <InputField
          fieldId="topicName"
          formItemProps={{ label: '课题名', style: { ...itemCss, width: '44em' } }}
          formUtils={form}
          maxLength={30}
          decorator={req}
        />
        {important && (
          <InputField
            fieldId="originTopicName"
            formItemProps={{ label: '原课题名', style: { ...itemCss, width: '44em' } }}
            formUtils={form}
            maxLength={36}
          />
        )}
        {important && (
          <InputField
            fieldId="originTopicCode"
            formItemProps={{ label: '原课题编号', style: itemCss }}
            formUtils={form}
            maxLength={36}
          />
        )}
        <DictSelectField
          fieldId="topicSourceCode"
          formItemProps={{ label: '课题来源', style: itemCss }}
          dictService={dictService}
          dictType="yz-res-topic-source"
          formUtils={form}
          decorator={req}
          defaultSelectFirst
        />
        <DictSelectField
          fieldId="researchContentCode"
          formItemProps={{ label: '研究内容', style: itemCss }}
          dictService={dictService}
          dictType="yz-res-content"
          formUtils={form}
          decorator={req}
          defaultSelectFirst
        />
        <DictSelectField
          fieldId="researchSubjectCode"
          formItemProps={{ label: '涉及学科', style: itemCss }}
          dictService={dictService}
          dictType="yz-res-subject"
          formUtils={form}
          decorator={req}
          defaultSelectFirst
        />
        <DictSelectField
          fieldId="researchTargetCode"
          formItemProps={{ label: '研究对象', style: itemCss }}
          dictService={dictService}
          dictType="yz-res-target"
          formUtils={form}
          decorator={req}
          defaultSelectFirst
        />
        <DictSelectField
          fieldId="prepareAchieveFormCode"
          formItemProps={{ label: '成果拟形式', style: itemCss }}
          dictService={dictService}
          dictType="yz-res-achieve-form"
          formUtils={form}
          decorator={req}
          defaultSelectFirst
        />
        <DatePickerField
          fieldId="prepareFinishDay"
          formItemProps={{ label: '计划完成时间', style: itemCss }}
          formUtils={form}
          required={true}
          defaultDiffDays={365}
        />
      </Form>
    );
  }
}
