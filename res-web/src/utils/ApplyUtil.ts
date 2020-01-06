const EDITABLE_STATUSES = ['draft', 'repair'];
export class ApplyUtil {
  static checkEditable(apply: any) {
    return !!apply && EDITABLE_STATUSES.includes(apply.statusCode);
  }
}
