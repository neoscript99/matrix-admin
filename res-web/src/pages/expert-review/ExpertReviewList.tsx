import React from 'react';
import { Avatar, Tag, Card, Result, InputNumber, message } from 'antd';
import {
  achieveExpertScoreService,
  attachmentService,
  paperService,
  reviewRoundExpertService,
  topicAchieveService,
} from '../../services';
import {
  EntityList,
  EntityListProps,
  EntityColumnProps,
  EntityListState,
  UploadWrap,
  ListOptions,
  Entity,
  NumberUtil,
  TableUtil,
} from 'oo-rest-mobx';
interface S extends EntityListState {
  scoreList: any[];
  roundExpert: Entity;
}
interface UploadProps {
  file: any;
}
const Upload = (props: UploadProps) => (
  <UploadWrap value={[props.file]} disabled={true} attachmentService={attachmentService} />
);
const columns: EntityColumnProps[] = [
  TableUtil.commonColumns.index,
  { title: '标题', dataIndex: 'name' },
  { title: '负责人', dataIndex: 'personInCharge.name' },
  { title: '单位', dataIndex: 'dept.name' },
];
const paperColumns: EntityColumnProps[] = [
  {
    title: '正文',
    render: (text, item) => <Upload file={item.paperFile} />,
  },
];
const topicColumns: EntityColumnProps[] = [
  {
    title: '成果简述',
    render: (text, item) => <Upload file={item.summary} />,
  },
  {
    title: '主报告盲评文本',
    render: (text, item) => <Upload file={item.mainReport} />,
  },
];
export class ExpertReviewList extends EntityList<EntityListProps, S> {
  constructor(a, b) {
    super(a, b);
    this.tableProps.pagination = { hideOnSinglePage: true, pageSize: 999 };
    this.tableProps.rowSelection = undefined;
  }
  componentDidMount() {
    reviewRoundExpertService.listByExpert().then(() => this.queryData());
  }

  render() {
    const { dataList, scoreList, roundExpert } = this.state;
    if (!dataList || dataList.length === 0) return <Result title="当前无评分" />;
    const scoredNumber = scoreList.length;
    const sum = scoreList.reduce((sum, item) => sum + item.score, 0);
    const average = scoredNumber > 0 ? NumberUtil.round(sum / scoredNumber, 2) : 0;
    const { round } = roundExpert;
    return (
      <React.Fragment>
        <Card>
          <Card.Meta
            avatar={
              <Avatar size="large" style={{ backgroundColor: '#1e88e5' }}>
                {round.name}
              </Avatar>
            }
            title={
              <div>
                {round.plan.planName}(评分截止日期：{round.endDay})
              </div>
            }
            description={
              <span>
                总数：<Tag>{dataList.length}</Tag>，已评分数量：<Tag>{scoredNumber}</Tag>，已评平均分：
                <Tag>{average}</Tag>
              </span>
            }
          />
        </Card>
        {super.render()}
      </React.Fragment>
    );
  }

  /**
   * 屏蔽原query
   */
  async queryData() {
    const roundExpert = reviewRoundExpertService.store.currentItem;
    if (!roundExpert?.id) return;
    const scoreOptions: ListOptions = { criteria: { eq: [['roundExpert.id', roundExpert.id]] } };
    const scoreList = (await achieveExpertScoreService.listAll(scoreOptions)).results;
    const achieveOptions: ListOptions = { criteria: { eq: [['reviewPlan.id', roundExpert.round.plan.id]] } };
    const dataList = (await this.domainService.listAll(achieveOptions)).results;
    dataList.forEach(item => (item.score = scoreList.find(s => s.achieveId === item.id)?.score));
    this.setState({ scoreList, dataList: this.sortList(dataList), roundExpert });
  }

  sortList(dataList: any[]) {
    return dataList.sort((a, b) => (b.score || 0) - (a.score || 0));
  }

  get columns(): EntityColumnProps[] {
    return [
      ...columns,
      ...(this.isTopic ? topicColumns : paperColumns),
      {
        title: '评分',
        render: (text, item) => (
          <InputNumber
            placeholder="总分100"
            value={item.score}
            min={0}
            max={100}
            onChange={this.updateScore.bind(this, item.id as string)}
          />
        ),
      },
    ];
  }
  showing = false;
  async updateScore(achieveId: string, score: number | undefined) {
    if (!score) return;
    const { roundExpert, dataList, scoreList } = this.state;
    const oldScore = scoreList.find(s => s.achieveId === achieveId);
    const service = achieveExpertScoreService;
    if (oldScore) {
      oldScore.score = score;
      await service.save(oldScore);
    } else {
      const newScore = await service.save({ achieve: { id: achieveId }, score, roundExpert: { id: roundExpert.id } });
      scoreList.push(newScore);
    }
    const achieve = dataList.find(d => d.id === achieveId);
    if (achieve) achieve.score = score;
    if (!this.showing) {
      this.showing = true;
      message.info('评分已更新', 2, () => (this.showing = false));
    }

    this.setState({ dataList: this.sortList(dataList), scoreList });
  }

  get domainService() {
    return this.isTopic ? topicAchieveService : paperService;
  }

  get isTopic() {
    const { roundExpert } = this.state;
    return roundExpert && roundExpert?.round.plan.reviewTypeCode === 'topic';
  }

  get name() {
    return '专家评分';
  }
}
