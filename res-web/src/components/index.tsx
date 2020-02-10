import React from 'react';
import { DictView } from 'oo-rest-mobx';
import { dictService } from '../services';
import { Icon } from 'antd';

export const DictViewer = DictView.build(dictService);
export const InfoIcon = () => <Icon type="info-circle" style={{ margin: 2 }} theme="twoTone" />;

export * from './ApproveDropdown';
export * from './PlanCard';
