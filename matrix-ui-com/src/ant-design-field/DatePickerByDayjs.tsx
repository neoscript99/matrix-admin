import { Dayjs } from 'dayjs';
import dayjsGenerateConfig from 'rc-picker/es/generate/dayjs';
import generatePicker, { PickerProps } from 'antd/es/date-picker/generatePicker';
import 'antd/es/date-picker/style/index';
export type DatePickerByDayjsProps = PickerProps<Dayjs>;
export const DatePickerByDayjs = generatePicker<Dayjs>(dayjsGenerateConfig);
