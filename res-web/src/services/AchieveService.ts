import { DomainServiceOptions, DomainService, MobxDomainStore, Entity } from 'oo-rest-mobx';

export class AchieveService extends DomainService {
  constructor(options: DomainServiceOptions) {
    super(options);
  }

  listByRound(round: Entity): Promise<Entity[]> {
    return this.postApi('listByRound', { roundId: round.id });
  }
}
