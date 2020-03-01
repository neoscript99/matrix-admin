package com.feathermind.research.config.dev

import com.feathermind.matrix.domain.sys.AttachmentInfo
import com.feathermind.matrix.domain.sys.UserRole
import com.feathermind.matrix.initializer.AbstractDataInitializer
import com.feathermind.matrix.initializer.DataInitializer
import com.feathermind.research.config.common.InitEntity
import com.feathermind.research.config.common.ResUserInitializer
import com.feathermind.research.domain.res.*
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.springframework.core.annotation.Order

import static com.feathermind.research.domain.res.ResDept.*

@Order(200)
@CompileStatic(TypeCheckingMode.SKIP)
class ResDevUserInitializer extends AbstractDataInitializer implements DataInitializer {

    static final String DEFAULT_PASS = ResUserInitializer.DEFAULT_PASS
    List<ReviewRoundExpert> roundExpertList
    List<ResUser> expertList

    @Override
    boolean isInited() {
        return ResUser.findByAccount('dept-admin')
    }

    @Override
    void doInit() {
        initUser()
        initExpert()
        initReviewRoundExpert()
        initPaper()
    }


    static ResUser demoSchoolUser = new ResUser(name: '单位管理员', account: 'dept-admin', cellPhone: '88888888', dept: demoSchool, password: DEFAULT_PASS)
    static ResUser expertUser = new ResUser(name: '测试专家01', account: 'expert', dept: expertDept, password: DEFAULT_PASS)

    def initUser() {
        demoSchoolUser.save()
        new UserRole(demoSchoolUser, InitEntity.DEPT_MANAGER).save()
        new UserRole(demoSchoolUser, InitEntity.RES_USER).save()
        initDemoSchoolMember()
    }

    void initExpert() {
        expertList = [expertUser,
                      new ResUser(name: '测试专家02', account: 'expert02', dept: expertDept, password: DEFAULT_PASS),
                      new ResUser(name: '测试专家03', account: 'expert03', dept: expertDept, password: DEFAULT_PASS),
                      new ResUser(name: '测试专家04', account: 'expert04', dept: expertDept, password: DEFAULT_PASS),
                      new ResUser(name: '测试专家05', account: 'expert05', dept: expertDept, password: DEFAULT_PASS)
        ]
        expertList.each {
            it.save()
            new UserRole(it, InitEntity.EXPERT).save()
        }
    }

    def initDemoSchoolMember() {
        new ResUser(name: '成员1', account: '高级中学管理员01', idCard: '333000199012126661', cellPhone: '88888888', dept: demoSchool, password: DEFAULT_PASS).save()
        new ResUser(name: '成员2', account: '高级中学管理员02', idCard: '333000199012126662', cellPhone: '88888888', dept: demoSchool, password: DEFAULT_PASS).save()
        new ResUser(name: '成员3', account: '高级中学管理员03', idCard: '333000199012126663', cellPhone: '88888888', dept: demoSchool, password: DEFAULT_PASS).save()
    }

    void initReviewRoundExpert() {
        roundExpertList = expertList.collect {
            new ReviewRoundExpert(round: ReviewRound.DemoPaperRound, expert: it, seq: 10).save()
        }
    }

    def initPaper() {
        def fileInfo = AttachmentInfo.DemoFileInfo
        def list = [new AchievePaper(name: '论文001', personInCharge: demoSchoolUser,
                reviewPlan: ReviewPlan.DemoPaperPlan, dept: demoSchool, paperFile: fileInfo),
                    new AchievePaper(name: '论文002', personInCharge: demoSchoolUser,
                            reviewPlan: ReviewPlan.DemoPaperPlan, dept: demoSchool, paperFile: fileInfo),
                    new AchievePaper(name: '论文003', personInCharge: demoSchoolUser,
                            reviewPlan: ReviewPlan.DemoPaperPlan, dept: demoSchool, paperFile: fileInfo),
                    new AchievePaper(name: '论文004', personInCharge: demoSchoolUser,
                            reviewPlan: ReviewPlan.DemoPaperPlan, dept: demoSchool, paperFile: fileInfo),
                    new AchievePaper(name: '论文005', personInCharge: demoSchoolUser,
                            reviewPlan: ReviewPlan.DemoPaperPlan, dept: demoSchool, paperFile: fileInfo),
                    new AchievePaper(name: '论文006', personInCharge: demoSchoolUser,
                            reviewPlan: ReviewPlan.DemoPaperPlan, dept: demoSchool, paperFile: fileInfo),
                    new AchievePaper(name: '论文007', personInCharge: demoSchoolUser,
                            reviewPlan: ReviewPlan.DemoPaperPlan, dept: demoSchool, paperFile: fileInfo)]
        list.eachWithIndex { paper, idx1 ->
            paper.save()
            roundExpertList.eachWithIndex { exp, idx2 ->
                def score = Math.random() * 100
                new AchieveExpertScore(achieve: paper, roundExpert: exp, score: score).save()
            }
        }
        return list
    }
}
