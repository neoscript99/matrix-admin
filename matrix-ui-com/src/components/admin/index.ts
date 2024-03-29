import { AdminServices } from 'matrix-ui-service';
import { EntityListProps } from '../layout';

export interface AdminPageProps extends EntityListProps {
  services: AdminServices;
}

export * from './Home';
export * from './Welcome';

export * from './role';
export * from './user';
export * from './dept';
export * from './user_role';
export * from './login';
export * from './note';
export * from './param';
export * from './apply';
