import React, { CSSProperties } from 'react';
import {
  EntityList,
  EntityColumnProps,
  TableUtil,
  ListOptions,
  Consts,
  EntityFormProps,
  Entity,
  ObjectUtil,
} from 'oo-rest-mobx';
import { achieveRoundResultService, duplicateCheckService, reviewRoundService } from '../../services';
import { Alert, Button, Tag } from 'antd';
import { DuplicateCheckForm, DuplicateCheckFormProps } from './DuplicateCheckForm';
import ReactExport from 'react-data-export';

const ExcelFile = ReactExport.ExcelFile;
const ExcelSheet = ReactExport.ExcelFile.ExcelSheet;
const ExcelColumn = ReactExport.ExcelFile.ExcelColumn;

const columns: EntityColumnProps[] = [
  TableUtil.commonColumns.index,
  { title: '标题', dataIndex: 'achieve.name' },
  { title: '负责人', dataIndex: 'achieve.personInCharge.name' },
  { title: '单位', dataIndex: 'achieve.dept.name' },
  { title: '得分', dataIndex: 'average' },
  {
    title: '打分明细',
    dataIndex: 'scoresJson',
    render: (v, item) => {
      const scores: any[] = JSON.parse(item.scoresJson);
      if (v === 'export') {
        const spt = ',\n';
        const info = scores.map(s => `${s.name}: ${s.score}`).join(spt);
        return item.hasError ? info.concat(spt, item.message) : info;
      }
      return (
        <div className="flex-row" style={{ maxWidth: '16em', margin: -10, justifyContent: 'space-between' }}>
          {scores.map(s => (
            <Tag key={s.name}>
              {s.name}: {s.score}
            </Tag>
          ))}
          {item.hasError && <Alert message={item.message} type="error" style={{ padding: 5, marginTop: 5 }} />}
        </div>
      );
    },
  },
];

const buttonCss: CSSProperties = {
  marginRight: '0.5rem',
};
const { tdButtonProps } = Consts.commonProps;
export class ReviewResultList extends EntityList {
  constructor(a, b) {
    super(a, b);
    this.tableProps.pagination = { hideOnSinglePage: true, pageSize: 999 };
    this.tableProps.rowSelection = undefined;
  }
  componentDidMount(): void {
    this.query();
  }
  render() {
    const { currentItem: round } = reviewRoundService.store;
    const { dataList } = this.state;
    return (
      <React.Fragment>
        <Button onClick={this.goBack} icon="rollback" style={buttonCss} />
        {round.plan && (
          <ExcelFile element={<Button icon="download" style={buttonCss} />} filename={round.plan.planName}>
            <ExcelSheet data={dataList} name={round.name}>
              {this.columns.map(col => (
                <ExcelColumn
                  key={col.title + (col.dataIndex || 'none')}
                  label={col.title}
                  value={item =>
                    col.render
                      ? col.render('export', item, dataList.indexOf(item))
                      : col.dataIndex && ObjectUtil.get(item, col.dataIndex)
                  }
                />
              ))}
            </ExcelSheet>
          </ExcelFile>
        )}
        {super.render()}
      </React.Fragment>
    );
  }
  goBack = () => {
    const { history } = this.props;
    history?.push('/ReviewPlan');
  };

  getQueryParam(): ListOptions {
    const { match } = this.props;
    const roundId = (match?.params as any).roundId;
    if (roundId) return { criteria: { eq: [['round.id', roundId]] }, orders: [['average', 'desc']] };
    else throw '请传入正确的评比轮次';
  }

  get columns(): EntityColumnProps[] {
    return [
      ...columns,
      {
        title: '查重',
        render: (v, item) => {
          const dup = item.achieve.duplicateCheck;
          let info;
          if (dup) {
            info = dup.success ? '通过' : '不通过';
            if (dup.desc) info += `: ${dup.desc}`;
          }
          if (v === 'export') return info;
          return (
            <div className="flex-col" style={{ alignItems: 'center', margin: -10 }}>
              <Button {...tdButtonProps} onClick={this.showForm.bind(this, item)} icon="edit" />
              {dup && (
                <Alert
                  message={info}
                  type={dup.success ? 'success' : 'error'}
                  style={{ maxWidth: '10em', padding: 5 }}
                />
              )}
            </div>
          );
        },
      },
    ];
  }
  showForm(item) {
    this.setState({ formProps: this.genFormProps('查重', item) });
  }
  getEntityForm() {
    return DuplicateCheckForm;
  }

  /**
   * 本页面的对话框只设置查重信息
   */
  genFormProps(title: string, item?: Entity, exProps?: Partial<EntityFormProps>): DuplicateCheckFormProps {
    const props = super.genFormProps(title, item!.achieve.duplicateCheck, { modalProps: { title, okText: '保存' } });
    return { ...props, domainService: duplicateCheckService, achieve: item!.achieve };
  }

  get domainService() {
    return achieveRoundResultService;
  }
}
