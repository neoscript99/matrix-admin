import React from 'react';
import { Form } from 'antd';
import { EntityForm, commonRules, InputField, InputNumberField, SelectField, DatePickerField } from 'oo-rest-mobx';
import moment from 'moment';
import { dictService } from '../../services';
const { required, number } = commonRules;

export class ReviewPlanForm extends EntityForm {
  getForm() {
    const { form, readonly } = this.props;
    const year = moment().year();
    return (
      <Form labelCol={{ span: 8 }} wrapperCol={{ span: 16 }}>
        <InputField
          fieldId="planName"
          formItemProps={{ label: '计划标题' }}
          formUtils={form}
          maxLength={36}
          decorator={{ rules: [required] }}
          readonly={readonly}
        />
        <InputNumberField
          fieldId="planYear"
          formItemProps={{ label: '立项年度' }}
          formUtils={form}
          min={1900}
          max={9999}
          decorator={{ initialValue: year, rules: [required,number] }}
          readonly={readonly}
        />
        <SelectField
          fieldId="reviewTypeCode"
          formItemProps={{ label: '评比类型' }}
          formUtils={form}
          dataSource={dictService.getDict('res-review-type')}
          labelProp="name"
          valueProp="code"
          decorator={{ rules: [required] }}
          readonly={readonly}
        />
        <DatePickerField
          fieldId="planBeginDay"
          formItemProps={{ label: '申报开始日期' }}
          formUtils={form}
          required
          defaultDiffDays={0}
          readonly={readonly}
        />
        <DatePickerField
          fieldId="planEndDay"
          formItemProps={{ label: '申报截止日期(当天15:00截止)' }}
          formUtils={form}
          required
          defaultDiffDays={90}
          readonly={readonly}
        />
      </Form>
    );
  }
}
