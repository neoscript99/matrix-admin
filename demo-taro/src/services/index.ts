import { ServiceUtil, SpringBootClient, ServiceYard } from 'matrix-ui-service';
import React from 'react';
import Taro from '@tarojs/taro';
import { config } from '../utils';
import { AuthService } from './AuthService';

export const useServiceStore = ServiceUtil.initReactUseStore(React);
export const taroFetch = ServiceUtil.initTaroFetch(Taro.request);
export const restClient = new SpringBootClient({ fetch: taroFetch, rootUrl: config.serverRoot });

export const yard = new ServiceYard({ restClient });
export const { loginService, wxMaService } = yard.adminServices;
export const authService = new AuthService(restClient);
Taro.login().then(({ code }) => wxMaService.wxMaLogin(code));
