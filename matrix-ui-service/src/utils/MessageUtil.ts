import { MessageApi } from '../api';

const apiInfo = '(设置MessageUtil.api后，可实现界面展示)';
export class MessageUtil {
  static api: MessageApi;
  static error(content: any): any {
    return MessageUtil.api ? MessageUtil.api.error(content) : console.error(content + apiInfo);
  }

  static info(content: any): any {
    return MessageUtil.api ? MessageUtil.api.info(content) : console.info(content + apiInfo);
  }

  static success(content: any): any {
    return MessageUtil.api ? MessageUtil.api.success(content) : console.info(content + apiInfo);
  }

  static warn(content: any): any {
    return MessageUtil.api ? MessageUtil.api.warn(content) : console.warn(content + apiInfo);
  }
}
