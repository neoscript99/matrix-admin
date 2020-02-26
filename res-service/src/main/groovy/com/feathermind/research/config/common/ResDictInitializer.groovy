package com.feathermind.research.config.common

import com.feathermind.matrix.domain.sys.Dict
import com.feathermind.matrix.domain.sys.DictType
import com.feathermind.matrix.domain.sys.Param
import com.feathermind.matrix.domain.sys.ParamType
import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.initializer.AbstractDataInitializer
import com.feathermind.matrix.initializer.DataInitializer
import org.springframework.core.annotation.Order

@Order(100)
class ResDictInitializer extends AbstractDataInitializer implements DataInitializer {
    @Override
    boolean isInited() {
        DictType.get('res-topic-cate')
    }

    @Override
    void doInit() {
        initParam()
        topicCateDict()
        topicSourceDict()
        resContentDict()
        subjectDict()
        resTargetDict()
        resAchieveFormDict()
        resAchieveCateDict()
        applyStatusDict()
        topicStatusDict()
        planStatusDict()
        reviewTypeDict()
        projectLevelDict()
        avgAlgorithmDict()
        runStatusDict()
    }

    void initParam() {
        new Param(code: 'ChangeInitPassword', name: '强制修改初始密码',
                value: 'false', type: ParamType.SYSTEM, validExp: '^(true|false)$',
                validDescribe: 'true或者false', lastUser: User.ADMIN).save()
    }

    void topicCateDict() {
        def cateType = new DictType(id: 'res-plan-cate', name: '计划类型').save()

        def yzgh = new Dict(code: 'YZGH', name: '规划课题', seq: 10, type: cateType).save();
        def yzkj = new Dict(code: 'YZKJ', name: '鄞州课程', seq: 20, type: cateType).save();
        def yzdy = new Dict(code: 'YZDY', name: '鄞州德育', seq: 30, type: cateType).save();
        def yzyj = new Dict(code: 'YZYJ', name: '鄞州幼教', seq: 40, type: cateType).save();
        def yzzd = new Dict(code: 'YZZD', name: '鄞州重点', seq: 50, type: cateType).save();

        def childType = new DictType(id: 'res-topic-cate', name: '课题类别', parentId: cateType.id).save()

        new Dict(code: 'YZGH', name: '鄞州规划', seq: 10, type: childType, parentCode: yzgh.code).save();
        new Dict(code: 'YZLS', name: '绿色教育', seq: 20, type: childType, parentCode: yzgh.code).save();
        new Dict(code: 'YZTY', name: '鄞州体艺', seq: 30, type: childType, parentCode: yzgh.code).save();
        new Dict(code: 'YZKC', name: '鄞州课程', seq: 40, type: childType, parentCode: yzkj.code).save();
        new Dict(code: 'YZDY', name: '鄞州德育', seq: 50, type: childType, parentCode: yzdy.code).save();
        new Dict(code: 'YZYJ', name: '鄞州幼教', seq: 60, type: childType, parentCode: yzyj.code).save();
        new Dict(code: 'YZZD', name: '鄞州重点', seq: 70, type: childType, parentCode: yzzd.code).save();
    }

    void topicSourceDict() {
        def dictType = new DictType(id: 'res-topic-source', name: '课题来源').save()

        new Dict(code: 'CZ', name: '初中', seq: 10, type: dictType).save();
        new Dict(code: 'XX', name: '小学', seq: 20, type: dictType).save();
        new Dict(code: 'JN', name: '九年制', seq: 30, type: dictType).save();
        new Dict(code: 'YJ', name: '幼教', seq: 40, type: dictType).save();
        new Dict(code: 'TJ', name: '特教', seq: 50, type: dictType).save();
        new Dict(code: 'CJ', name: '成教', seq: 60, type: dictType).save();
        new Dict(code: 'MB', name: '民办', seq: 70, type: dictType).save();
        new Dict(code: 'ZS', name: '直属', seq: 80, type: dictType).save();
    }

