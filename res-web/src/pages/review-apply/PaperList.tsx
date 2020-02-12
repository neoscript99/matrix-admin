import { ReviewApplyList } from './ReviewApplyList';
import { EntityColumnProps, DomainService } from 'oo-rest-mobx';
import { dictService, paperService } from '../../services';
import { PaperForm } from './PaperForm';
const columns: EntityColumnProps[] = [
  { title: '论文题目', dataIndex: 'title' },
  { title: '论文作者', dataIndex: 'author.name' },
  ...ReviewApplyList.planColumns,
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

  getEntityForm() {
    return PaperForm;
  }

  get name() {
    return '论文';
  }
}
