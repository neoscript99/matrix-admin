import React from 'react';
import { SearchForm, InputField, InputNumberField } from 'oo-rest-mobx';
import { Form } from 'antd';
import { resUserService } from '../../services';

export class TopicSearchForm extends SearchForm {
  render() {
    const isMainManager = resUserService.isMainManager();
    const { form } = this.props;
    return (
      <Form layout="inline">
        <InputNumberField
          fieldId="planYear"
          style={{ width: '6em' }}
          placeholder="立项年度"
          onKeyDown={this.searchOnEnter.bind(this)}
          formUtils={form}
        />
        {isMainManager && (
          <InputField
            fieldId="deptName"
            style={{ width: '8em' }}
            placeholder="单位名称(*..*)"
            onKeyDown={this.searchOnEnter.bind(this)}
            formUtils={form}
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
