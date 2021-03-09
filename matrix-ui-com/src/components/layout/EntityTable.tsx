import React from 'react';
import { Table } from 'antd';
import { TableProps } from 'antd/lib/table';
import { DomainService, Entity } from 'matrix-ui-service';
import { useServiceStore } from '../../utils';
export interface EntityTableProps extends TableProps<Entity> {
  domainService?: DomainService;
  listName: string;
}

/**
 * 监控store变化
 * 目前属于临时改造EntityList，未来全面转为React hook + ant design pro
 * @param props
 * @constructor
 */
export function EntityTable(props: EntityTableProps) {
  const { domainService, listName, ...rest } = props;
  const store = useServiceStore(domainService);
  return <Table dataSource={store[listName]} {...rest}></Table>;
}
