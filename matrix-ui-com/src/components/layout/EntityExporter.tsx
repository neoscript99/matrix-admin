import React from 'react';
import { CloseOutlined, DownloadOutlined } from '@ant-design/icons';
import { Button, Modal, Result, Space } from 'antd';
import { NamePath, ObjectUtil } from 'matrix-ui-service';
import { CSVLink } from 'react-csv';
import { ModalProps } from 'antd/es';

/**
 * 由于ant和pro组件的兼容性问题，没有指定强制类型
 */
export interface EntityExporterColumn {
  title?: any;
  dataIndex?: NamePath;
  //antd可以用render
  render?: (text: any, record: any, index: number, four?: any, five?: any) => any;
  //antd-pro只能用renderText
  renderText?: (text: any, record: any, index: number, four?: any) => any;
}
export interface EntityExporterProps {
  dataSource?: any[];
  columns: EntityExporterColumn[];
  filename: string;
}

/**
 * react-data-export必须依赖xlsx导致编译文件太大，2021-05-14 改用 react-csv
 * react-data-export依赖xlsx，下级项目自行安装
 * react-data-export/types/index.d.ts类型有问题，以文档为准
 * https://www.npmjs.com/package/react-data-export
 */
export const EntityExporter: React.FC<EntityExporterProps> = (props) => {
  const { dataSource, columns, filename } = props;
  const headers = columns.map((col, idx) => ({
    label: col.title || idx,
    key: col.title || idx,
  }));
  if (!dataSource) return null;
  const data = dataSource.map((item, itemIdx) => {
    const row: Record<string, string> = {};
    columns.forEach((col, idx) => {
      let value = col.dataIndex && ObjectUtil.get(item, col.dataIndex);
      if (col.renderText) value = col.renderText(value, item, itemIdx);
      else if (col.render) value = col.render(value, item, itemIdx);
      row[col.title || idx] = value || '';
    });
    return row;
  });
  return (
    <CSVLink data={data} headers={headers} filename={`${filename}.csv`}>
      <Button icon={<DownloadOutlined />} type="primary" disabled={data.length === 0}>
        保存
      </Button>
    </CSVLink>
  );
};

export type EntityExporterPopProps = Pick<ModalProps, 'onCancel'> & EntityExporterProps;
export const EntityExporterPop: React.FC<EntityExporterPopProps> = (props) => {
  const { onCancel, ...rest } = props;
  return (
    <Modal title="导出完成" visible={!!rest.dataSource} footer={null} maskClosable={false} onCancel={onCancel}>
      <Result
        status="success"
        title="导出完成，请下载保存!"
        subTitle={rest.dataSource && `记录数：${rest.dataSource.length}`}
        extra={
          <Space>
            <EntityExporter {...rest} />
            <Button icon={<CloseOutlined />} onClick={onCancel}>
              关闭
            </Button>
          </Space>
        }
      />
    </Modal>
  );
};
