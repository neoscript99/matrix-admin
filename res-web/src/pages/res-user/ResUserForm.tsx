import React from 'react';
import { UserForm, commonRules, transforms, InputField, DatePickerField, SelectField } from 'oo-rest-mobx';
import { resUserService } from '../../services';
const { required } = commonRules;

export class ResUserForm extends UserForm {
  get userService() {
    return resUserService;
  }

  getExtraFormItem() {
    const {
      form,
      readonly,
      services: { dictService },
    } = this.props;
    const css = this.formItemCss;
    //如果是上级管理员，不做必输限制，方便管理
    const req = { rules: this.userService.isMainManager() ? [] : [required] };
    return (
      <React.Fragment>
        <InputField
          fieldId="idCard"
          formItemProps={{ label: '身份证', style: css }}
          formUtils={form}
          maxLength={18}
          decorator={{
            rules: [...req.rules, { min: 16, max: 18, message: '格式错误' }],
          }}
          disabled={readonly}
        />
        <DatePickerField
          fieldId="birthDay"
          formItemProps={{ label: '生日', style: css }}
          formUtils={form}
          required={req.rules.length > 0}
          disabled={readonly}
        />
        <InputField
          fieldId="title"
          formItemProps={{ label: '职务职称', style: css }}
          formUtils={form}
          maxLength={30}
          decorator={req}
          disabled={readonly}
        />
        <InputField
          fieldId="major"
          formItemProps={{ label: '专业', style: css }}
          formUtils={form}
          maxLength={30}
          decorator={req}
          disabled={readonly}
        />
        <SelectField
          fieldId="degreeCode"
          formItemProps={{ label: '最后学历', style: css }}
          formUtils={form}
          dataSource={dictService.getDict('pub-degree')}
          valueProp="code"
          labelProp="name"
          decorator={req}
          disabled={readonly}
        />
      </React.Fragment>
    );
  }

  get hideRoles() {
    const { hideRoles } = this.props;
    return !this.userService.isMainManager() || hideRoles;
  }

  get justSameDept() {
    const { justSameDept } = this.props;
    return !this.userService.isMainManager() || justSameDept;
  }
}
