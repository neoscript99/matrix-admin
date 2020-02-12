import React from 'react';
import { InputField, commonRules, TooltipLabel, UploadField, SelectField, DictSelectField } from 'oo-rest-mobx';
import { Form } from 'antd';
import { adminServices, dictService } from '../../services';
import { DeptUserForm } from '../../components';
import { config } from '../../utils';

export class TopicAchieveForm extends DeptUserForm {
  getForm() {
    const { form, readonly } = this.props;
    const req = { rules: [commonRules.required] };
    const isDev = config.isDev();
    return (
      <Form>
        <InputField
          fieldId="achieveName"
          formItemProps={{ label: '成果名称' }}
          formUtils={form}
          decorator={req}
          maxLength={36}
          readonly={readonly}
        />
        <SelectField
          fieldId="personInCharge.id"
          formItemProps={{ label: '成果负责人' }}
          formUtils={form}
          dataSource={this.state.deptUserList}
          valueProp="id"
          labelProp="name"
          decorator={req}
          showSearch={true}
          readonly={readonly}
        />
        <SelectField
          fieldId="members"
          formItemProps={{ label: '课题组成员' }}
          formUtils={form}
          dataSource={this.state.deptUserList}
          valueProp="id"
          labelProp="name"
          decorator={req}
          showSearch={true}
          mode="multiple"
          readonly={readonly}
        />
        <DictSelectField
          fieldId="achieveCateCode"
          formItemProps={{ label: '成果类别' }}
          dictService={dictService}
          dictType="res-achieve-cate"
          formUtils={form}
          decorator={req}
          defaultSelectFirst={isDev}
          readonly={readonly}
        />
        <DictSelectField
          fieldId="projectLevelCode"
          formItemProps={{ label: '立项情况' }}
          dictService={dictService}
          dictType="res-project-level"
          formUtils={form}
          decorator={req}
          defaultSelectFirst={isDev}
          readonly={readonly}
        />
        <UploadField
          fieldId="summary"
          formUtils={form}
          formItemProps={{
            label: (
              <TooltipLabel
                tooltip="说明：限300字，分三点简述：为什么做该课题（约20%字数），怎么在做（约50%字数），效果怎样（约30%字数）。"
                label="成果简述（内容提要）"
              />
            ),
          }}
          required
          readonly={readonly}
          maxNumber={1}
          attachmentService={adminServices.attachmentService}
        />
        <UploadField
          fieldId="mainReport"
          formUtils={form}
          formItemProps={{ label: <TooltipLabel label="成果主报告盲评文本" tooltip="限5000字" /> }}
          required
          readonly={readonly}
          maxNumber={1}
          attachmentService={adminServices.attachmentService}
        />
      </Form>
    );
  }
}
