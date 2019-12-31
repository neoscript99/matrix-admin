import { LoginInfo, AdminServices, DomainService, SpringBootClient, MobxDomainStore } from 'oo-rest-mobx';
import { config } from '../utils';
import { ResUserService } from './ResUserService';
import { ResDeptService } from './ResDeptService';
import { WorkPlanService } from './WorkPlanService';
import { TopicService } from './TopicService';
import { TopicMemberService } from './TopicMemberService';

function afterLogin(loginInfo: LoginInfo) {
  if (!loginInfo.token) throw 'token不能为空';
  workPlanService.initDictList();
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
export const workPlanService = new WorkPlanService(restClient);
export const topicService = new TopicService(restClient);
export const topicMemberService = new TopicMemberService(restClient);
export const initialApplyService = new DomainService({ domain: 'topicInitialApply', restClient, storeClass });

export const loginService = adminServices.loginService;
loginService.trySessionLogin().then(loginInfo => loginInfo.success || loginService.tryLocalLogin());
