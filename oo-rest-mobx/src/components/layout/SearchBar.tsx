import React, { Component, CSSProperties } from 'react';
import { Button } from 'antd';
import { SearchForm, SearchFormProps } from './SearchForm';
import { FormComponentProps } from 'antd/lib/form';
import { ReactUtil } from '../../utils/ReactUtil';

export interface SearchFromBarProps extends FormComponentProps {
  onSearch: (searchParam: any) => void;
  formRender: (props: SearchFormProps) => React.ReactNode;
  searchParam: any;
  style?: CSSProperties;
}

const buttonCss: CSSProperties = {
  marginLeft: '0.5rem',
};

class SearchFromBar extends Component<SearchFromBarProps> {
  render() {
    const { formRender, form, style } = this.props;
    const formNode = formRender({ form, onChange: this.handleSearch.bind(this) });
    return (
      formNode && (
        <div style={{ display: 'flex', alignItems: 'center', ...style }}>
          {formNode}
          <Button icon="search" type="primary" style={buttonCss} title="查询" onClick={this.handleSearch.bind(this)} />
          <Button icon="delete" style={buttonCss} title="重置" onClick={this.handleReset.bind(this)} />
        </div>
      )
    );
  }

  handleSearch() {
    const { form, onSearch } = this.props;
    form.validateFields((err, searchParam) => err || onSearch(searchParam));
  }

  handleReset() {
    const { form, onSearch } = this.props;
    form.resetFields();
    onSearch({});
  }
}

export const SearchBar = ReactUtil.formWrapper(SearchFromBar, 'searchParam');
