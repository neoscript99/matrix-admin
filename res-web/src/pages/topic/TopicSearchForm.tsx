import React from 'react';
import { SearchForm, InputField, InputNumberField, DeptSelectField } from 'oo-rest-mobx';
import { Form, Tooltip } from 'antd';
import { deptService, resUserService } from '../../services';

export class TopicSearchForm extends SearchForm {
  render() {
    const isMainManager = resUserService.isMainManager();
    const { form } = this.props;
    return (
      <Form layout="inline" style={{ display: 'flex', justifyContent: 'flex-end', width: '100%' }}>
        <InputNumberField
          fieldId="planYear"
          style={{ width: '6em' }}
          placeholder="立项年度"
          onKeyDown={this.searchOnEnter.bind(this)}
          formUtils={form}
        />
        {isMainManager && (
          <DeptSelectField
            fieldId="deptId"
            dataSource={deptService.store.enabledList}
            formItemProps={{ label: '' }}
            formUtils={form}
            style={{ width: '15em' }}
            placeholder="选择单位"
          />
        )}
        <InputField
          fieldId="topicName"
          style={{ width: '8em' }}
          placeholder="课题名称(*..*)"
          onKeyDown={this.searchOnEnter.bind(this)}
          formUtils={form}
        />
        <InputField
          fieldId="initialCode"
          style={{ width: '8em' }}
          placeholder="立项编号(..*)"
          onKeyDown={this.searchOnEnter.bind(this)}
          formUtils={form}
        />
      </Form>
    );
  }
}
