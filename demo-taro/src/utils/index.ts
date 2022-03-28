import { MessageUtil } from 'matrix-ui-service';
import { Config } from './Config';
import { TaroMessageApi } from './TaroMessageApi';

export const config = new Config();

MessageUtil.api = TaroMessageApi;
export * from './TaroUtil';
