import { ServiceUtil, SpringBootClient,ServiceYard } from 'matrix-ui-service';
import React from 'react';
import Taro from '@tarojs/taro';
import { config } from '../utils';

export const useServiceStore = ServiceUtil.initReactUseStore(React);
export const taroFetch = ServiceUtil.initTaroFetch(Taro.request);
export const restClient = new SpringBootClient({ fetch: taroFetch, rootUrl: config.serverRoot });


export const yard = new ServiceYard({ restClient });
