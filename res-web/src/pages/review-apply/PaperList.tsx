import { ReviewApplyList } from './ReviewApplyList';
import { EntityColumnProps, DomainService } from 'oo-rest-mobx';
import { dictService, paperService } from '../../services';
const columns: EntityColumnProps[] = [
  { title: '论文题目', dataIndex: 'title' },
  { title: '论文作者', dataIndex: 'author.name' },
  { title: '所属计划', dataIndex: 'reviewPlan.planName' },
  { title: '论文状态', dataIndex: 'reviewStatusCode', render: dictService.dictRender.bind(null, 'res-review-status') },
];

export class PaperList extends ReviewApplyList {
  get columns(): EntityColumnProps[] {
    return columns;
  }

  get domainService() {
    return paperService;
  }

  get reviewTypeCode(): string {
    return 'paper';
  }
}
