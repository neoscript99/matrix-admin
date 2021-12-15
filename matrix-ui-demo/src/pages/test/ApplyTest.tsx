import React, { useCallback, useState } from 'react';
import { ApplyForm } from 'matrix-ui-com/lib/components';
import { yard } from '../../services';
import { ApplyEntity } from 'matrix-ui-service';
const mockApply = { id: '1ceece6a-158f-430c-8fdd-2adfe56d9e43' };
export const ApplyTest = () => {
  const [apply, setApply] = useState<ApplyEntity>(mockApply as ApplyEntity);
  const onSubmit = useCallback((a) => console.log('onSubmit: ', a), []);
  const onCancel = useCallback(() => console.log('onCancel: '), []);
  return (
    <ApplyForm
      apply={apply}
      applyDictType="res-apply-status"
      adminServices={yard.adminServices}
      onSubmit={onSubmit}
      onCancel={onCancel}
    />
  );
};
