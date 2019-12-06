package org.yzedu.research.config.common

import com.feathermind.matrix.domain.sys.Dict
import com.feathermind.matrix.domain.sys.DictType
import com.feathermind.matrix.initializer.AbstractDataInitializer
import com.feathermind.matrix.initializer.DataInitializer
import org.springframework.core.annotation.Order

@Order(100)
class ResDictInitializer extends AbstractDataInitializer implements DataInitializer {
    @Override
    boolean isInited() {
        DictType.get('topic-cate')
    }

    @Override
    void doInit() {
        topicCateDict()
        topicSourceDict()
        degreeDict()
        researchDirectionDict()
    }

    void topicCateDict() {
        def dictType = new DictType(id: 'yz-res-topic-cate', name: '课题类别').save()

        new Dict(code: 'YZZD', name: '鄞州重点', seq: 1, type: dictType).save();
        new Dict(code: 'YZGH', name: '鄞州规划', seq: 2, type: dictType).save();
        new Dict(code: 'YZYS', name: '鄞州艺术', seq: 3, type: dictType).save();
        new Dict(code: 'YZKJ', name: '鄞州课程', seq: 4, type: dictType).save();
        new Dict(code: 'YZDY', name: '鄞州德育', seq: 5, type: dictType).save();
        new Dict(code: 'YZTY', name: '鄞州体育', seq: 6, type: dictType).save();
    }

    void topicSourceDict() {
        def dictType = new DictType(id: 'yz-res-topic-source', name: '课题来源').save()

        new Dict(code: 'CZ', name: '初中', seq: 1, type: dictType).save();
        new Dict(code: 'XX', name: '小学', seq: 2, type: dictType).save();
        new Dict(code: 'JN', name: '九年制', seq: 3, type: dictType).save();
        new Dict(code: 'YJ', name: '幼教', seq: 4, type: dictType).save();
        new Dict(code: 'TJ', name: '特教', seq: 5, type: dictType).save();
        new Dict(code: 'CJ', name: '成教', seq: 6, type: dictType).save();
        new Dict(code: 'MB', name: '民办', seq: 7, type: dictType).save();
        new Dict(code: 'ZS', name: '直属', seq: 8, type: dictType).save();
    }

    void degreeDict() {
        def dictType = new DictType(id: 'yz-res-degree', name: '学历').save()

        new Dict(code: 'doctor', name: '博士研究生', seq: 1, type: dictType).save();
        new Dict(code: 'master', name: '硕士研究生', seq: 2, type: dictType).save();
        new Dict(code: 'bachelor', name: '大学本科', seq: 3, type: dictType).save();
        new Dict(code: 'college-below', name: '大专及以下', seq: 4, type: dictType).save();

    }

    /**
     * 研究内容
     */
    void researchDirectionDict() {
        def dictType = new DictType(id: 'yz-res-dir', name: '研究内容').save()

        new Dict(code: 'school-manage', name: '学校管理', seq: 10, type: dictType).save();
        new Dict(code: 'edu-mode', name: '育人模式', seq: 20, type: dictType).save();
        new Dict(code: 'school-culture', name: '校园文化', seq: 30, type: dictType).save();
        new Dict(code: 'general-practice', name: '综合实践', seq: 40, type: dictType).save();
        new Dict(code: 'teacher-edu', name: '教师教育', seq: 50, type: dictType).save();
        new Dict(code: 'teaching-manage', name: '教学管理', seq: 60, type: dictType).save();
        new Dict(code: 'moral-edu', name: '德育', seq: 70, type: dictType).save();
        new Dict(code: 'course-material-method', name: '课程教材教法', seq: 80, type: dictType).save();
        new Dict(code: 'sport-edu', name: '体育', seq: 90, type: dictType).save();
        new Dict(code: 'aesthetic-edu', name: '美育', seq: 100, type: dictType).save();
        new Dict(code: 'mental-health', name: '心理健康教育', seq: 110, type: dictType).save();
        new Dict(code: 'labour-edu', name: '劳动教育', seq: 120, type: dictType).save();
        new Dict(code: 'it', name: '信息技术', seq: 130, type: dictType).save();
        new Dict(code: 'other', name: '其他', seq: 140, type: dictType).save();
    }

    void subjectDict() {
        def dictType = new DictType(id: 'yz-res-subject', name: '学科').save()
        new Dict(code: 'chinese', name: '语文', seq: 10, type: dictType).save();
        new Dict(code: 'mathematics', name: '数学', seq: 20, type: dictType).save();
        new Dict(code: 'foreign-language', name: '外语', seq: 30, type: dictType).save();
        new Dict(code: 'science', name: '科学（物理、化学、生物、信息技术）', seq: 40, type: dictType).save();
        new Dict(code: 'society', name: '社会（政治、历史、地理）', seq: 50, type: dictType).save();
        new Dict(code: 'sport', name: '体育', seq: 60, type: dictType).save();
        new Dict(code: 'art', name: '艺术（音乐、美术）', seq: 70, type: dictType).save();
        new Dict(code: 'mental-health', name: '心理健康教育', seq: 80, type: dictType).save();
        new Dict(code: 'general-practice', name: '综合实践活动（含劳动）', seq: 90, type: dictType).save();
        new Dict(code: 'other', name: '其他', seq: 100, type: dictType).save();
    }

    void targetDict() {
        def dictType = new DictType(id: 'yz-res-target', name: '研究对象').save()
        new Dict(code: 'student', name: '学生', seq: 10, type: dictType).save();
        new Dict(code: 'teacher', name: '教师', seq: 10, type: dictType).save();
        new Dict(code: 'school', name: '学校（幼儿园）', seq: 10, type: dictType).save();
        new Dict(code: 'other', name: '其它', seq: 10, type: dictType).save();
    }
}
