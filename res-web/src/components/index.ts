import { DictView } from 'oo-rest-mobx';
import { dictService } from '../services';

export * from './ApproveDropdown';
export const DictViewer = DictView.build(dictService);
