import { LoginInfo, AdminServices, DomainService, SpringBootClient, MobxDomainStore } from 'oo-rest-mobx';
import { config } from '../utils';
import { ResUserService } from './ResUserService';
import { ResDeptService } from './ResDeptService';
import { PlanService } from './PlanService';
import { TopicService } from './TopicService';
import { TopicMemberService } from './TopicMemberService';
import { TopicSupportService } from './TopicSupportService';

function afterLogin(loginInfo: LoginInfo) {
  if (!loginInfo.token) throw 'token不能为空';
  initialPlanService.initDictList();
  reviewPlanService.initDictList();
  resDeptTypeService.listAll({});
}

const storeClass = MobxDomainStore;
export const restClient = new SpringBootClient({ rootUrl: config.serverRoot });

export const resDeptTypeService = new DomainService({ domain: 'resDeptType', restClient, storeClass });
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
export const paramService = adminServices.paramService;
export const attachmentService = adminServices.attachmentService;
export const deptService = adminServices.deptService;
export const initialPlanService = new PlanService('initialPlan', restClient);
export const topicService = new TopicService(restClient);
//独立store
export const initialApplyService = new TopicService(restClient);
//独立store
export const finishApplyService = new TopicService(restClient);
export const topicMemberService = new TopicMemberService(restClient);
export const topicSupportService = new TopicSupportService(restClient);
export const applyService = new DomainService({ domain: 'apply', restClient, storeClass });
export const reviewPlanService = new PlanService('reviewPlan', restClient);
export const reviewRoundService = new PlanService('reviewRound', restClient);
export const reviewRoundExpertService = new PlanService('reviewRoundExpert', restClient);
export const reviewRoundScoreService = new PlanService('reviewRoundScore', restClient);
export const paperService = new DomainService({ domain: 'paper', restClient, storeClass });
export const topicAchieveService = new DomainService({ domain: 'topicAchieve', restClient, storeClass });
export const topicAchieveMemberService = new DomainService({ domain: 'topicAchieveMember', restClient, storeClass });

export const loginService = adminServices.loginService;
//session登录不成功的话，尝试本地保存的用户密码登录
loginService.trySessionLogin().then(loginInfo => loginInfo.success || loginService.tryLocalLogin());
