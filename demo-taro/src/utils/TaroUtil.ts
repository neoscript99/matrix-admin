import Taro from '@tarojs/taro';
import { config } from './index';
import { AttachmentEntity } from 'matrix-ui-service';

export interface ChooseOption {
  type?: 'image' | 'video' | 'file';
  //微信聊天记录或手机存储
  source?: 'album' | 'message' | 'camera';
  count?: number;
}

/** 图片的本地临时文件列表 */
interface ImageFile {
  /** 本地临时文件路径 */
  path: string;
  /** 本地临时文件大小，单位 B */
  size: number;
}
export class TaroUtil {
  static goBack() {
    Taro.navigateBack({
      delta: 1, // 返回上一级页面。
    });
  }

  /**
   * @param source 微信聊天记录或手机存储
   * @param count
   */
  static async chooseUploadImage(option: ChooseOption) {
    const type = 'image';
    let src: string;
    const { source = 'message', count = 1 } = option;
    const tempFiles: ImageFile[] = await (source === 'message'
      ? Taro.chooseMessageFile({ count, type }).then((res) => res.tempFiles)
      : Taro.chooseImage({ count, sizeType: ['compressed'], sourceType: [source] }).then((res) => res.tempFiles));
    const attaches: AttachmentEntity[] = [];
    //逐个压缩并上传服务器
    console.log(tempFiles, 'tempFiles');
    for (const tempFile of tempFiles) {
      console.log('Image: ', tempFile);
      src = tempFile.path;
      //大于200K的图片，进行压缩，这个参数可以根据不用业务进行调整
      //png文件除外，背景透明的时候会变黑
      if (tempFile.size > 500 * 1024 && !src.endsWith('png')) {
        const quality = 90;
        const zip = await Taro.compressImage({ src, quality });
        console.log('Taro.compressImage result: ', zip);
        //微信开发工具会导致后缀为空，真机没有问题，实际应该是jpg
        src = zip.tempFilePath;
      }
      attaches.push(await TaroUtil.matrixUpload(src));
    }
    return { attaches, src };
  }

  static matrixUpload(filePath: string): Promise<AttachmentEntity> {
    return new Promise((resolve) => {
      Taro.uploadFile({
        url: config.uploadUrl,
        filePath,
        name: 'file',
        success: (res) => resolve(JSON.parse(res.data) as AttachmentEntity),
      });
    });
  }
}
