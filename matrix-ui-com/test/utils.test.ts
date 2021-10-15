import moment = require('moment');
import { LangUtil } from 'matrix-ui-service';

describe('util test', () => {
  it('moment test', () => {
    console.log(moment.isMoment(null));
    console.log(moment.isMoment(undefined));
    const m = moment();
    console.log(m.format());
    console.log(m.format('MMDDHHmmssSSS'));
    console.log(m.format().substr(0, 10));
    console.log(typeof m);
    console.log(typeof m.date());
    console.log(LangUtil.getClassName(m));
    const mm = moment(m);
    console.log(mm.format());
  });

  it('flattenObject test', () => {
    const flat = LangUtil.flattenObject({
      user: { map: { a: '123424', b: { b1: 'XYZ', b2: moment() } } },
      name: 'name',
    });
    console.log(flat);
    console.log(typeof flat['user.map.b.b2']);
    expect(flat['user.map.b.b1']).toEqual('XYZ');
  });
});
