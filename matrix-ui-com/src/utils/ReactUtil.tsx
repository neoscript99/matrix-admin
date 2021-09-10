import React from 'react';
import { StyleUtil } from './StyleUtil';
import { Form } from 'antd';
import { FormInstance } from 'antd/lib/form';
import { ServiceUtil } from 'matrix-ui-service';

export const useServiceStore = ServiceUtil.initReactUseStore(React);
export interface FormComponentProps {
  form: FormInstance;
}
export class ReactUtil {
  static formWrapper<PP>(comp: React.ComponentType<PP>, mapPropName = 'inputItem'): React.FC<Omit<PP, 'form'>> {
    const formWrapper = (props) => {
      const [form] = Form.useForm();
      const inputItem = props[mapPropName];
      React.useEffect(() => {
        form.setFieldsValue(inputItem);
      }, [inputItem]);

      const pp = ({ ...props, form } as unknown) as PP;
      return React.createElement(comp, pp);
    };
    return formWrapper;
  }
  static hiddenTextRender(maxSize: number, value: React.ReactNode) {
    const HiddenText = StyleUtil.hiddenText(maxSize);
    return <HiddenText>{value}</HiddenText>;
  }

  static wordBreakTextRender(maxSize: number, value: React.ReactNode) {
    const BreakText = StyleUtil.wordBreakText(maxSize);
    return <BreakText>{value}</BreakText>;
  }
}
