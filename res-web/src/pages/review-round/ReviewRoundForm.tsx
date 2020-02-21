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
  Entity,
  SelectField,
  StringUtil,
  StyleUtil,
} from 'oo-rest-mobx';
import { dictService, resUserService, reviewRoundExpertService } from '../../services';
import { config } from '../../utils';
import moment from 'moment';
const { required, number, array } = commonRules;
const { flexFormCss, oneSpanFormItemCss, twoSpanFormItemCss } = StyleUtil.commonStyle;
interface S {
  expertIds?: any[];
  availableExperts: Entity[];
}
export interface ReviewRoundFormProps extends EntityFormProps {
  parentList: any[];
}
export class ReviewRoundForm extends EntityForm<ReviewRoundFormProps, S> {
  saveEntity(saveItem: Entity): Promise<Entity> {
    //saveItem会出现parentRound: {id: undefined},导致后台保存失败
    if (!saveItem.parentRound?.id) saveItem.parentRound = null;
    return super.saveEntity(saveItem);
  }
  state = { availableExperts: [] } as S;
  resetPassword = `${moment().year()}_${StringUtil.randomString(4)}`;
  async componentDidMount() {
    const { form, inputItem } = this.props;
    let availableExperts = await resUserService.getAvailableExperts();
    if (inputItem?.id) {
      const selectedExpert = await reviewRoundExpertService
        .list({ criteria: { eq: [['round.id', inputItem?.id]] } })
        .then(res => res.results.map(re => re.expert));
      form?.setFieldsValue({ experts: selectedExpert.map(expert => expert.id) });
      availableExperts = selectedExpert.concat(availableExperts);
    }
    this.setState({ availableExperts });
  }

  getForm() {
    const { form, readonly, inputItem, parentList } = this.props;
    const isDev = config.isDev();
    const req = { rules: [required] };
    const pid = form?.getFieldValue('parentRound.id');
    const parentRound = pid && parentList.find(r => r.id === pid);
    return (
      <Form style={flexFormCss}>
        <InputField
          fieldId="planName"
          formItemProps={{ label: '评比计划', style: oneSpanFormItemCss }}
          value={inputItem?.plan.planName}
          readonly={true}
        />
        <InputField
          fieldId="name"
          formItemProps={{
            label: <TooltipLabel tooltip="例如：第一轮、第二轮" label="轮次名" />,
            style: oneSpanFormItemCss,
          }}
          formUtils={form}
          decorator={req}
          readonly={readonly}
        />
        <InputNumberField
          fieldId="grades"
          formItemProps={{
            label: <TooltipLabel tooltip="专家评分后，计算得分并根据这里的配置分多个等级" label="本轮等级数" />,
            style: oneSpanFormItemCss,
          }}
          formUtils={form}
          min={2}
          max={100}
          decorator={{ initialValue: 4, rules: [number] }}
          readonly={readonly}
        />
        <DictSelectField
          fieldId="avgAlgorithmCode"
          formItemProps={{ label: '平均分算法', style: oneSpanFormItemCss }}
          defaultSelectFirst={isDev}
          formUtils={form}
          dictService={dictService}
          dictType="res-avg-algorithm"
          decorator={req}
          readonly={readonly}
        />
        <DatePickerField
          fieldId="endDay"
          formItemProps={{ label: '评分截止日期', style: oneSpanFormItemCss }}
          formUtils={form}
          required
          defaultDiffDays={30}
          readonly={readonly}
        />
        <InputField
          fieldId="expertPassword"
          formItemProps={{
            label: <TooltipLabel tooltip="每次产生新的密码，防止恶意登录" label="专家重置密码" />,
            style: oneSpanFormItemCss,
          }}
          formUtils={form}
          decorator={{ rules: [required], initialValue: this.resetPassword }}
          readonly={readonly}
        />
        <SelectField
          fieldId="experts"
          dataSource={this.state.availableExperts}
          originValueType="array"
          formItemProps={{ label: '邀请专家', style: twoSpanFormItemCss }}
          defaultSelectFirst={isDev}
          formUtils={form}
          valueProp="id"
          labelProp="name"
          decorator={{ rules: [array] }}
          mode="multiple"
        />
        {parentList.length > 0 && (
          <SelectField
            fieldId="parentRound.id"
            dataSource={parentList}
            formItemProps={{ label: '上轮评分', style: oneSpanFormItemCss }}
            formUtils={form}
            valueProp="id"
            labelProp="name"
            allowClear
          />
        )}
        {parentList.length > 0 && parentRound && (
          <InputNumberField
            fieldId="parentPassGrades"
            formItemProps={{
              label: `上轮通过等级数(共${parentRound.grades}个)`,
              style: oneSpanFormItemCss,
            }}
            formUtils={form}
            min={1}
            max={parentRound?.grades}
            decorator={{ rules: [number] }}
            readonly={readonly}
          />
        )}
      </Form>
    );
  }
}
