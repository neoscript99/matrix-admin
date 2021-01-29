export interface MessageApi {
  info(content: any): any;
  success(content: any): any;
  error(content: any): any;
  warn(content: any): any;
}
