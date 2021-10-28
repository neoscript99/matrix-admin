import { loginPromise, yard } from './TestEnv';
import { DeptEntity } from '../src';

const userService = yard.adminServices.userService;

describe('Domain Query', () => {
  it('query api', async () => {
    //等待登录成功
    await loginPromise;
    const res = await userService.query(
      { dept: { name: '总', id: '0' } as DeptEntity, account: 'admin', current: 1, pageSize: 10, keyword: 'abc' },
      { 'dept,name': 'descend', 'dept,seq': 'descend' },
      { 'dept,seq': [0, 1, 2], enabled: [true] },
    );
    console.debug(JSON.stringify(res));
    expect(res.totalCount).toBeGreaterThan(0);
  });
});
