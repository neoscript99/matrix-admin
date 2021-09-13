import { RestService } from './RestService';
import clone from 'lodash/clone';

export interface StoreChangeListener<D> {
  (store: D): void;
}

export abstract class StoreService<D = any> extends RestService {
  public store: D;

  changeListeners: StoreChangeListener<D>[] = [];

  addChangeListener(fun: StoreChangeListener<D>) {
    this.changeListeners.push(fun);
  }

  removeChangeListener(fun: StoreChangeListener<D>) {
    const len = this.changeListeners.length;
    for (let i = 0; i < len; i++) {
      if (this.changeListeners[i] === fun) {
        this.changeListeners.splice(i, 1);
        return;
      }
    }
  }
  setStore(newStore: Partial<D>) {
    Object.assign(this.store, newStore);
    this.fireStoreChange();
  }
  fireStoreChange() {
    //必须用一个新实例，否则用===判断无法获知更新，如react.setState hooks
    //用clone保持对象信息，否则都会变成plain对象
    const newStore = clone(this.store);
    this.changeListeners.forEach((listener) => listener(newStore));
  }
}
