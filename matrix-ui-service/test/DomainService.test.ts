import { yard } from './TestEnv';

const userService = yard.adminServices.userService;
const deptService = yard.adminServices.deptService;

describe('Domain CURD', () => {
  it('user list and get', async () => {
    //嵌套属性排序，目前gorm不支持，可能是用了DetachedCriteria的原因，原来做客户经理考核的时候好像是支持的
    const data = await userService.listAll({
      criteria: { dept: { like: [['name', '%']] } },
      pageInfo: { currentPage: 1, pageSize: 2 },
      orders: [
        ['dept.seq', 'asc'],
        ['name', 'asc'],
      ],
    });

    expect(data).not.toBeNull();
    expect(data.totalCount).toBeGreaterThan(0);
    console.log(data.results[0].id);
    expect(await userService.get(data.results[0].id)).toHaveProperty('name');
  });

  it('create, update, list, delete', async () => {
    await deptService.save({ name: 'DeptTest1', seq: 999, enabled: false });
    await deptService.save({ name: 'DeptTest2', seq: 999, enabled: false });
    const department = await deptService.save({ name: 'DeptTest3', seq: 999, enabled: false });
    expect(department.seq).toEqual(999);
    department.seq = 888;
    const updatedDept = await deptService.save(department);
    expect(updatedDept.seq).toEqual(888);

    const firstDept = await deptService.findFirst({ eq: [['seq', 888]] });
    console.log(firstDept);
    if (firstDept) expect(await deptService.delete(firstDept.id)).toEqual(1);

    const listDept = await deptService.listFirstPage({ criteria: { eq: [['seq', 999]] } });
    expect(await deptService.deleteByIds(listDept.results.map((dept) => dept.id))).toEqual(2);
  });
});
