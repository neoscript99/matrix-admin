import { ReviewApplyList } from './ReviewApplyList';
import { EntityColumnProps, SimpleSearchForm } from 'oo-rest-mobx';
import { paperService } from '../../services';
import { PaperForm } from './PaperForm';
const columns: EntityColumnProps[] = [
  { title: '论文题目', dataIndex: 'title' },
  { title: '论文作者', dataIndex: 'personInCharge.name' },
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
  getSearchForm() {
    return PaperSearchForm;
  }
}

export class PaperSearchForm extends SimpleSearchForm {
  placeholder = '标题、所属计划';
}
