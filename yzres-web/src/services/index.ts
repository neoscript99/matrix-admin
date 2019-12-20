import { LoginInfo, AdminServices, DomainService, SpringBootClient, MobxDomainStore } from 'oo-rest-mobx';
import { config } from '../utils';
import { YzUserService } from './YzUserService';
import { YzDeptService } from './YzDeptService';

function afterLogin(loginInfo: LoginInfo) {
  if (!loginInfo.token) throw 'token不能为空';
}

export const restClient = new SpringBootClient({ rootUrl: config.serverRoot });

export const yzDeptService = new YzDeptService(restClient);
//本服务仅用户鄞州项目的用户管理，不做登录等操作
export const yzUserService = new YzUserService(restClient);
export const adminServices = new AdminServices(restClient, afterLogin, { deptService: yzDeptService });
export const dictService = adminServices.dictService;
export const workPlanService = new DomainService({ domain: 'topicWorkPlan', restClient, storeClass: MobxDomainStore });
export const initialApplyService = new DomainService({
  domain: 'topicInitialApply',
  restClient,
  storeClass: MobxDomainStore,
});

export const userService = adminServices.userService;
userService.trySessionLogin().then(loginInfo => loginInfo.success || userService.tryLocalLogin());
