import React from 'react';
import { Avatar, Tag, Card, Result, Button } from 'antd';
import { achieveReviewScoreService, paperService, reviewRoundExpertService, topicAchieveService } from '../../services';
import { EntityList, EntityListProps, EntityColumnProps, EntityListState } from 'oo-rest-mobx';
interface S extends EntityListState {
  scoredNumber: number;
  average: number;
}
const columns: EntityColumnProps[] = [
  { title: '成果名称', dataIndex: 'name' },
  { title: '成果负责人', dataIndex: 'personInCharge.name' },
];
export class ExpertReviewList extends EntityList<EntityListProps, S> {
  constructor(a, b) {
    super(a, b);
    this.tableProps.pagination = { hideOnSinglePage: true, pageSize: 999 };
    this.tableProps.rowSelection = undefined;
  }
  componentDidMount() {
    reviewRoundExpertService.listByExpert().then(list => {
      list.length > 0 && this.query();
    });
  }

  render() {
    const { round } = reviewRoundExpertService.store.currentItem;
    if (!round) return <Result title="当前无评分" />;
    const scoreList = achieveReviewScoreService.store.allList;
    const scoredNumber = scoreList.length;
    const average = scoredNumber > 0 ? scoreList.reduce((sum, item) => sum + item.score, 0) / scoredNumber : 0;
    const { dataList } = this.state;
    return (
      <React.Fragment>
        <Card>
          <Card.Meta
            avatar={
              <Avatar size="large" style={{ backgroundColor: '#1e88e5' }}>
                {round.name}
              </Avatar>
            }
            title={round.plan.planName}
            description={
              <span>
                总数：<Tag>{dataList.length}</Tag>，已评分数量：<Tag>{scoredNumber}</Tag>，平均分：<Tag>{average}</Tag>
              </span>
            }
          />
        </Card>
        {super.render()}
      </React.Fragment>
    );
  }

  query() {
    const { id } = reviewRoundExpertService.store.currentItem;
    achieveReviewScoreService.listAll({ criteria: { eq: [['reviewRoundExpertId', id]] } });
    return super.query();
  }

  getQueryParam() {
    const { round } = reviewRoundExpertService.store.currentItem;
    const param = super.getQueryParam();
    param.criteria = { eq: [['reviewPlan.id', round.plan.id]] };
    return param;
  }

  get columns(): EntityColumnProps[] {
    return columns;
  }

  get domainService() {
    const { round } = reviewRoundExpertService.store.currentItem;
    return round && round.plan.reviewTypeCode === 'topic' ? topicAchieveService : paperService;
  }

  get name() {
    return '专家评分';
  }
}
