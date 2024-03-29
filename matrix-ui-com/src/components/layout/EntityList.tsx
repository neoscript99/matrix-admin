import React, { Component } from 'react';
import { ColumnProps, TableProps } from 'antd/lib/table';
import { CloseOutlined } from '@ant-design/icons';
import { Button, message, Modal, Result, Table, Tag } from 'antd';
import { Consts, TableUtil } from '../../utils';
import { EntityForm, EntityFormProps } from './EntityForm';
import { OperatorBar, OperatorBarProps } from './OperatorBar';
import { SearchBar, SearchFromBarProps } from './SearchBar';
import { SearchForm, SearchFormProps } from './SearchForm';
import { DomainService, Entity, ListOptions, ListResult, LangUtil } from 'matrix-ui-service';
import { CheckboxField, InputField, SelectField } from '../../ant-design-field';
import { RouteChildrenProps } from 'react-router';
import { EntityExporterPop, EntityExporterProps } from './EntityExporter';
import { TablePaginationConfig } from 'antd/lib/table/interface';
import { EntityTable } from './EntityTable';

export interface OperatorSwitch {
  update?: boolean;
  create?: boolean;
  delete?: boolean;
  view?: boolean;
  exportSelected?: boolean;
  exportAll?: boolean;
}

export interface EntityListProps extends Partial<RouteChildrenProps> {
  name?: string;
  operatorVisible?: OperatorSwitch;

  hasNestColumnName?: boolean;
  // 自适应处理 searchBarOnTop?: boolean;
}

export interface EntityListState {
  selectedRowKeys?: any[];
  formProps?: EntityFormProps;
  searchParam?: any;
  exportList?: Entity[];
  tableLoading?: boolean;
}

export interface EntityListTableProps extends TableProps<Entity> {
  pagination: TablePaginationConfig;
}

export interface EntityColumnProps<P = Entity> extends ColumnProps<P> {
  fieldType?: typeof InputField | typeof SelectField | typeof CheckboxField;
  valueTransfer?: (value: any) => any;
  renderText?: (text: any, record?: P, index?: number) => string;
  //导出到excel的宽度（英文字符数）
  cellWidth?: number;
  /**
   * react-data-export/types/index.d.ts类型有问题，以文档为准
   * https://www.npmjs.com/package/react-data-export
   */
  cellStyle?: any;
}
/**
 * EntityList不做分页，获取所有数据
 * 但后台max还是限制了1000，所以大于这个记录数不能用EntityList，改用EntityPageList
 * 这里的pagination配置的是前台分页信息
 */
export abstract class EntityList<
  P extends EntityListProps = EntityListProps,
  S extends EntityListState = EntityListState
