import { ReviewApplyList } from './ReviewApplyList';
import { EntityColumnProps, DomainService } from 'oo-rest-mobx';
import { topicAchieveService } from '../../services';
const columns: EntityColumnProps[] = [];

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
}
