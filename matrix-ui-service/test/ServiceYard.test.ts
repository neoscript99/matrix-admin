import { loginPromise, sleep, yard } from './TestEnv';
import { AfterLoginService } from '../src/services';

class AfterLogin01 {
  isLogin = false;
  afterLogin() {
    console.log('AfterLogin01');
    this.isLogin = true;
  }
}
class AfterLogin02 implements AfterLoginService {
  afterLogin() {
    console.log('AfterLogin02');
  }
}
describe('ServiceYard Test', () => {
  it('test after login', async () => {
    yard.s01 = new AfterLogin01();
    yard.s02 = new AfterLogin02();
    //等待登录成功
    await loginPromise;
    //等待afterLogin调用
    await sleep(3);

    expect(yard.s01.isLogin).toBeTruthy();
  });
});
