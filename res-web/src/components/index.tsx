import React from 'react';
import { DictView } from 'oo-rest-mobx';
import { dictService } from '../services';

export const DictViewer = DictView.build(dictService);

export * from './ApproveDropdown';
export * from './PlanCard';
export * from './DeptUserForm';
