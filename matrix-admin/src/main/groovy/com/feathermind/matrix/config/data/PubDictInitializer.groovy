package com.feathermind.matrix.config.data

import com.feathermind.matrix.domain.sys.Dict
import com.feathermind.matrix.domain.sys.DictType
import com.feathermind.matrix.initializer.AbstractDataInitializer
import com.feathermind.matrix.initializer.DataInitializer
import org.springframework.core.annotation.Order

@Order(100)
class PubDictInitializer extends AbstractDataInitializer implements DataInitializer {
    @Override
    boolean isInited() {
        DictType.get('pub_sex')
    }

    @Override
    void doInit() {
        sexDict()
        degreeDict()
    }

    void sexDict() {
        def dictType = new DictType(id: 'pub_sex', name: '性别').save()

        new Dict(code: 'male', name: '男', seq: 10, type: dictType).save();
        new Dict(code: 'female', name: '女', seq: 20, type: dictType).save();
    }

    void degreeDict() {
        def dictType = new DictType(id: 'pub_degree', name: '学历').save()

        new Dict(code: 'doctor', name: '博士研究生', seq: 10, type: dictType).save();
        new Dict(code: 'master', name: '硕士研究生', seq: 20, type: dictType).save();
        new Dict(code: 'bachelor', name: '大学本科', seq: 30, type: dictType).save();
        new Dict(code: 'college-below', name: '大专及以下', seq: 40, type: dictType).save();
    }
}
