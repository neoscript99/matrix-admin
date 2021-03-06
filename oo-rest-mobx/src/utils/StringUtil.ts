import moment from 'moment';
import { sha256 } from 'js-sha256';
import capitalize from 'lodash/capitalize';
import camelCase from 'lodash/camelCase';
import upperFirst from 'lodash/upperFirst';
type HashMessage = string | number[] | ArrayBuffer | Uint8Array;

export class StringUtil {
  static dateStringConvert(fromDateFormat: string, toDateFormat: string, text: string) {
    if (text && fromDateFormat && toDateFormat) return moment(text, fromDateFormat).format(toDateFormat);
    else return text;
  }

  static isNotBlank(value: string | null | undefined): boolean {
    return value && value.trim().length > 0 ? true : false;
  }

  static isBlank(value: string | null | undefined): boolean {
    return !StringUtil.isNotBlank(value);
  }

  /**
   * 根据时间生成随机字符串，最大长度64
   * @param length
   */
  static randomString(length = 8): string {
    return sha256(new Date().toISOString()).substr(0, length);
  }

  static sha256(message: HashMessage): string {
    return sha256(message);
  }

  static capitalize = capitalize;
  static camelCase = camelCase;
  static upperFirst = upperFirst;
}
