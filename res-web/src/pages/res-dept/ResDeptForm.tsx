import React from 'react';
import { DeptForm, commonRules, InputField, InputNumberField } from 'oo-rest-mobx';
const { required, email, numberRule } = commonRules;
export class ResDeptForm extends DeptForm {
  getExtraFormItem() {
    const { form } = this.props;
    return (
      <React.Fragment>
        <InputNumberField
          fieldId="defaultApplyNum"
          formItemProps={{ label: '默认申报数' }}
          formUtils={form}
          min={0}
          max={100}
          decorator={{ initialValue: 3 }}
        />
        <InputField fieldId="contact" formItemProps={{ label: '联系人' }} formUtils={form} maxLength={18} />
        <InputField fieldId="telephone" formItemProps={{ label: '联系电话' }} formUtils={form} maxLength={18} />
        <InputField fieldId="cellphone" formItemProps={{ label: '联系手机' }} formUtils={form} maxLength={18} />
        <InputField fieldId="shortDial" formItemProps={{ label: '联系短号' }} formUtils={form} maxLength={18} />
        <InputField
          fieldId="email"
          formItemProps={{ label: '单位邮箱' }}
          formUtils={form}
          maxLength={18}
          decorator={{ rules: [email] }}
        />
        <InputField fieldId="address" formItemProps={{ label: '单位地址' }} formUtils={form} maxLength={18} />
      </React.Fragment>
    );
  }
}
