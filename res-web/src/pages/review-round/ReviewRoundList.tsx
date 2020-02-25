import React from 'react';
import {
  EntityList,
  EntityColumnProps,
  DomainService,
  ListOptions,
  Entity,
  EntityListProps,
  Consts,
} from 'oo-rest-mobx';
import { dictService, reviewRoundService } from '../../services';
import { ReviewRoundForm, ReviewRoundFormProps } from './ReviewRoundForm';
import { Button, Popconfirm, Table } from 'antd';
const columns: EntityColumnProps[] = [
  { title: '评分轮次', dataIndex: 'name' },
  { title: '截止日期', dataIndex: 'endDay' },
  { title: '等级数', dataIndex: 'grades' },
  {
    title: '平均分算法',
    dataIndex: 'avgAlgorithmCode',
    render: dictService.dictRender.bind(null, 'res-avg-algorithm'),
  },
];
const { tdButtonProps, twoColModalProps } = Consts.commonProps;
interface P extends EntityListProps {
  plan: Entity;
  showForm?: boolean;
  onFormClose: () => void;
}
export class ReviewRoundList extends EntityList<P> {
  constructor(props, context) {
    super(props, context);
    this.tableProps.pagination = { hideOnSinglePage: true, pageSize: 999 };
    this.tableProps.rowSelection = undefined;
  }

  componentDidMount(): void {
    this.query();
  }
  render() {
    const { showForm } = this.props;
    const { dataList, formProps } = this.state;
    const newProps = formProps || (showForm ? this.genFormProps('新增') : undefined);
    //如果用React.Fragment，table会到第一个，margin会消除，antd功能
    return (
      <div>
        <Table dataSource={dataList} columns={this.columns} {...this.tableProps} />
        {this.getEntityFormPop(newProps)}
      </div>
    );
  }
  genFormProps(action: string, item?: any, exProps?): ReviewRoundFormProps {
    const { dataList } = this.state;
    const props = super.genFormProps(action, item, exProps);
    return {
      ...props,
      modalProps: { ...props.modalProps, ...twoColModalProps },
      parentList: item ? dataList.filter(p => p.dateCreated! < item.dateCreated) : dataList,
    };
  }
  get domainService(): DomainService {
    return reviewRoundService;
  }
  get columns(): EntityColumnProps[] {
    return [
      ...columns,
      {
        title: '操作',
        render: (text, item) => (
          <div>
            <Button {...tdButtonProps} onClick={this.doUpdate.bind(this, item)}>
              修改
            </Button>
            <Popconfirm
              title="确定删除本轮评分的所有信息(包括专家评分记录)?"
              onConfirm={this.doDelete.bind(this, [item.id])}
              okText="确定"
              cancelText="取消"
            >
              <Button {...tdButtonProps}>删除</Button>
            </Popconfirm>
          </div>
        ),
      },
    ];
  }
  getQueryParam(): ListOptions {
    const { plan } = this.props;
    return { criteria: { eq: [['plan.id', plan.id]] } };
  }
  getInitItem() {
    const { plan } = this.props;
    return { plan };
  }
  get name() {
    return '专家评分轮次';
  }
  getEntityForm() {
    return ReviewRoundForm;
  }
  handleFormCancel(): void {
    super.handleFormCancel();
    this.props.onFormClose();
  }
  handleFormSuccess(item: Entity): void {
    super.handleFormSuccess(item);
    this.props.onFormClose();
  }
  handleDeleteError(err, msg?: string) {
    super.handleDeleteError(err, '删除失败，可能存在其它轮次依赖本轮');
  }
}