    /**
     * 研究内容
     */
    void resContentDict() {
        def dictType = new DictType(id: 'res-content', name: '研究内容').save()

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
        def dictType = new DictType(id: 'res-subject', name: '学科').save()
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

    void resTargetDict() {
        def dictType = new DictType(id: 'res-target', name: '研究对象').save()
        new Dict(code: 'student', name: '学生', seq: 10, type: dictType).save();
        new Dict(code: 'teacher', name: '教师', seq: 20, type: dictType).save();
        new Dict(code: 'school', name: '学校（幼儿园）', seq: 30, type: dictType).save();
        new Dict(code: 'other', name: '其它', seq: 40, type: dictType).save();
    }

    void resAchieveFormDict() {
        def dictType = new DictType(id: 'res-achieve-form', name: '成果形式').save()
        new Dict(code: 'writing-translation', name: '专（译）著', seq: 10, type: dictType).save();
        new Dict(code: 'research-report', name: '研究报告', seq: 20, type: dictType).save();
        new Dict(code: 'paper', name: '论文', seq: 30, type: dictType).save();
        new Dict(code: 'student-book', name: '学生用书', seq: 40, type: dictType).save();
        new Dict(code: 'teacher-book', name: '教师用书', seq: 50, type: dictType).save();
        new Dict(code: 'training-aid', name: '教具', seq: 60, type: dictType).save();
        new Dict(code: 'other', name: '其它', seq: 70, type: dictType).save();
    }

    void resAchieveCateDict() {
        def dictType = new DictType(id: 'res-achieve-cate', name: '成果类别').save()
        new Dict(code: 'c1-integrated-study', name: 'C1综合研究类', seq: 10, type: dictType).save();
        //C2教学研究类，细分：
        new Dict(code: 'c2-1-integrated-teaching', name: 'C2-1：教学综合', seq: 20, type: dictType).save();
        new Dict(code: 'c2-2-chinese', name: 'C2-2：语文', seq: 30, type: dictType).save();
        new Dict(code: 'c2-3-mathematics', name: 'C2-3：数学', seq: 40, type: dictType).save();
        new Dict(code: 'c2-4-english', name: 'C2-4：英语', seq: 50, type: dictType).save();
        new Dict(code: 'c2-5-science', name: 'C2-5：科学（物理、化学、生物、信息技术）', seq: 60, type: dictType).save();
        new Dict(code: 'c2-6-society', name: 'C2-6：社会（政治、历史、地理）', seq: 70, type: dictType).save();
        new Dict(code: 'c2-7-sport', name: 'C2-7：体育', seq: 80, type: dictType).save();
        new Dict(code: 'c2-8-music', name: 'C2-8：音乐', seq: 90, type: dictType).save();
        new Dict(code: 'c2-9-painting', name: 'C2-9：美术', seq: 100, type: dictType).save();
        new Dict(code: 'c3-course-study', name: 'C3课程研究类', seq: 110, type: dictType).save();
        new Dict(code: 'c4-moral-mental', name: 'C4德育、心理教育研究类', seq: 120, type: dictType).save();
        new Dict(code: 'd-preschool-edu', name: 'D幼儿教育类', seq: 130, type: dictType).save();
        new Dict(code: 'e-achieve-extension', name: 'E成果推广类', seq: 140, type: dictType).save();

    }

    /**
     * 立项、结题、参评都用这个状态字典
     */
    void applyStatusDict() {
        def dictType = new DictType(id: 'res-apply-status', name: '审核状态').save()

        new Dict(code: 'draft', name: '草稿', seq: 10, type: dictType).save();
        new Dict(code: 'wait', name: '待审核', seq: 20, type: dictType).save();
        new Dict(code: 'repair', name: '退回修改', seq: 20, type: dictType).save();
        new Dict(code: 'fail', name: '不通过', seq: 20, type: dictType).save();
        new Dict(code: 'pass', name: '通过', seq: 20, type: dictType).save();

    }

    void topicStatusDict() {
        def dictType = new DictType(id: 'res-topic-status', name: '课题状态').save()

        new Dict(code: 'created', name: '新建', seq: 10, type: dictType).save();
        new Dict(code: 'applied', name: '已立项', seq: 20, type: dictType).save();
        new Dict(code: 'finished', name: '已结题', seq: 40, type: dictType).save();
        new Dict(code: 'reviewed', name: '已参评', seq: 50, type: dictType).save();
    }

    void reviewTypeDict() {
        def dictType = new DictType(id: 'res-review-type', name: '评比类型').save()

        new Dict(code: 'topic', name: '课题成果评比', seq: 10, type: dictType).save();
        new Dict(code: 'paper', name: '论文评比', seq: 20, type: dictType).save();
    }

    /**
     * 暂不需要，根据评比申报是否截止，决定能否修改论文和成果的基本信息
     */
    void reviewStatusDict() {
        def dictType = new DictType(id: 'res-review-status', name: '评比状态').save()

        new Dict(code: 'created', name: '新建', seq: 10, type: dictType).save();
        new Dict(code: 'reviewing', name: '已评审', seq: 20, type: dictType).save();
        new Dict(code: 'reviewed', name: '评审完成', seq: 30, type: dictType).save();
    }

    void planStatusDict() {
        def dictType = new DictType(id: 'res-plan-status', name: '计划状态').save()

        new Dict(code: 'before', name: '未开始', seq: 10, type: dictType).save();
        new Dict(code: 'going', name: '材料上报', seq: 20, type: dictType).save();
        new Dict(code: 'done', name: '评分统计', seq: 30, type: dictType).save();
    }

    void projectLevelDict() {
        def dictType = new DictType(id: 'res-project-level', name: '立项情况').save()

        new Dict(code: 'state', name: '省级规划立项', seq: 10, type: dictType).save();
        new Dict(code: 'city', name: '市级规划立项', seq: 20, type: dictType).save();
        new Dict(code: 'district', name: '区级规划立项', seq: 30, type: dictType).save();
        new Dict(code: 'none', name: '无各级规划立项', seq: 40, type: dictType).save();
    }

    void avgAlgorithmDict() {
        def dictType = new DictType(id: 'res-avg-algorithm', name: '平均分算法').save()

        new Dict(code: 'normal', name: '全部分数平均', seq: 10, type: dictType).save();
        new Dict(code: 'ignore-max-min', name: '去除最高最低', seq: 20, type: dictType).save();
    }

    void runStatusDict() {
        def dictType = new DictType(id: 'res-run-status', name: '评分计算状态').save()

        new Dict(code: 'init', name: '新建', seq: 10, type: dictType).save();
        new Dict(code: 'running', name: '计算中', seq: 20, type: dictType).save();
        new Dict(code: 'success', name: '成功', seq: 30, type: dictType).save();
        new Dict(code: 'partial', name: '部分成功', seq: 40, type: dictType).save();
        new Dict(code: 'fail', name: '失败', seq: 50, type: dictType).save();
    }

}
