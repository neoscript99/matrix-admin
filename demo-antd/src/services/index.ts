import { SpringBootClient, ServiceYard } from 'matrix-ui-service';
import { config } from '../utils';

export const restClient = new SpringBootClient({ rootUrl: config.serverRoot });

class DemoServiceYard extends ServiceYard {}

export const yard = new DemoServiceYard({ restClient });
