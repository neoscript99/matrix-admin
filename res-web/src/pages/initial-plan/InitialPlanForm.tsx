import React from 'react';
import { Form } from 'antd';
import { EntityForm, commonRules, InputField, InputNumberField, SelectField, DatePickerField } from 'oo-rest-mobx';
import moment from 'moment';
import { dictService } from '../../services';
const { required, numberRule } = commonRules;

export class InitialPlanForm extends EntityForm {
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
          disabled={readonly}
        />
        <InputNumberField
          fieldId="planYear"
          formItemProps={{ label: '立项年度' }}
          formUtils={form}
          min={1900}
          max={9999}
          decorator={{ initialValue: year, rules: [numberRule] }}
          disabled={readonly}
        />
        <SelectField
          fieldId="planCateCode"
          formItemProps={{ label: '计划类型' }}
          formUtils={form}
          dataSource={dictService.getDict('res-plan-cate')}
          labelProp="name"
          valueProp="code"
          decorator={{ rules: [required] }}
          disabled={readonly}
        />
        <DatePickerField
          fieldId="planBeginDay"
          formItemProps={{ label: '申报开始日期' }}
          formUtils={form}
          required
          defaultDiffDays={0}
          disabled={readonly}
        />
        <DatePickerField
          fieldId="planEndDay"
          formItemProps={{ label: '申报截止日期(当天15:00截止)' }}
          formUtils={form}
          required
          defaultDiffDays={90}
          disabled={readonly}
        />
        <DatePickerField
          fieldId="finishDeadline"
          formItemProps={{ label: '结题截止日期' }}
          formUtils={form}
          required
          defaultDiffDays={730}
          disabled={readonly}
        />
        <InputNumberField
          fieldId="maxNumberPerDept"
          formItemProps={{ label: '限制申请数' }}
          formUtils={form}
          min={0}
          max={99}
          title="本设置项和单位本身申报数，取最小值"
          disabled={readonly}
        />
      </Form>
    );
  }
}
