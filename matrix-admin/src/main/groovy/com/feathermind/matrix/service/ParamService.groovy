package com.feathermind.matrix.service

import com.feathermind.matrix.domain.sys.Param
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * 参数管理
 * @since 2018-10-16
 * @author wangchu
 */
@Service
class ParamService extends AbstractService<Param> {
    @Value('${spring.profiles.active}')
    String profiles

    String getValue(String typeCode, String code) {
        log.info("ParamService.getByTypeAndCode $typeCode $code");
        findFirst([eq: [['type.code', typeCode], ['code', code]]])?.value
    }

    String getProfiles(){
        return profiles
    }
}