> extends Component<P, S> {
  listName = 'allList';
  state = {} as S;
  tableProps: EntityListTableProps = {
    rowKey: 'id',
    rowSelection: {
      onChange: this.changeSelectRows.bind(this),
      //hideDefaultSelections: true,
    },
    bordered: true,
    pagination: {
      pageSize: 10,
      current: 1,
      showSizeChanger: true,
      showQuickJumper: true,
      showTotal: (total) => <Tag color="blue">总记录数：{total}</Tag>,
      onChange: this.pageChange.bind(this),
      onShowSizeChange: this.pageSizeChange.bind(this),
    },
  };
  uuid = new Date().toISOString();
  entityFormWrapper?: React.ComponentType<Omit<EntityFormProps, 'form'>>;
  render() {
    const { formProps, tableLoading } = this.state;
    const barCss: React.CSSProperties = {
      display: 'flex',
      justifyContent: 'space-between',
      flexWrap: 'wrap',
      marginBottom: 5,
    };
    return (
      <div>
        {this.getEntityFormPop(formProps)}
        {this.getExportPop()}
        <div style={barCss}>
          <OperatorBar
            onCreate={this.handleCreate.bind(this)}
            onUpdate={this.handleUpdate.bind(this)}
            onDelete={this.handleDelete.bind(this)}
            onView={this.handleView.bind(this)}
            onExportSelected={this.handleExportSelected.bind(this)}
            onExportAll={this.handleExportAll.bind(this)}
            operatorVisible={this.getOperatorVisible()}
            operatorEnable={this.getOperatorEnable()}
            {...this.getOperatorProps()}
          />
          {this.getSearchFormBar()}
        </div>
        <EntityTable
          domainService={this.domainService}
          listName={this.listName}
          columns={this.getNamePathColumns()}
          {...this.tableProps}
          loading={tableLoading}
        />
      </div>
    );
  }

  abstract get domainService(): DomainService;

  abstract get columns(): EntityColumnProps[];

  /**
   * 如果包含.号分隔的字段名，在antd4中需要转化为数组
   * 嵌套 dataIndex 支持从 'xxx.yyy' 改成 ['xxx', 'yyy']
   * @see https://ant.design/docs/react/migration-v4-cn
   */
  getNamePathColumns(): EntityColumnProps[] {
    return this.props.hasNestColumnName
      ? this.columns.map((col) => {
          let { dataIndex } = col;
          if (typeof col.dataIndex === 'string' && col.dataIndex.indexOf('.') > -1)
            dataIndex = col.dataIndex.split('.');
          return { ...col, dataIndex };
        })
      : this.columns;
  }

  get exportColumns(): EntityColumnProps[] {
    return this.columns;
  }

  query(): Promise<ListResult> {
    console.debug(`${this.className}(${this.toString()}).query`);
    const p = this.domainService.listAll(this.getQueryParam());
    this.updateTableProps(p);
    return p;
  }

  /**
   * EntityList切换页面到第一页时不会触发查询，所以需要手工调用query
   * EntityPageList切换页面后会触发查询
   * @param toPageOne 是否回到首页
   */
  refresh(toPageOne?: boolean) {
    if (toPageOne) this.pageChange(1);
    this.query();
  }

  updateTableProps(promise: Promise<any>): void {
    this.setState({ tableLoading: true });
    promise
      .then((data: ListResult) => {
        this.tableProps.pagination.total = data.totalCount;
        this.updateStorePageInfo();
        this.setState({ tableLoading: false });
        return data;
      })
      .catch((e) => {
        this.setState({ tableLoading: false });
        message.info(`查询出错：${e}`);
        throw e;
      });
    /*
    chrome对finally的支持暂时还不稳定
    .finally(() => {
      this.tableProps.loading = false
      this.forceUpdate()
    })
    */
  }

  componentDidMount(): void {
    this.setState({ selectedRowKeys: [] });
    const { store } = this.domainService;
    if (store.needRefresh) {
      this.query();
      store.needRefresh = false;
    } else {
      this.restoreState();
    }
  }

  restoreState() {
    const { pageInfo } = this.domainService.store;
    Object.assign(this.tableProps.pagination, TableUtil.fromPageInfo(pageInfo));
  }

  /**
   * 不用get property是因为无法继承
   */
  getQueryParam(): ListOptions {
    return {
      criteria: {},
      orders: [['dateCreated', 'desc']],
    };
  }

  toString() {
    return this.uuid;
  }

  get className() {
    return LangUtil.getClassName(this);
  }

  pageChange(page: number): void {
    this.tableProps.pagination.current = page;
    this.updateStorePageInfo();
  }

  pageSizeChange(current: number, size: number): void {
    this.tableProps.pagination.pageSize = size;
    this.tableProps.pagination.current = 1;
    this.updateStorePageInfo();
  }

  updateStorePageInfo() {
    this.domainService.syncPageInfo(TableUtil.toPageInfo(this.tableProps.pagination));
    //目前几种情况下，更新store.pageInfo后，当前页面的选择记录j 都应该清空
    this.changeSelectRows([], []);
  }

  changeSelectRows(selectedRowKeys: React.Key[], selectedRows: Entity[]) {
    if (this.tableProps.rowSelection) this.tableProps.rowSelection.selectedRowKeys = selectedRowKeys;
    this.setState({ selectedRowKeys });
  }

  /**
   * 不用lambda表达式是因为无法被子类继承重载
   */
  handleCreate() {
    this.setState({
      formProps: this.genFormProps('新增'),
    });
  }

  /**
   * 新增时的初始值
   */
  getInitItem(): Entity | undefined {
    return undefined;
  }

  handleUpdate() {
    this.doUpdate(this.getSelectItem());
  }

  /**
   * 可用于事件处理参数绑定
   * @param item
   */
  doUpdate(item) {
    if (item)
      this.setState({
        formProps: this.genFormProps('修改', item),
      });
  }
  handleDelete(): Promise<any> {
    return this.doDelete(this.state.selectedRowKeys);
  }

  /**
   * 可用于事件处理参数绑定
   * @param ids
   */
  doDelete(ids): Promise<any> {
    if (ids)
      return this.domainService
        .deleteByIds(ids as any[])
        .then(() => {
          message.success('删除成功');
          this.query();
        })
        .catch((err) => this.handleDeleteError(err));
    else return Promise.reject('请选择记录');
  }
  handleDeleteError(err, msg?: string) {
    console.error('EntityList.handleDeleteError: ', err);
    message.error(msg || '删除失败，可能存在依赖信息无法删除');
  }
  handleView() {
    const item = this.getSelectItem();
    if (item) {
      const formProps = this.genFormProps('查看', item, { readonly: true });
      this.setState({ formProps });
    }
  }
  /**
   * 不用get property是因为无法继承
   */
  getSelectItem(): Entity | undefined {
    const { selectedRowKeys } = this.state;
    if (!selectedRowKeys || !this.dataList) return undefined;
    const id = selectedRowKeys[0];
    return this.dataList.find((v) => v.id === id);
  }

  getSelectItems() {
    const { selectedRowKeys } = this.state;
    if (!selectedRowKeys || !this.dataList) return [];
    return this.dataList.filter((v) => selectedRowKeys.includes(v.id));
  }

  handleFormSuccess(item: Entity) {
    this.setState({ formProps: undefined });
    this.refresh();
  }

  handleFormCancel() {
    this.setState({ formProps: undefined });
  }

  genFormProps(action: string, item?: Entity, exProps?: Partial<EntityFormProps>): EntityFormProps {
    return {
      modalProps: { title: `${action}${this.props.name}`, okText: action },
      domainService: this.domainService,
      onSuccess: this.handleFormSuccess.bind(this),
      onCancel: this.handleFormCancel.bind(this),
      columns: this.columns,
      inputItem: item || this.getInitItem(),
      ...exProps,
    };
  }

  handleSearch(searchParam: any): void {
    this.domainService.store.searchParam = searchParam;
    this.tableProps.pagination.current = 1;
    this.updateStorePageInfo();
    this.query();
  }
  getEntityForm(): React.ComponentType<any> {
    return EntityForm;
  }
  getEntityFormPop(formProps?: EntityFormProps) {
    if (formProps) {
      if (!this.entityFormWrapper) this.entityFormWrapper = EntityForm.formWrapper(this.getEntityForm());
      const FormComponent = this.entityFormWrapper;
      return <FormComponent {...formProps} />;
    } else return null;
  }

  getSearchForm(): typeof SearchForm | null {
    return null;
  }
  searchFormRender(props: SearchFormProps): React.ReactNode {
    const SearchForm = this.getSearchForm();
    return SearchForm && <SearchForm {...props} />;
  }
  getSearchFormBar(props?: Partial<SearchFromBarProps>): React.ReactNode {
    return (
      <SearchBar
        onSearch={this.handleSearch.bind(this)}
        formRender={this.searchFormRender.bind(this)}
        searchParam={this.domainService.store.searchParam}
        {...props}
      />
    );
  }
  getOperatorEnable(): OperatorSwitch {
    const { selectedRowKeys } = this.state;
    const selectedNum = selectedRowKeys ? selectedRowKeys.length : 0;
    const total = this.tableProps.pagination.total;
    return {
      update: selectedNum === 1,
      view: selectedNum === 1,
      delete: selectedNum > 0,
      exportSelected: selectedNum > 0,
      exportAll: !!total && total > 0,
    };
  }

  /**
   * 可以通过props传入，也可以重载本方法
   */
  getOperatorVisible(): OperatorSwitch {
    return this.props.operatorVisible || Consts.allOperator;
  }

  getOperatorProps(): Partial<OperatorBarProps> | undefined {
    return undefined;
  }

  get store() {
    return this.domainService.store;
  }
  get dataList() {
    return this.store[this.listName];
  }

  handleExportSelected() {
    this.setState({ exportList: this.getSelectItems() });
  }

  handleExportAll() {
    this.setState({ exportList: this.dataList });
  }

  getExportProps(): EntityExporterProps {
    return {
      dataSource: this.state.exportList,
      columns: this.exportColumns,
      filename: `${this.props.name}导出`,
    };
  }
  getExportPop() {
    const { exportList } = this.state;
    const cancel = () => this.setState({ exportList: undefined });
    return <EntityExporterPop onCancel={cancel} {...this.getExportProps()} />;
  }
}
