import { AbstractClient } from '../rest';
import { DomainStore } from './DomainStore';
import { DomainService, Entity, LoginInfo } from './index';

export interface AttachmentEntity extends Entity {
  id: string;
  name: string;
  fileSize: number;
  fileId: string;
  ownerId?: string;
  ownerName?: string;
  dateCreated: Date;
}

export class AttachmentStore extends DomainStore<AttachmentEntity> {
  loginInfo: LoginInfo = { success: false, token: 'none' };
}

export class AttachmentService extends DomainService<AttachmentEntity, AttachmentStore> {
  maxSizeMB = 20;
  previewEnable = false;
  constructor(restClient: AbstractClient) {
    super({ domain: 'attachment', storeClass: AttachmentStore, restClient });
    this.getMaxSizeMB().then((mb) => (this.maxSizeMB = mb));
    this.post(`/previewCheck`).then((res) => (this.previewEnable = res.success));
  }
  get uploadUrl() {
    return `${this.rootUrl}/upload`;
  }
  get downloadUrl() {
    return `${this.rootUrl}/download`;
  }
  urlWithToken(id: string) {
    return `${this.downloadUrl}/${id}?token=${this.store.loginInfo.token}`;
  }
  get previewUrl() {
    return this.previewEnable && `${this.rootUrl}/preview`;
  }
  getMaxSizeMB(): Promise<number> {
    return this.postApi('getMaxSizeMB');
  }

  afterLogin(loginInfo: LoginInfo) {
    super.afterLogin(loginInfo);
  }
}
