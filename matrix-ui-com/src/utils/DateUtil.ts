import moment from 'moment';

export class DateUtil {
  static dateStringConvert(fromDateFormat: string, toDateFormat: string, text: string) {
    if (text && fromDateFormat && toDateFormat) return moment(text, fromDateFormat).format(toDateFormat);
    else return text;
  }
}
