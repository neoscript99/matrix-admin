import { loginPromise, yard } from './TestEnv';

const userService = yard.adminServices.userService;

describe('Domain Query', () => {
  it('query api', async () => {
    await loginPromise;
    const res = await userService.query({ name: '系统' }, { name: null }, { account: ['admin'] });
    console.debug(res);
    expect(res.totalCount).toBeGreaterThan(0);
  });
});
