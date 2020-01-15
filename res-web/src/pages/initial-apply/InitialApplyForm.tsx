import React from 'react';
import {
  EntityForm,
  StyleUtil,
  InputField,
  commonRules,
  DatePickerField,
  EntityFormProps,
  Entity,
  StringUtil,
  DictSelectField,
  SelectField,
  UploadField,
} from 'oo-rest-mobx';
import { Typography, Form } from 'antd';
import { dictService, topicService, loginService, resUserService, applyService } from '../../services';
import moment from 'moment';
import { config } from '../../utils';

const { Title, Paragraph } = Typography;

export interface InitialApplyFormProps extends EntityFormProps {
  inputItem: Entity;
}

interface S {
  deptUserList?: any[];
  qualification: QualificationCheckResult;
}

interface QualificationCheckResult {
  success: boolean;
  reasons: string[];
}

export class InitialApplyForm extends EntityForm<InitialApplyFormProps, S> {
  async componentDidMount() {
    const dept = loginService.dept!;
    const deptUserList = await resUserService.getDeptUsers(dept);
    const qualification = await topicService.checkQualification(this.props.inputItem.initialPlan.id, dept.id);
    this.setState({ deptUserList, qualification });
  }

  async saveEntity(saveItem) {
    const {
      inputItem: {
        initialPlan: { topicCateCode, planYear },
      },
    } = this.props;
    const user = { id: loginService.user!.id };
    const day = moment().format('MMDD');
    const apply = {
      name: `${saveItem.topicName}立项申请`,
      type: 'topic_initial_apply',
      applier: user,
      statusCode: 'draft',
    };
    const initialApply = await applyService.save(apply);
    saveItem.initialApply = initialApply;
    saveItem.dept = { id: loginService.dept!.id };
    saveItem.topicCateCode = topicCateCode;
    saveItem.topicStatusCode = 'created';
    saveItem.topicCode = `${topicCateCode}-${planYear}-${day}-${StringUtil.randomString(4)}`;
    return await super.saveEntity(saveItem);
  }

  getForm() {
    if(!this.state)
      return null;
    const itemCss: React.CSSProperties = { width: '22em', marginBottom: '10px' };
    const {
      form,
      readonly,
      inputItem: { initialPlan },
    } = this.props;
    const { deptUserList, qualification } = this.state;
    const important = initialPlan.topicCateCode === 'YZZD';
    const req = { rules: [commonRules.required] };
    if (qualification.success)
      return (
        <Form style={StyleUtil.flexForm()}>
          <InputField
            fieldId="topicName"
            formItemProps={{ label: '课题名', style: { ...itemCss, width: '44em' } }}
            formUtils={form}
            maxLength={30}
            decorator={req}
            readonly={readonly}
          />
          {important && (
            <InputField
              fieldId="originTopicName"
              formItemProps={{ label: '原课题名', style: { ...itemCss, width: '44em' } }}
              formUtils={form}
              maxLength={36}
              readonly={readonly}
            />
          )}
          {important && (
            <InputField
              fieldId="originTopicCode"
              formItemProps={{ label: '原课题编号', style: itemCss }}
              formUtils={form}
              maxLength={36}
              readonly={readonly}
            />
          )}
          <SelectField
            fieldId="personInCharge.id"
            formItemProps={{ label: '课题负责人', style: itemCss }}
            formUtils={form}
            dataSource={deptUserList}
            valueProp="id"
            labelProp="name"
            decorator={req}
            readonly={readonly}
          />
          <DictSelectField
            fieldId="topicSourceCode"
            formItemProps={{ label: '课题来源', style: itemCss }}
            dictService={dictService}
            dictType="res-topic-source"
            formUtils={form}
            decorator={req}
            defaultSelectFirst
            readonly={readonly}
          />
          <DictSelectField
            fieldId="researchContentCode"
            formItemProps={{ label: '研究内容', style: itemCss }}
            dictService={dictService}
            dictType="res-content"
            formUtils={form}
            decorator={req}
            defaultSelectFirst
            readonly={readonly}
          />
          <DictSelectField
            fieldId="researchSubjectCode"
            formItemProps={{ label: '涉及学科', style: itemCss }}
            dictService={dictService}
            dictType="res-subject"
            formUtils={form}
            decorator={req}
            defaultSelectFirst
            readonly={readonly}
          />
          <DictSelectField
            fieldId="researchTargetCode"
            formItemProps={{ label: '研究对象', style: itemCss }}
            dictService={dictService}
            dictType="res-target"
            formUtils={form}
            decorator={req}
            defaultSelectFirst
            readonly={readonly}
          />
          <DictSelectField
            fieldId="prepareAchieveFormCode"
            formItemProps={{ label: '成果拟形式', style: itemCss }}
            dictService={dictService}
            dictType="res-achieve-form"
            formUtils={form}
            decorator={req}
            defaultSelectFirst
            readonly={readonly}
          />
          <DatePickerField
            fieldId="prepareFinishDay"
            formItemProps={{ label: '计划完成时间', style: itemCss }}
            formUtils={form}
            required={true}
            defaultDiffDays={365}
            readonly={readonly}
          />
          <UploadField
            fieldId="initialReport"
            formItemProps={{ label: '申报盲评文本', style: itemCss }}
            decorator={req}
            readonly={readonly}
            action={`${config.serverRoot}/upload`}
          />
        </Form>
      );
    else
      return (
        <Typography>
          <Title level={3}>贵单位暂时无法申请立项，原因如下：</Title>
          <Paragraph style={{ fontSize: '1.1em' }}>
            <ol>
              {qualification.reasons.map(r => (
                <li key={r}>{r}</li>
              ))}
            </ol>
          </Paragraph>
        </Typography>
      );
  }
}
