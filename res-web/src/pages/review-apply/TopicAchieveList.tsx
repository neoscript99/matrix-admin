import { ReviewApplyList } from './ReviewApplyList';
import { EntityColumnProps, DomainService } from 'oo-rest-mobx';
import { topicAchieveService } from '../../services';
import { TopicAchieveForm } from './TopicAchieveForm';
const columns: EntityColumnProps[] = [
  { title: '成果名称', dataIndex: 'achieveName' },
  { title: '成果负责人', dataIndex: 'personInCharge.name' },
  ...ReviewApplyList.planColumns,
];

export class TopicAchieveList extends ReviewApplyList {
  get columns(): EntityColumnProps[] {
    return columns;
  }

  get domainService(): DomainService {
    return topicAchieveService;
  }

  get reviewTypeCode(): string {
    return 'topic';
  }
  get name() {
    return '课题成果';
  }

  getEntityForm() {
    return TopicAchieveForm;
  }
}
