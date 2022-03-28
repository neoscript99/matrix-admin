import Taro from '@tarojs/taro';

export class TaroMessageApi {
  static error(content: any): any {
    return Taro.atMessage({
      message: `错误：${content}`,
      type: 'error',
    });
  }

  static info(content: any): any {
    return Taro.atMessage({
      message: content,
      type: 'info',
      duration: 2000,
    });
  }

  static success(content: any): any {
    return Taro.atMessage({
      message: content,
      type: 'success',
      duration: 2000,
    });
  }

  static warn(content: any): any {
    return Taro.atMessage({
      message: content,
      type: 'warning',
    });
  }
}
