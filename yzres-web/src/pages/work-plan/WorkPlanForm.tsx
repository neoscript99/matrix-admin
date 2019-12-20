import React from 'react';
import { Form } from 'antd';
import {
  EntityForm,
  commonRules,
  genRules,
  InputField,
  InputNumberField,
  SelectField,
  DatePickerField,
  transforms,
} from 'oo-rest-mobx';
import moment from 'moment';
import { dictService } from '../../services';
const { required, numberRule } = commonRules;

export class WorkPlanForm extends EntityForm {
  saveEntity(saveItem) {
    saveItem.planBeginDay = transforms.momentToDayString(saveItem.planBeginDay);
    saveItem.planEndDay = transforms.momentToDayString(saveItem.planEndDay);
    saveItem.finishDeadline = transforms.momentToDayString(saveItem.finishDeadline);
    return super.saveEntity(saveItem);
  }
  getForm() {
    const { form } = this.props;
    const day = { rules: [genRules.momentDay(true)] };
    const year = moment().year();
    return (
      <Form labelCol={{ span: 8 }} wrapperCol={{ span: 16 }}>
        <InputField
          fieldId="planName"
          formItemProps={{ label: '计划标题' }}
          formUtils={form}
          maxLength={36}
          decorator={{ rules: [required] }}
        />
        <InputNumberField
          fieldId="planYear"
          formItemProps={{ label: '立项年度' }}
          formUtils={form}
          min={1900}
          max={9999}
          decorator={{ initialValue: year, rules: [numberRule] }}
        />
        <SelectField
          fieldId="topicCateCode"
          formItemProps={{ label: '课题类别' }}
          formUtils={form}
          dataSource={dictService.getDict('yz-res-topic-cate')}
          labelProp="name"
          valueProp="code"
          decorator={{ rules: [required] }}
        />
        <DatePickerField
          fieldId="planBeginDay"
          formItemProps={{ label: '申报开始日期' }}
          formUtils={form}
          decorator={day}
        />
        <DatePickerField
          fieldId="planEndDay"
          formItemProps={{ label: '申报截止日期' }}
          formUtils={form}
          decorator={day}
        />
        <DatePickerField
          fieldId="finishDeadline"
          formItemProps={{ label: '结题截止日期' }}
          formUtils={form}
          decorator={day}
        />
      </Form>
    );
  }
}
