import { message } from 'antd';
import { MessageUtil } from 'matrix-ui-service';
import { Config } from './Config';

export const config = new Config();
MessageUtil.api = message;
