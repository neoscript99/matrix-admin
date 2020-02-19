import React from 'react';
import { Form } from 'antd';
import {
  EntityForm,
  commonRules,
  InputField,
  InputNumberField,
  SelectField,
  DatePickerField,
  TooltipLabel,
  DictSelectField,
} from 'oo-rest-mobx';
import { dictService } from '../../services';
const { required, number } = commonRules;

export class ReviewRoundForm extends EntityForm {
  getForm() {
    const { form, readonly, inputItem } = this.props;
    return (
      <Form labelCol={{ span: 8 }} wrapperCol={{ span: 16 }}>
        <InputField
          fieldId="planName"
          formItemProps={{ label: '评比计划' }}
          value={inputItem?.plan.planName}
          readonly={true}
        />
        <InputField
          fieldId="name"
          formItemProps={{ label: <TooltipLabel tooltip="例如：第一轮、第二轮" label="轮次名" /> }}
          formUtils={form}
          readonly={readonly}
        />
        <InputNumberField
          fieldId="grades"
          formItemProps={{
            label: <TooltipLabel tooltip="专家评分后，计算得分并根据这里的配置分多个等级" label="等级数" />,
          }}
          formUtils={form}
          min={1}
          max={100}
          decorator={{ initialValue: 4, rules: [required, number] }}
          readonly={readonly}
        />
        <DictSelectField
          fieldId="avgAlgorithmCode"
          formItemProps={{ label: '平均分算法' }}
          formUtils={form}
          dictService={dictService}
          dictType="res-avg-algorithm"
          decorator={{ rules: [required] }}
          readonly={readonly}
        />
        <DatePickerField
          fieldId="beginDay"
          formItemProps={{ label: '评分开始日期' }}
          formUtils={form}
          required
          defaultDiffDays={0}
          readonly={readonly}
        />
        <DatePickerField
          fieldId="endDay"
          formItemProps={{ label: '评分截止日期' }}
          formUtils={form}
          required
          defaultDiffDays={90}
          readonly={readonly}
        />
      </Form>
    );
  }
}
