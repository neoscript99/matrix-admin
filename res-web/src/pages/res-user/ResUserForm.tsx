import React from 'react';
import {
  UserForm,
  commonRules,
  TooltipLabel,
  CommonValidators,
  InputField,
  SelectField,
  StringUtil,
  Entity,
} from 'oo-rest-mobx';
import { resUserService } from '../../services';
import { config } from '../../utils';
const { required } = commonRules;

const idCardLabel = <TooltipLabel tooltip="单位管理员录入身份证后不能修改，请仔细核对" label="身份证" />;
export class ResUserForm extends UserForm {
  get userService() {
    return resUserService;
  }
  async saveEntity(saveItem: Entity) {
    //系统管理员可以不输入身份证，不做检查
    if (saveItem.idCard && !(await resUserService.idCardCheck({ ...this.props.inputItem, ...saveItem })))
      throw '身份证已存在，请检查';
    const result = CommonValidators.idCard(saveItem.idCard);
    if (result.success) {
      saveItem.birthDay = result.info.birthDay;
      saveItem.sexCode = result.info.sex === '男' ? 'male' : 'female';
    } else {
      console.warn('ResUserForm.saveEntity: 身份证校验失败，', result);
    }
    return await super.saveEntity(saveItem);
  }
  getExtraFormItem() {
    const {
      form,
      readonly,
      inputItem,
      services: { dictService },
    } = this.props;
    const css = this.formItemCss;
    const isMainManager = this.userService.isMainManager();
    //如果是上级管理员，不做必输限制，方便管理
    const req = { rules: isMainManager ? [] : [required] };
    //单位管理员不能修改身份证，待确认
    const idCardRead: boolean = !isMainManager && !!inputItem && StringUtil.isNotBlank(inputItem.idCard);
    return (
      <React.Fragment>
        <InputField
          fieldId="idCard"
          formItemProps={{ label: idCardLabel, style: css }}
          formUtils={form}
          maxLength={18}
          decorator={{
            rules: [...req.rules, config.isDev() ? commonRules.idCardSimple : commonRules.idCard],
          }}
          readonly={readonly || idCardRead}
        />
        {readonly && (
          <InputField
            fieldId="birthDay"
            formItemProps={{ label: '生日', style: css }}
            formUtils={form}
            readonly={readonly}
          />
        )}
        <InputField
          fieldId="title"
          formItemProps={{ label: '职务职称', style: css }}
          formUtils={form}
          maxLength={30}
          decorator={req}
          readonly={readonly}
        />
        <InputField
          fieldId="major"
          formItemProps={{ label: '专业', style: css }}
          formUtils={form}
          maxLength={30}
          decorator={req}
          readonly={readonly}
        />
        <SelectField
          fieldId="degreeCode"
          formItemProps={{ label: '最后学历', style: css }}
          formUtils={form}
          dataSource={dictService.getDict('pub_degree')}
          valueProp="code"
          labelProp="name"
          decorator={req}
          readonly={readonly}
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
  get hideEnabled() {
    return !this.userService.isMainManager() || this.props.hideEnabled;
  }
  get autoGenerateAccount() {
    return !this.userService.isMainManager() || this.props.autoGenerateAccount;
  }
  get autoGenerateSex() {
    return true;
  }
}
