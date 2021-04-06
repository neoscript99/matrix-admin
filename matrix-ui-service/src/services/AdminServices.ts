import {
  DeptService,
  MenuService,
  ParamService,
  RoleService,
  UserService,
  DictService,
  DomainService,
  LoginService,
  AttachmentService,
  ApplyService,
  ApplyLogService,
  WechatService,
} from './';
import { SpringBootClient } from '../rest';
import { DomainStore } from './DomainStore';

export class AdminServices {
  userService: UserService;
  roleService: RoleService;
  paramService: ParamService;
  noteService: DomainService;
  menuService: MenuService;
  deptService: DeptService;
  userRoleService: DomainService;
  dictService: DictService;
  loginService: LoginService;
  attachmentService: AttachmentService;
  applyService: ApplyService;
  applyLogService: ApplyLogService;
  wechatService: WechatService;

  constructor(restClient: SpringBootClient, initServices?: Partial<AdminServices>) {
    this.paramService = new ParamService(restClient);
    this.noteService = new DomainService({ domain: 'note', storeClass: DomainStore, restClient });
    this.userRoleService = new DomainService({ domain: 'userRole', storeClass: DomainStore, restClient });
    this.roleService = new RoleService(restClient);
    this.menuService = new MenuService(restClient);
    //userService支持替换
    this.userService = initServices?.userService || new UserService(restClient);
    //deptService支持替换
    this.deptService = initServices?.deptService || new DeptService(restClient);
    this.dictService = new DictService(restClient);
    //外部设置的afterLogin必须首先执行，需要设置安全认证header
    this.loginService = new LoginService(restClient);
    this.wechatService = new WechatService(restClient, this.loginService);
    this.attachmentService = new AttachmentService(restClient);
    this.applyService = new ApplyService(restClient);
    this.applyLogService = new ApplyLogService(restClient);
  }
}
