import { Dayjs } from 'dayjs';
import * as React from 'react';
import { DatePickerByDayjs } from './DatePickerByDayjs';
import { PickerTimeProps } from 'antd/es/date-picker/generatePicker';

export type TimePickerByDayjsProps = Omit<PickerTimeProps<Dayjs>, 'picker'>;

export const TimePickerByDayjs = React.forwardRef<any, TimePickerByDayjsProps>((props, ref) => {
  return <DatePickerByDayjs {...props} picker="time" mode={undefined} ref={ref} />;
});

TimePickerByDayjs.displayName = 'TimePicker';
