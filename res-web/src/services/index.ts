import { LoginInfo, AdminServices, DomainService, SpringBootClient, MobxDomainStore } from 'oo-rest-mobx';
import { config } from '../utils';
import { ResUserService } from './ResUserService';
import { ResDeptService } from './ResDeptService';
import { InitialPlanService } from './InitialPlanService';
import { TopicService } from './TopicService';
import { TopicMemberService } from './TopicMemberService';

function afterLogin(loginInfo: LoginInfo) {
  if (!loginInfo.token) throw 'token不能为空';
  initialPlanService.initDictList();
}
const storeClass = MobxDomainStore;
export const restClient = new SpringBootClient({ rootUrl: config.serverRoot });

//机构、用户服务自定义
export const resDeptService = new ResDeptService(restClient);
export const resUserService = new ResUserService(restClient);
//用于课题成员的维护，防止store冲突
export const resTopicUserService = new ResUserService(restClient);
export const adminServices = new AdminServices(restClient, afterLogin, {
  deptService: resDeptService,
  userService: resUserService,
});
export const dictService = adminServices.dictService;
export const initialPlanService = new InitialPlanService(restClient);
export const topicService = new TopicService(restClient);
//独立store
export const initialApplyService = new TopicService(restClient);
//独立store
export const finishApplyService = new TopicService(restClient);
export const topicMemberService = new TopicMemberService(restClient);
export const applyService = new DomainService({ domain: 'apply', restClient, storeClass });

export const loginService = adminServices.loginService;
loginService.trySessionLogin().then(loginInfo => loginInfo.success || loginService.tryLocalLogin());
