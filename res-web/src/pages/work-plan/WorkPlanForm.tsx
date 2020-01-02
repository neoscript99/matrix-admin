import React from 'react';
import { Form } from 'antd';
import {
  EntityForm,
  commonRules,
  InputField,
  InputNumberField,
  SelectField,
  DatePickerField,
} from 'oo-rest-mobx';
import moment from 'moment';
import { dictService } from '../../services';
const { required, numberRule } = commonRules;

export class WorkPlanForm extends EntityForm {
  getForm() {
    const { form } = this.props;
    const year = moment().year();
    return (
      <Form labelCol={{ span: 8 }} wrapperCol={{ span: 16 }}>
        <InputField
          fieldId="planName"
          formItemProps={{ label: '计划标题' }}
          formUtils={form}
          maxLength={36}
          decorator={{ rules: [required] }}
        />
        <InputNumberField
          fieldId="planYear"
          formItemProps={{ label: '立项年度' }}
          formUtils={form}
          min={1900}
          max={9999}
          decorator={{ initialValue: year, rules: [numberRule] }}
        />
        <SelectField
          fieldId="topicCateCode"
          formItemProps={{ label: '课题类别' }}
          formUtils={form}
          dataSource={dictService.getDict('res-topic-cate')}
          labelProp="name"
          valueProp="code"
          decorator={{ rules: [required] }}
        />
        <DatePickerField fieldId="planBeginDay" formItemProps={{ label: '申报开始日期' }} formUtils={form} required />
        <DatePickerField fieldId="planEndDay" formItemProps={{ label: '申报截止日期' }} formUtils={form} required />
        <DatePickerField fieldId="finishDeadline" formItemProps={{ label: '结题截止日期' }} formUtils={form} required />
      </Form>
    );
  }
}
