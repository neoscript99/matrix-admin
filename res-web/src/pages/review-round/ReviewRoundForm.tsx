import React from 'react';
import { Form } from 'antd';
import {
  EntityForm,
  commonRules,
  InputField,
  InputNumberField,
  DatePickerField,
  TooltipLabel,
  DictSelectField,
  EntityFormProps,
  DeptEntity,
  SelectField,
} from 'oo-rest-mobx';
import { dictService, resUserService, reviewRoundExpertService } from '../../services';
import { config } from '../../utils';
const { required, number, array } = commonRules;
interface S {
  expertIds?: any[];
  expertListAll: DeptEntity[];
}
export class ReviewRoundForm extends EntityForm<EntityFormProps, S> {
  state = { expertListAll: [] } as S;
  componentDidMount(): void {
    const { form, inputItem } = this.props;
    resUserService
      .list({ criteria: { dept: { eq: [['seq', 66666]] } } })
      .then(res => this.setState({ expertListAll: res.results as DeptEntity[] }));
    if (inputItem?.id)
      reviewRoundExpertService
        .list({ criteria: { eq: [['round.id', inputItem?.id]] } })
        .then(res => form?.setFieldsValue({ experts: res.results.map(re => re.expert.id) }));
  }

  getForm() {
    const { form, readonly, inputItem } = this.props;
    const isDev = config.isDev();
    const req = { rules: [required] };
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
          decorator={req}
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
          decorator={{ initialValue: 4, rules: [number] }}
          readonly={readonly}
        />
        <DictSelectField
          fieldId="avgAlgorithmCode"
          formItemProps={{ label: '平均分算法' }}
          defaultSelectFirst={isDev}
          formUtils={form}
          dictService={dictService}
          dictType="res-avg-algorithm"
          decorator={req}
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
        <SelectField
          fieldId="experts"
          dataSource={this.state.expertListAll}
          originValueType="array"
          formItemProps={{ label: '邀请专家' }}
          defaultSelectFirst={isDev}
          formUtils={form}
          valueProp="id"
          labelProp="name"
          decorator={{ rules: [array] }}
          mode="multiple"
        />
      </Form>
    );
  }
}
