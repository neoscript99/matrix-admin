import React from 'react';
import { EntityForm, UploadWrap } from 'oo-rest-mobx';
import { Descriptions } from 'antd';
import { DescriptionsProps } from 'antd/lib/descriptions';
import { DictViewer } from '../../components';
import { attachmentService } from '../../services';

const Col = Descriptions.Item;
export class TopicView extends EntityForm {
  getForm() {
    const { inputItem: topic } = this.props;
    if (!topic) return null;
    const { initialPlan, personInCharge, finishPlan, supports, members } = topic;
    const important = initialPlan.planCateCode === 'YZZD';
    const descProps: DescriptionsProps = { bordered: true, style: { marginBottom: '1rem' } };
    return (
      <div>
        <Descriptions title="基本信息" {...descProps}>
          <Col label="课题名">{topic.topicName}</Col>
          {important && <Col label="原课题名">{topic.originTopicName}</Col>}
          {important && <Col label="原立项编号">{topic.originInitialCode}</Col>}
          <Col label="课题类别">
            <DictViewer dictType="res-topic-cate" dictCode={topic.topicCateCode} />
          </Col>
          <Col label="课题来源">
            <DictViewer dictType="res-topic-source" dictCode={topic.topicSourceCode} />
          </Col>
          <Col label="研究内容">
            <DictViewer dictType="res-content" dictCode={topic.researchContentCode} />
          </Col>
          <Col label="涉及学科">
            <DictViewer dictType="res-subject" dictCode={topic.researchSubjectCode} />
          </Col>
          <Col label="研究对象">
            <DictViewer dictType="res-target" dictCode={topic.researchTargetCode} />
          </Col>
          <Col label="成果拟形式">
            <DictViewer dictType="res-achieve-form" dictCode={topic.prepareAchieveFormCodes} multipleMode={true} />
          </Col>
          <Col label="计划完成时间">{topic.prepareFinishDay}</Col>
          <Col label="申报盲评文本">
            <UploadWrap value={[topic.initialReport]} disabled={true} attachmentService={attachmentService} />
          </Col>
        </Descriptions>
        <Descriptions title="课题成员" {...descProps}>
          <Col label="负责人">{personInCharge.name}</Col>
          <Col label="性别">
            <DictViewer dictType="pub_sex" dictCode={personInCharge.sexCode} />
          </Col>
          <Col label="身份证号码">{personInCharge.idCard}</Col>
          <Col label="出生日期">{personInCharge.birthDay}</Col>
          <Col label="职务职称">{personInCharge.title}</Col>
          <Col label="专业">{personInCharge.major}</Col>
          <Col label="最后学历">
            <DictViewer dictType="pub_degree" dictCode={personInCharge.degreeCode} />
          </Col>
          <Col label="其他成员">
            <div>
              {members.map(m => (
                <div key={m.id}>
                  {m.name}-{m.idCard}
                </div>
              ))}
            </div>
          </Col>
        </Descriptions>
      </div>
    );
  }
}
