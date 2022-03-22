import moment = require('dayjs');

describe('util test', () => {
  it('dayjs test', () => {
    console.log(moment.isDayjs(null));
    console.log(moment.isDayjs(undefined));
    const m = moment();
    console.log(m.format());
    console.log(m.format('MMDDHHmmssSSS'));
    console.log(m.format().substr(0, 10));
    console.log(typeof m);
    console.log(typeof m.date());
    const mm = moment(m);
    console.log(mm.format());
  });
});
