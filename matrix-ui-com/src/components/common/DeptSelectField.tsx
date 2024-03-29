import React from 'react';
import { SelectFieldProps, SelectField } from '../../ant-design-field';
import { PickPartial } from 'matrix-ui-service';

export class DeptSelectField extends React.Component<PickPartial<SelectFieldProps, 'valueProp'>> {
  render() {
    const { formItemProps, ...props } = this.props;
    const fiProps = { label: '单位', ...formItemProps };
    return (
      <SelectField
        formItemProps={fiProps}
        valueProp="id"
        labelProp="name"
        placeholder="---选择单位---"
        showSearch={true}
        {...props}
      />
    );
  }
}
