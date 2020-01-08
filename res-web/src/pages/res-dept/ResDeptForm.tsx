import React from 'react';
import { DeptForm, commonRules, InputField, InputNumberField, SelectField } from 'oo-rest-mobx';
import { resDeptTypeService } from '../../services';

const { required, email, numberRule } = commonRules;

export class ResDeptForm extends DeptForm {
  getExtraFormItem() {
    const { form } = this.props;
    const req = { rules: [commonRules.required] };
    return (
      <React.Fragment>
        <SelectField
          fieldId="type.code"
          formItemProps={{ label: '机构类型' }}
          dataSource={resDeptTypeService.store.allList}
          valueProp="code"
          labelProp="name"
          decorator={req}
          formUtils={form}
        />
        <InputNumberField fieldId="classNumber" formItemProps={{ label: '班级数' }} formUtils={form} />
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
        <InputField fieldId="note" formItemProps={{ label: '备注' }} formUtils={form} maxLength={64} />
      </React.Fragment>
    );
  }
}
