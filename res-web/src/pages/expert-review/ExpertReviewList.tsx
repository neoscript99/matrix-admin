import React from 'react';
import { Card } from 'antd';
import { achieveReviewScoreService, reviewRoundExpertService } from '../../services';
import { Entity, EntityList, EntityListProps, EntityColumnProps, EntityListState } from 'oo-rest-mobx';
interface S extends EntityListState {
  roundList: Entity[];
}
const columns: EntityColumnProps[] = [
  { title: '成果名称', dataIndex: 'achieve.name' },
  { title: '成果负责人', dataIndex: 'achieve.personInCharge.name' },
];
export class ExpertReviewList extends EntityList<EntityListProps, S> {
  async componentDidMount() {
    this.state.roundList = [];
    const dataList = await reviewRoundExpertService.listByExpert();
    this.setState({ roundList: dataList });
  }

  render() {
    return (
      <React.Fragment>
        <Card title="计划"></Card>
        {super.render()}
      </React.Fragment>
    );
  }

  get columns(): EntityColumnProps[] {
    return columns;
  }

  get domainService() {
    return achieveReviewScoreService;
  }
}
