import { StorageApi } from '../api';

export class StorageUtil {
  static api: StorageApi;
  static getApi() {
    return StorageUtil.api || localStorage;
  }

  static clear(): void {
    StorageUtil.getApi()?.clear();
  }

  static getItem(key: string): string | null {
    return StorageUtil.getApi()?.getItem(key);
  }

  static key(index: number): string | null {
    return StorageUtil.getApi()?.key(index);
  }

  static removeItem(key: string): void {
    StorageUtil.getApi()?.removeItem(key);
  }

  static setItem(key: string, value: string): void {
    StorageUtil.getApi()?.setItem(key, value);
  }
}
