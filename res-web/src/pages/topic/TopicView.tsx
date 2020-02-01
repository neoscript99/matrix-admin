import React from 'react';
import { EntityForm, UploadWrap } from 'oo-rest-mobx';
import { Descriptions, Tag } from 'antd';
import { DescriptionsProps } from 'antd/lib/descriptions';
import { DictViewer } from '../../components';
import { attachmentService } from '../../services';

const Col = Descriptions.Item;
export class TopicView extends EntityForm {
  getForm() {
    const { inputItem: topic } = this.props;
    if (!topic) return null;
    const { initialPlan, personInCharge, initialApply, finishApply } = topic;
    const important = initialPlan.planCateCode === 'YZZD';
    const descProps: DescriptionsProps = { bordered: true, size: 'small', style: { marginBottom: '1rem' } };
    return (
      <div>
        <Descriptions title="立项材料" {...descProps}>
          <Col label="课题名">{topic.topicName}</Col>
          {important && <Col label="原课题名">{topic.originTopicName}</Col>}
          {important && <Col label="原立项编号">{topic.originInitialCode}</Col>}
          <Col label="课题类别">
            <DictViewer dictType="res-topic-cate" dictCode={topic.topicCateCode} />
          </Col>
          <Col label="所属计划">{initialPlan.planName}</Col>
          <Col label="立项年度">{initialPlan.planYear}</Col>
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
          <Col label="立项申请状态">
            <DictViewer dictType="res-apply-status" dictCode={initialApply.statusCode} />
          </Col>
          <Col label="立项编号">{topic.initialCode}</Col>
          <Col label="课题状态">
            <DictViewer dictType="res-topic-status" dictCode={topic.topicStatusCode} />
          </Col>
        </Descriptions>
        {finishApply && (
          <Descriptions title="结题材料" {...descProps}>
            <Col label="主报告">
              <UploadWrap value={[topic.mainReport]} disabled={true} attachmentService={attachmentService} />
            </Col>
            <Col label="成果支撑材料" span={2}>
              <UploadWrap
                value={topic.supports}
                listType="picture-card"
                showUploadList={{ showPreviewIcon: false }}
                disabled={true}
                attachmentService={attachmentService}
              />
            </Col>
            <Col label="最后成果形式">
              <DictViewer dictType="res-achieve-form" dictCode={topic.achieveFormCodes} multipleMode={true} />
            </Col>
            <Col label="结题申请状态">
              <DictViewer dictType="res-apply-status" dictCode={finishApply.statusCode} />
            </Col>
            <Col label="实际完成时间">{topic.finishDay}</Col>
            <Col label="结题证书编号">{topic.topicCert}</Col>
          </Descriptions>
        )}
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
          <Col label="其他成员" span={2}>
            <div>
              {topic.members.map(m => (
                <Tag key={m.id}>
                  {m.name} - {m.idCard}
                </Tag>
              ))}
            </div>
          </Col>
        </Descriptions>
      </div>
    );
  }
}
