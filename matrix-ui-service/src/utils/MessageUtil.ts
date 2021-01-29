import { MessageApi } from '../api';

export class MessageUtil {
  static api: MessageApi;
  static error(content: any): any {
    return MessageUtil.api?.error(content);
  }

  static info(content: any): any {
    return MessageUtil.api?.info(content);
  }

  static success(content: any): any {
    return MessageUtil.api?.success(content);
  }

  static warn(content: any): any {
    return MessageUtil.api?.warn(content);
  }
}
