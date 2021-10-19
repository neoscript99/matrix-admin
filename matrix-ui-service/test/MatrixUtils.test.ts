import { Criteria } from '../src/services';
import { StringUtil, ServiceUtil, LangUtil } from '../src/utils';

describe('MatrixUtilsTest', () => {
  it('string util', () => {
    for (let i = 0; i < 10; i++) console.log(StringUtil.randomString(128));
    const str = '';
    //会出现[ '' ]这样的数组，注意问题
    console.log(str.split(','));
    console.log([].join(','));
  });

  it('processCriteria test 1', () => {
    const criteria: Criteria = {
      order: [
        ['aa', 'asc'],
        ['bb', 'desc'],
        ['cc.name', 'asc'],
      ],
    };
    ServiceUtil.processNestCriteria(criteria);
    console.debug(criteria);
    expect(criteria).toEqual({
      order: [
        ['aa', 'asc'],
        ['bb', 'desc'],
      ],
      cc: { order: [['name', 'asc']] },
    });
  });

  it('processCriteria test 2', () => {
    const criteria2: Criteria = {
      order: [
        ['portal.seq', 'asc'],
        ['rowOrder', 'desc'],
      ],
      between: [['portal.age', 10, 20]],
    };
    ServiceUtil.processNestCriteria(criteria2);
    console.debug(criteria2);
    expect(criteria2).toEqual({
      between: [],
      order: [['rowOrder', 'desc']],
      portal: { between: [['age', 10, 20]], order: [['seq', 'asc']] },
    });
  });

  it('flatten object', () => {
    const obj = { dept: { name: '总', seq: 1, enabled: true } };
    const fo = LangUtil.flattenObject(obj);
    console.debug(fo);
    expect(fo['dept.seq']).toEqual(1);
  });
});
