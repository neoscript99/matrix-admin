import React from 'react';
import { EntityList } from 'oo-rest-mobx';
import { achieveService } from '../../services';

export class ReviewResultList extends EntityList {
  render() {
    return <div></div>;
  }

  get columns() {
    return [];
  }

  get domainService() {
    return achieveService;
  }
}
