import React from 'react';
import { UserForm } from 'oo-rest-mobx';
import { yzUserService } from '../../services';

export class YzUserForm extends UserForm {
  get userService() {
    return yzUserService;
  }
}
