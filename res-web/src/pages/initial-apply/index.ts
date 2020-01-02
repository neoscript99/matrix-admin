export * from './InitialApplyList';

const EDITABLE_STATUSES = ['draft', 'repair'];
export function checkEditable(statusCode: string) {
  return EDITABLE_STATUSES.includes(statusCode);
}
