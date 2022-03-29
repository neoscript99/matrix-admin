import { AtImagePicker } from 'taro-ui';
import React, { useCallback, useMemo, useState } from 'react';
import { AtImagePickerProps } from 'taro-ui/types/image-picker';
import { AttachmentEntity, AttachmentService } from 'matrix-ui-service';
import Taro from '@tarojs/taro';

type SuccessCallbackResult = Taro.compressImage.SuccessCallbackResult;

export interface MatrixImagePickerProps {
  attachmentService: AttachmentService;
  onImageChange: (entitys: AttachmentEntity[]) => void;
  pickerProps?: Omit<AtImagePickerProps, 'files' | 'onChange' | 'showAddBtn'>;
}
export const MatrixImagePicker: React.FC<MatrixImagePickerProps> = (props) => {
  const { attachmentService, onImageChange, pickerProps } = props;
  //images不需要状态控制
  const images = useMemo<AttachmentEntity[]>(() => [], []);
  const url = useMemo(() => attachmentService.uploadUrl, []);
  const [files, setFiles] = useState<AtImagePickerProps['files']>([]);
  const showAddBtn = useMemo(() => !pickerProps.count || files.length < pickerProps.count, [files]);
  const chooseFile = useCallback<AtImagePickerProps['onChange']>(
    (newFiles, operationType, index) => {
      setFiles(newFiles);
      if (operationType === 'add') {
        const filePath = newFiles[newFiles.length - 1].url;
        new Promise<SuccessCallbackResult>((resolve) => {
          if (Taro.getEnv() === Taro.ENV_TYPE.WEB) resolve({ tempFilePath: filePath, errMsg: '' });
          else resolve(Taro.compressImage({ src: filePath }));
        }).then((res) => {
          Taro.uploadFile({ url, filePath: res.tempFilePath, name: 'file' }).then((uploadRes) => {
            images.push(JSON.parse(uploadRes.data));
            onImageChange(images);
          });
        });
      } else if (operationType == 'remove') {
        images.splice(index, 1);
        onImageChange(images);
      }
    },
    [images],
  );
  return <AtImagePicker files={files} onChange={chooseFile} {...pickerProps} showAddBtn={showAddBtn} />;
};
