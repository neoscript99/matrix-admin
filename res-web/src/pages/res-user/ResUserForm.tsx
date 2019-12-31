import React from 'react';
import { UserForm, commonRules, transforms, InputField, DatePickerField, SelectField } from 'oo-rest-mobx';
import { resUserService } from '../../services';
const { required } = commonRules;

export class ResUserForm extends UserForm {
  get userService() {
    return resUserService;
  }

  saveEntity(saveItem) {
    saveItem.birthDay = transforms.momentToDayString(saveItem.birthDay);
    return super.saveEntity(saveItem);
  }
  getExtraFormItem() {
    const {
      form,
      services: { dictService },
    } = this.props;
    const css = this.formItemCss;
    const req = { rules: [required] };
    return (
      <React.Fragment>
        <InputField
          fieldId="idCard"
          formItemProps={{ label: '身份证', style: css }}
          formUtils={form}
          maxLength={18}
          decorator={{
            rules: [required, { min: 16, max: 18, message: '格式错误' }],
          }}
        />
        <DatePickerField fieldId="birthDay" formItemProps={{ label: '生日', style: css }} formUtils={form} required />
        <InputField
          fieldId="title"
          formItemProps={{ label: '职务职称', style: css }}
          formUtils={form}
          maxLength={30}
          decorator={req}
        />
        <InputField
          fieldId="major"
          formItemProps={{ label: '专业', style: css }}
          formUtils={form}
          maxLength={30}
          decorator={req}
        />
        <SelectField
          fieldId="degreeCode"
          formItemProps={{ label: '最后学历', style: css }}
          formUtils={form}
          dataSource={dictService.getDict('pub-degree')}
          valueProp="code"
          labelProp="name"
          decorator={req}
        />
      </React.Fragment>
    );
  }
}
