import React from 'react';
import { AdminPageProps } from '../';
import { Consts, TableUtil } from '../../../utils';
import { EntityPageList, EntityColumnProps, EntityListState, EntityFormProps, SearchFormProps } from '../../layout';
import { Criteria, ListOptions, UserService, StringUtil } from 'matrix-ui-service';
import { UserForm, UserFormProps } from './UserForm';
import { Entity } from 'matrix-ui-service';
import { Button, message, Popconfirm } from 'antd';
import { UserSearchForm } from './UserSearchForm';
import { DictView } from '../../common';

const { commonColumns } = TableUtil;
export class UserList<
  P extends AdminPageProps = AdminPageProps,
  S extends EntityListState = EntityListState
> extends EntityPageList<P, S> {
  static defaultProps = { name: '用户' };
  get domainService(): UserService {
    return this.props.services.userService;
  }

  get columns(): EntityColumnProps[] {
    return this.getUserColumns();
  }
  getUserColumns(): EntityColumnProps[] {
    const { dictService } = this.props.services;
    const columns: EntityColumnProps[] = [
      { title: '姓名', dataIndex: 'name' },
      { title: '帐号', dataIndex: 'account' },
      { title: '所属机构', dataIndex: ['dept', 'name'] },
      DictView.dictColumn(dictService, '性别', 'sexCode', 'pub_sex'),
      { title: '手机号码', dataIndex: 'cellPhone' },
      commonColumns.enabled,
      ...this.getExtraColumns(),
      commonColumns.lastUpdated,
      { title: '操作', key: 'operator', render: this.opCol.bind(this) },
    ];

    return columns;
  }

  getExtraColumns(): EntityColumnProps[] {
    return [commonColumns.editable];
  }
  opCol(text: string, record: any) {
    return (
      <Popconfirm
        title={`确定重置? `}
        onConfirm={this.resetPassword.bind(this, record)}
        okText="确定"
        cancelText="取消"
      >
        <Button type="link">重置密码</Button>
      </Popconfirm>
    );
  }

  genFormProps(action: string, item?: Entity, exProps?: Partial<EntityFormProps>): UserFormProps {
    const props = super.genFormProps(action, item, exProps);
    const { services } = this.props;
    return { ...props, modalProps: { ...props.modalProps, ...Consts.commonProps.twoColModalProps }, services };
  }
  getInitItem(): Entity {
    return { editable: true };
  }
  resetPassword(user: Entity) {
    this.domainService.resetPassword(user).then(() => message.success('重置成功'));
  }
  getOperatorEnable() {
    const base = super.getOperatorEnable();
    return {
      ...base,
      update: base.update && this.getSelectItem()?.editable,
      delete: base.delete && this.getSelectItems().every((item) => item.editable),
    };
  }
  getEntityForm() {
    return UserForm;
  }
  searchFormRender(props: SearchFormProps): React.ReactNode {
    return <UserSearchForm {...props} deptList={this.props.services.deptService.store.enabledList} />;
  }
  getQueryParam(): ListOptions {
    const param = super.getQueryParam();
    const criteria: Criteria = {};
    const {
      searchParam: { account, name, deptId },
    } = this.domainService.store;
    if (StringUtil.isNotBlank(account)) criteria.ilike = [['account', account + '%']];
    if (StringUtil.isNotBlank(name)) criteria.like = [['name', '%' + name + '%']];
    if (deptId) criteria.dept = { eq: [['id', deptId]] };
    return { ...param, criteria };
  }
}
