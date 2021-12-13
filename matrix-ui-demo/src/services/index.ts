import { SpringBootClient, ServiceYard, MessageUtil } from 'matrix-ui-service';
import { config } from '../utils';

export const restClient = new SpringBootClient({ rootUrl: config.serverRoot });
restClient.registerErrorHandler((error) => {
  MessageUtil.error(error.message);
});

class DemoServiceYard extends ServiceYard {}

export const yard = new DemoServiceYard({ restClient });
