import React from 'react';
import {
  InfoIcon,
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
  TooltipLabel,
} from 'oo-rest-mobx';
import { Typography, Form, Popover } from 'antd';
import { dictService, topicService, loginService, applyService, adminServices } from '../../services';
import moment from 'moment';
import { DeptUserForm, DeptUserFormState } from '../../components';
import { config } from '../../utils';

const { Title, Paragraph } = Typography;

export interface InitialApplyFormProps extends EntityFormProps {
  inputItem: Entity;
}

interface S extends DeptUserFormState {
  qualification: QualificationCheckResult;
}

interface QualificationCheckResult {
  success: boolean;
  reasons: string[];
}
const { flexFormCss, oneSpanFormItemCss, twoSpanFormItemCss } = StyleUtil.commonStyle;

export class InitialApplyForm extends DeptUserForm<InitialApplyFormProps, S> {
  state = { qualification: { success: true, reasons: [] } } as S;
  async componentDidMount() {
    await super.componentDidMount();
    const { inputItem } = this.props;
    //新增需做校验，修改时不用
    if (!inputItem?.id) {
      const qualification = await topicService.checkQualification(
        this.props.inputItem.initialPlan.id,
        loginService.dept!.id,
      );
      this.setState({ qualification });
    }
  }

  getContainerProps() {
    const props = super.getContainerProps();
    const footer = this.state.qualification.success ? undefined : null;
    props.modalProps = { ...props.modalProps, maskClosable: true, footer };
    return props;
  }
  async saveEntity(saveItem) {
    const {
      inputItem: {
        id,
        initialPlan: { planYear },
      },
    } = this.props;
    //如果是新申请，自动设置部分属性
    if (!id) {
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
      saveItem.topicStatusCode = 'created';
      //立项编码在审批成功后再生成
      saveItem.topicCode = `${saveItem.topicCateCode}-${planYear}-${day}-${StringUtil.randomString(4)}`;
    }
    return await super.saveEntity(saveItem);
  }

  getForm() {
    const {
      form,
      readonly,
      inputItem: { initialPlan },
    } = this.props;
    const { deptUserList, qualification } = this.state;
    const important = initialPlan.planCateCode === 'YZZD';
    const req = { rules: [commonRules.required] };
    const isDev = config.isDev();
    const isCourseMethod = form?.getFieldValue('researchContentCode') === 'course-material-method';
    if (qualification.success)
      return (
        <Form style={flexFormCss}>
          <InputField
            fieldId="topicName"
            formItemProps={{ label: '课题名', style: twoSpanFormItemCss }}
            formUtils={form}
            maxLength={30}
            decorator={req}
            readonly={readonly}
          />
          {important && (
            <InputField
              fieldId="originTopicName"
              formItemProps={{ label: '原课题名', style: twoSpanFormItemCss }}
              formUtils={form}
              maxLength={36}
              readonly={readonly}
            />
          )}
          {important && (
            <InputField
              fieldId="originInitialCode"
              formItemProps={{ label: '原立项编号', style: oneSpanFormItemCss }}
              formUtils={form}
              maxLength={36}
              readonly={readonly}
            />
          )}
          <SelectField
            fieldId="personInCharge.id"
            formItemProps={{
              label: <TooltipLabel label="课题负责人" tooltip="可先在‘基础信息>>用户管理’中添加" />,
              style: oneSpanFormItemCss,
            }}
            formUtils={form}
            dataSource={deptUserList}
            valueProp="id"
            labelProp="name"
            showSearch
            decorator={req}
            readonly={readonly}
          />
          <DictSelectField
            fieldId="topicCateCode"
            formItemProps={{ label: '课题类别', style: oneSpanFormItemCss }}
            dictService={dictService}
            dictType="res-topic-cate"
            formUtils={form}
            decorator={req}
            defaultSelectFirst={isDev}
            parentDictCode={initialPlan.planCateCode}
            readonly={readonly}
          />
          <DictSelectField
            fieldId="topicSourceCode"
            formItemProps={{ label: '课题来源', style: oneSpanFormItemCss }}
            dictService={dictService}
            dictType="res-topic-source"
            formUtils={form}
            decorator={req}
            defaultSelectFirst={isDev}
            readonly={readonly}
          />
          <DictSelectField
            fieldId="researchTargetCode"
            formItemProps={{ label: '研究对象', style: oneSpanFormItemCss }}
            dictService={dictService}
            dictType="res-target"
            formUtils={form}
            decorator={req}
            defaultSelectFirst={isDev}
            readonly={readonly}
          />
          <DictSelectField
            fieldId="researchContentCode"
            formItemProps={{ label: '研究内容', style: oneSpanFormItemCss }}
            dictService={dictService}
            dictType="res-content"
            formUtils={form}
            decorator={req}
            defaultSelectFirst={isDev}
            readonly={readonly}
          />
          <DictSelectField
            fieldId="researchSubjectCode"
            formItemProps={{
              label: <TooltipLabel label="涉及学科" tooltip="选择‘课程教材教法’时需设置" />,
              style: oneSpanFormItemCss,
            }}
            dictService={dictService}
            dictType="res-subject"
            formUtils={form}
            decorator={isCourseMethod ? req : undefined}
            readonly={readonly || !isCourseMethod}
          />
          <DictSelectField
            fieldId="prepareAchieveFormCodes"
            formItemProps={{ label: '成果拟形式', style: oneSpanFormItemCss }}
            dictService={dictService}
            dictType="res-achieve-form"
            formUtils={form}
            decorator={req}
            mode="multiple"
            readonly={readonly}
            defaultSelectFirst={isDev}
          />
          <DatePickerField
            fieldId="prepareFinishDay"
            formItemProps={{ label: '计划完成时间', style: oneSpanFormItemCss }}
            formUtils={form}
            required={true}
            defaultDiffDays={365}
            disabledDate={current => !!current && current.isAfter(moment(initialPlan.finishDeadline))}
            readonly={readonly}
          />
          <UploadField
            fieldId="initialReport"
            formUtils={form}
            formItemProps={{
              label: initialReportLabel,
              style: oneSpanFormItemCss,
            }}
            readonly={readonly}
            maxNumber={1}
            maxSizeMB={10}
            required
            attachmentService={adminServices.attachmentService}
          />
        </Form>
      );
    else
      return (
        <Typography>
          <Title level={3}>贵单位暂时无法申请立项，原因如下：</Title>
          <Paragraph style={{ fontSize: '1.1em' }}>
            <ul>
              {qualification.reasons.map(r => (
                <li key={r}>
                  <pre>{r}</pre>
                </li>
              ))}
            </ul>
          </Paragraph>
        </Typography>
      );
  }
}
const initialReportLabelContent = (
  <p>
    一、选题
    <br />
    （一）选题背景；（二）选题意义；（三）国内外相关研究述评。 <br />
    二、内容
    <br />
    （一）研究基本思路；（二）研究目标；（三）研究内容；（四）研究方法、步骤；（五）重点难点分析。
    <br /> 三、预期价值
    <br />
    （一）理论创新程度或实际价值；（二）成果可能去向。
    <br /> 四、前期准备
    <br />
    （一）前期准备工作（已收集的数据，进行的调查研究，完成的部分初稿等）；（二）已有与本课题相关的研究成果；（三）参考文献（以上各限填10项）。
    <br />
    <b>（限4000字内）</b>
  </p>
);
const initialReportLabel = (
  <Popover title={<Title level={3}>要求说明</Title>} content={initialReportLabelContent} style={{ maxWidth: 600 }}>
    <span>申报盲评文本</span>
    <InfoIcon />
  </Popover>
);
