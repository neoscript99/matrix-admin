import React from 'react';
import { Button, Form, Radio, Input, Space, Card, Col, Row } from 'antd';
import { AdminServices, ApplyEntity } from 'matrix-ui-service';
import { ApplyLogList } from './ApplyLogList';

const { TextArea } = Input;

export interface ApplyFormAction {
  value: string | number | boolean;
  label: React.ReactNode;
  info?: string;
}
export interface ApplyFormResult {
  info: string;
  action: string | number;
}
export interface ApplyFormProps {
  apply: ApplyEntity;
  applyDictType: string;
  adminServices: AdminServices;
  ownerRender?: React.ReactNode;
  onSubmit: (res: ApplyFormResult) => void;
  onCancel: () => void;
  actions: ApplyFormAction[];
}
export function ApplyForm(props: ApplyFormProps) {
  const { apply, ownerRender, adminServices, applyDictType, onSubmit, onCancel, actions } = props;
  const { applyLogService, dictService } = adminServices;
  return (
    <>
      {ownerRender}
      <Row gutter={8}>
        <Col span={14}>
          <Card title="审批记录" size="small" style={{ height: '100%' }}>
            <ApplyLogList
              dictService={dictService}
              applyId={apply.id}
              applyLogService={applyLogService}
              applyDictType={applyDictType}
            />
          </Card>
        </Col>
        <Col span={10}>
          <Card title="审批" size="small" style={{ height: '100%' }}>
            <Form onFinish={onSubmit}>
              <Form.Item name="action" label="结果" required={true}>
                <Radio.Group options={actions} />
              </Form.Item>
              <Form.Item dependencies={['action']}>
                {(form) => {
                  const item = form.getFieldsValue();
                  if (item.action) {
                    const map = actions.find((a) => a.value === item.action);
                    map && form.setFieldsValue({ info: map.info });
                  }
                  return (
                    <Form.Item name="info" label="备注" required={true}>
                      <TextArea maxLength={255} size="large" />
                    </Form.Item>
                  );
                }}
              </Form.Item>
              <Form.Item>
                <Space style={{ display: 'flex', justifyContent: 'center' }}>
                  <Button onClick={onCancel}>取消</Button>
                  <Button type="primary" htmlType="submit">
                    提交
                  </Button>
                </Space>
              </Form.Item>
            </Form>
          </Card>
        </Col>
      </Row>
    </>
  );
}
ApplyForm.defaultProps = {
  actions: [
    { value: 'approve', label: '批准', info: '同意/通过' },
    { value: 'sendBack', label: '退回修改', info: '请修改如下问题：' },
    { value: 'reject', label: '驳回', info: '流程终止，原因说明：' },
  ],
};
