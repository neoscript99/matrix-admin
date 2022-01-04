import React, { useEffect, useMemo, useState } from 'react';
import { Table } from 'antd';
import { StyleUtil, TableUtil } from '../../../utils';
import { ApplyLogService, DictService } from 'matrix-ui-service';
const LogInfo = StyleUtil.hiddenText(10);

export interface ApplyLogListProps {
  applyId: string;
  applyLogService: ApplyLogService;
  dictService: DictService;
  applyDictType: string;
}

export function ApplyLogList({ applyId, applyDictType, applyLogService, dictService }: ApplyLogListProps) {
  //同一个界面可以展示多个日志，不用公告store
  // const store = useServiceStore(applyLogService);
  const [logList, setLogList] = useState<any[]>();
  //相同apply会重复打开，每次都需要执行查询
  useEffect(() => {
    applyLogService.listAll({ criteria: { eq: [['apply.id', applyId]] } }).then((res) => setLogList(res.results));
  }, [applyId]);
  const columns = useMemo(
    () => [
      TableUtil.commonColumns.index,
      { title: '用户', dataIndex: ['operator', 'name'] },
      TableUtil.commonColumns.dateCreated,
      {
        title: '状态',
        dataIndex: 'fromStatusCode',
        render: (text, item) =>
          dictService.getName(applyDictType, text) + '->' + dictService.getName(applyDictType, item.toStatusCode),
      },
      { title: '备注', dataIndex: 'info', render: (text) => <LogInfo>{text}</LogInfo> },
    ],
    [applyDictType],
  );
  return <Table pagination={false} columns={columns} dataSource={logList} size="small" rowKey="id" />;
}
