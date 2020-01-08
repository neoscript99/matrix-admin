package com.feathermind.research.domain.res

import com.feathermind.matrix.domain.sys.Department
import com.feathermind.matrix.initializer.InitializeDomain
import grails.gorm.annotation.Entity
import groovy.transform.ToString
import static ResDeptType.*
import static com.feathermind.research.domain.res.ResDeptType.compulsory
import static com.feathermind.research.domain.res.ResDeptType.directAgency

/**
 * 鄞州教育局单位信息
 */
@Entity
@ToString(includePackage = false, includeNames = true, includes = 'name,contact')
@InitializeDomain(depends = ResDeptType)
class ResDept extends Department {

    //联系人
    ResDeptType type
    String contact
    String telephone
    /*String cellphone
    String shortDial
    String email*/

    Integer classNumber
    //每个立项计划单位默认申报数
    Integer defaultApplyNum = 3
    //备注
    String note
    Date dateCreated
    Date lastUpdated

    static mapping = {
        type fetch: 'join', lazy: false
    }
    static constraints = {
        contact nullable: true
        telephone nullable: true
        classNumber nullable: true
        note nullable: true, maxSize: 256
    }


    static ResDept headDept = new ResDept(name: '教育科学研究室', seq: 0, contact: '鲍老师', telephone: '88121117', type: directAgency)
    static ResDept demoSchool = new ResDept(name: '鄞州区青少年宫', seq: 100, defaultApplyNum: 3, type: directAgency)
    static ResDept expertDept = new ResDept(name: '专家组', seq: 1, contact: '专家', telephone: '88888888', type: externalAgency)

    static initList = [headDept, expertDept, demoSchool,
                       new ResDept(name: '鄞州区特殊教育中心', classNumber: 14, seq: 101, defaultApplyNum: 3, type: directAgency),
                       new ResDept(name: '宁波艺术实验学校', classNumber: 48, seq: 102, defaultApplyNum: 3, type: compulsory),
                       new ResDept(name: '宁波市新城第一实验学校', classNumber: 68, seq: 103, defaultApplyNum: 3, type: compulsory),
                       new ResDept(name: '宁波市第七中学', classNumber: 54, seq: 104, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '宁波市曙光中学', classNumber: 62, seq: 105, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '宁波四眼碶中学', classNumber: 34, seq: 106, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '宁波市春晓中学', classNumber: 29, seq: 107, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '鄞州实验中学', classNumber: 36, seq: 108, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '鄞州区中河街道宋诏桥初级中学', classNumber: 31, seq: 109, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '鄞州区钟公庙街道中心初级中学', classNumber: 29, seq: 110, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '鄞州蓝青学校', classNumber: 26, seq: 111, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '鄞州区瞻岐镇中心初级中学', classNumber: 15, seq: 112, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '鄞州区咸祥镇中心初级中学', classNumber: 12, seq: 113, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '鄞州区东吴镇中心初级中学', classNumber: 12, seq: 114, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '鄞州区塘溪镇中心初级中学', classNumber: 18, seq: 115, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '宁波逸夫中学', classNumber: 27, seq: 116, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '鄞州区董玉娣中学', classNumber: 17, seq: 117, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '鄞州区邱隘实验中学', classNumber: 18, seq: 118, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '鄞州区云龙镇中心初级中学', classNumber: 30, seq: 119, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '鄞州区横溪镇中心初级中学', classNumber: 20, seq: 120, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '鄞州区姜山镇中心初级中学', classNumber: 55, seq: 121, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '鄞州区潘火实验中学', classNumber: 10, seq: 122, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '鄞州区李关弟中学', classNumber: 28, seq: 123, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '鄞州区首南街道中心初级中学', classNumber: 27, seq: 124, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '东钱湖中学', classNumber: 31, seq: 125, defaultApplyNum: 3, type: middleSchool),
                       new ResDept(name: '鄞州区江东中心小学', classNumber: 48, seq: 126, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '宁波市曙光小学', classNumber: 18, seq: 127, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '宁波市通途小学', classNumber: 40, seq: 128, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '宁波市四眼碶小学', classNumber: 60, seq: 129, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '宁波市栎木小学', classNumber: 18, seq: 130, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '宁波市荷花庄小学', classNumber: 18, seq: 131, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '宁波市镇安小学', classNumber: 42, seq: 132, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '宁波市白鹤小学', classNumber: 17, seq: 133, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '宁波市黄鹂小学', classNumber: 18, seq: 134, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '宁波市李惠利小学', classNumber: 18, seq: 135, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '宁波市东柳小学', classNumber: 12, seq: 136, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区第二实验小学', classNumber: 72, seq: 137, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区江东外国语小学', classNumber: 42, seq: 138, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区中河街道宋诏桥小学', classNumber: 57, seq: 139, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区堇山小学', classNumber: 63, seq: 140, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区华泰小学', classNumber: 54, seq: 141, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区东湖小学', classNumber: 33, seq: 142, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区中河实验小学', classNumber: 26, seq: 143, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区钟公庙街道中心小学', classNumber: 42, seq: 144, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区钟公庙实验小学', classNumber: 24, seq: 145, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区惠风书院', classNumber: 49, seq: 146, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州蓝青小学', classNumber: 16, seq: 147, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区瞻岐镇中心小学', classNumber: 27, seq: 148, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区瞻岐镇大嵩小学', classNumber: 10, seq: 149, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区咸祥镇中心小学', classNumber: 23, seq: 150, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区东吴镇中心小学', classNumber: 24, seq: 151, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区东吴镇天童小学', classNumber: 8, seq: 152, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区塘溪镇中心小学', classNumber: 20, seq: 153, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区塘溪镇第二中心小学', classNumber: 17, seq: 154, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区五乡镇中心小学', classNumber: 26, seq: 155, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '宁波逸夫小学', classNumber: 19, seq: 156, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区五乡镇贵玉小学', classNumber: 30, seq: 157, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区邱隘镇中心小学', classNumber: 35, seq: 158, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区邱隘实验小学', classNumber: 27, seq: 159, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区邱隘镇汇头小学', classNumber: 6, seq: 160, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区邱隘镇回龙学校', classNumber: 6, seq: 161, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区邱隘镇渔金小学', classNumber: 6, seq: 162, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区明湖学校', seq: 163, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区邱隘镇东新学校', classNumber: 31, seq: 164, defaultApplyNum: 3, note: '其中小学24 初中6', type: primarySchool),
                       new ResDept(name: '鄞州区云龙镇王笙舲小学', classNumber: 21, seq: 165, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区云龙镇甲南小学', classNumber: 32, seq: 166, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区云龙镇石桥小学', classNumber: 10, seq: 167, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区云龙镇前徐小学', classNumber: 11, seq: 168, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区横溪镇中心小学', classNumber: 43, seq: 169, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区横溪镇金峨小学', classNumber: 8, seq: 170, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区实验小学', classNumber: 84, seq: 171, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区姜山镇朝阳小学', classNumber: 19, seq: 172, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区姜山镇茅山小学', classNumber: 22, seq: 173, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区姜山镇丽水小学', classNumber: 14, seq: 174, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区姜山镇陈家团小学', classNumber: 8, seq: 175, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区姜山镇培本小学', classNumber: 6, seq: 176, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区姜山镇朝阳学校', classNumber: 13, seq: 177, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区潘火街道东南小学', classNumber: 40, seq: 178, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区潘火街道德培小学', classNumber: 46, seq: 179, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区潘火街道花园学校', classNumber: 30, seq: 180, defaultApplyNum: 3, note: '其中小学24 初中6', type: primarySchool),
                       new ResDept(name: '鄞州区下应中心小学', classNumber: 34, seq: 181, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区下应街道中海小学', classNumber: 22, seq: 182, defaultApplyNum: 3, note: '其中小学8 初中6', type: primarySchool),
                       new ResDept(name: '鄞州区下应街道尚正学校', classNumber: 14, seq: 183, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区首南第一小学', classNumber: 39, seq: 184, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区首南街道学士小学', classNumber: 13, seq: 185, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区首南泰安学校', classNumber: 19, seq: 186, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区首南街道蔡家小学', classNumber: 12, seq: 187, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '东钱湖中心小学', classNumber: 40, seq: 188, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '东钱湖钱湖人家小学', classNumber: 21, seq: 189, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '东钱湖高钱小学', classNumber: 12, seq: 190, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '东钱湖韩岭小学', classNumber: 6, seq: 191, defaultApplyNum: 3, type: primarySchool),
                       new ResDept(name: '鄞州区城北成人文化技术学校', seq: 192, defaultApplyNum: 3, type: adultSchool),
                       new ResDept(name: '鄞州区城南成人文化技术学校', seq: 193, defaultApplyNum: 3, type: adultSchool),
                       new ResDept(name: '鄞州区瞻岐镇成人文化技术学校', seq: 194, defaultApplyNum: 3, type: adultSchool),
                       new ResDept(name: '鄞州区咸祥镇成人文化技术学校', seq: 195, defaultApplyNum: 3, type: adultSchool),
                       new ResDept(name: '鄞州区东吴镇成人文化技术学校', seq: 196, defaultApplyNum: 3, type: adultSchool),
                       new ResDept(name: '鄞州区塘溪镇成人文化技术学校', seq: 197, defaultApplyNum: 3, type: adultSchool),
                       new ResDept(name: '鄞州区五乡镇成人文化技术学校', seq: 198, defaultApplyNum: 3, type: adultSchool),
                       new ResDept(name: '鄞州区邱隘镇成人文化技术学校', seq: 199, defaultApplyNum: 3, type: adultSchool),
                       new ResDept(name: '鄞州区云龙镇成人文化技术学校', seq: 200, defaultApplyNum: 3, type: adultSchool),
                       new ResDept(name: '鄞州区横溪镇成人文化技术学校', seq: 201, defaultApplyNum: 3, type: adultSchool),
                       new ResDept(name: '鄞州区姜山镇成人文化技术学校', seq: 202, defaultApplyNum: 3, type: adultSchool),
                       new ResDept(name: '东钱湖成人文化技术学校', seq: 203, defaultApplyNum: 3, type: adultSchool),
                       new ResDept(name: '鄞州区江东中心幼儿园', classNumber: 37, seq: 204, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '鄞州区江东实验幼儿园', classNumber: 33, seq: 205, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '宁波市第三幼儿园', classNumber: 24, seq: 206, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '宁波市李惠利幼儿园', classNumber: 30, seq: 207, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '宁波市明楼幼儿园', classNumber: 21, seq: 208, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '宁波市华光幼儿园', classNumber: 9, seq: 209, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '宁波市常青藤幼儿园', classNumber: 15, seq: 210, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '鄞州区江东外国语小学附属幼儿园', classNumber: 9, seq: 211, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '宁波市乐源幼儿园', classNumber: 18, seq: 212, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '宁波市新韵太古幼儿园', classNumber: 9, seq: 213, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '宁波市鄞州区托幼实验园', classNumber: 19, seq: 214, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '宁波市新城第一幼儿园', classNumber: 21, seq: 215, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '宁波市东部新城中心幼儿园', classNumber: 11, seq: 216, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '宁波市市级机关第二幼儿园', classNumber: 21, seq: 217, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '鄞州区实验幼儿园', classNumber: 27, seq: 218, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '鄞州区中河街道中心幼儿园', classNumber: 25, seq: 219, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '鄞州区钟公庙街道中心幼儿园', classNumber: 27, seq: 220, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '鄞州区瞻岐镇中心幼儿园', classNumber: 16, seq: 221, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '鄞州区咸祥镇中心幼儿园', classNumber: 17, seq: 222, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '鄞州区东吴镇中心幼儿园', classNumber: 18, seq: 223, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '鄞州区塘溪镇中心幼儿园', classNumber: 9, seq: 224, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '鄞州区五乡镇中心幼儿园', classNumber: 24, seq: 225, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '鄞州区邱隘镇明湖幼儿园', classNumber: 9, seq: 226, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '鄞州区邱隘镇中心幼儿园', classNumber: 28, seq: 227, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '鄞州区云龙镇中心幼儿园', classNumber: 18, seq: 228, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '鄞州区横溪镇中心幼儿园', classNumber: 15, seq: 229, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '鄞州区姜山幼儿园', classNumber: 52, seq: 230, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '鄞州区潘火街道中心幼儿园', classNumber: 30, seq: 231, defaultApplyNum: 3, note: '其中殷家园 9个 世纪园9个 中心园12个', type: centralPreschool),
                       new ResDept(name: '鄞州区下应街道中心幼儿园', classNumber: 15, seq: 232, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '鄞州区首南街道中心幼儿园', classNumber: 21, seq: 233, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '东钱湖中心幼儿园', classNumber: 12, seq: 234, defaultApplyNum: 3, type: centralPreschool),
                       new ResDept(name: '鄞州区中河街道飞虹幼儿园', classNumber: 10, seq: 235, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区中河街道彩虹幼儿园', classNumber: 8, seq: 236, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区中河街道东裕幼儿园', classNumber: 12, seq: 237, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区中河街道东湖馨园幼儿园', classNumber: 9, seq: 238, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区中河街道格兰云天幼儿园', classNumber: 12, seq: 239, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区中河街道华泰剑桥幼儿园', classNumber: 11, seq: 240, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区中河街道荣安和院幼儿园', classNumber: 9, seq: 241, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区中河街道春江花城幼儿园', classNumber: 12, seq: 242, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区中河街道城市花园幼儿园', classNumber: 15, seq: 243, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区中河街道金湾华庭幼儿园', classNumber: 13, seq: 244, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区中河街道东城幼儿园', classNumber: 9, seq: 245, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区钟公庙街道实验幼儿园', classNumber: 18, seq: 246, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区钟公庙街道金家漕幼儿园', classNumber: 20, seq: 247, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区钟公庙街道金地幼儿园', classNumber: 10, seq: 248, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区钟公庙街道金色幼儿园', classNumber: 12, seq: 249, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区钟公庙街道泰安幼儿园', classNumber: 19, seq: 250, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区瞻岐镇大嵩幼儿园', classNumber: 3, seq: 251, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区瞻岐镇合岙幼儿园', classNumber: 7, seq: 252, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区东吴镇凌云幼儿园', classNumber: 4, seq: 253, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区滨海幼儿园', classNumber: 9, seq: 254, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区五乡镇宝幢幼儿园', classNumber: 9, seq: 255, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区云龙镇甲南幼儿园', classNumber: 12, seq: 256, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区云龙镇荻江幼儿园', classNumber: 12, seq: 257, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区姜山镇小城春秋幼儿园', classNumber: 16, seq: 258, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区姜山镇茅山幼儿园', classNumber: 12, seq: 259, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区姜山镇朝阳幼儿园', classNumber: 14, seq: 260, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区姜山镇丽水幼儿园', classNumber: 9, seq: 261, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区潘火街道东莺幼儿园', classNumber: 20, seq: 262, defaultApplyNum: 3, note: '总园+分园', type: publicPreschool),
                       new ResDept(name: '鄞州区潘火街道德培幼儿园', classNumber: 17, seq: 263, defaultApplyNum: 3, note: '金桥12 香园5', type: publicPreschool),
                       new ResDept(name: '鄞州区潘火街道东杰幼儿园', classNumber: 16, seq: 264, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区潘火街道紫郡幼儿园', classNumber: 19, seq: 265, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区潘火街道上上城幼儿园', classNumber: 15, seq: 266, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区潘火街道格兰晴天幼儿园', classNumber: 12, seq: 267, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区潘火殷家花园幼儿园', classNumber: 9, seq: 268, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区下应街道东方湾邸幼儿园', classNumber: 9, seq: 269, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区下应街道中海幼儿园', classNumber: 15, seq: 270, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区下应街道天宫幼儿园', classNumber: 11, seq: 271, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区首南街道学府实验幼儿园', classNumber: 10, seq: 272, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区首南街道学府1号幼儿园', classNumber: 6, seq: 273, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区首南街道荣安琴湾幼儿园', classNumber: 9, seq: 274, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区首南街道雍城世家幼儿园', classNumber: 12, seq: 275, defaultApplyNum: 3, note: '分园2个', type: publicPreschool),
                       new ResDept(name: '鄞州区宝韵荣安幼儿园', classNumber: 9, seq: 276, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区首南街道句章幼儿园', classNumber: 10, seq: 277, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区江东国际幼儿园', classNumber: 11, seq: 278, defaultApplyNum: 3, type: publicPreschool),
                       new ResDept(name: '鄞州区中河街道绿茵东湖幼儿园', classNumber: 13, seq: 279, defaultApplyNum: 3, type: privatePreschool),
                       new ResDept(name: '鄞州区中河街道宋诏桥幼儿园', classNumber: 10, seq: 280, defaultApplyNum: 3, type: privatePreschool),
                       new ResDept(name: '鄞州区钟公庙绿茵君玺府幼儿园', classNumber: 4, seq: 281, defaultApplyNum: 3, type: privatePreschool),
                       new ResDept(name: '鄞州区朝晖实验学校', classNumber: 32, seq: 282, defaultApplyNum: 3, note: '电子学籍36 小学26 初中6', type: privateSchool),
                       new ResDept(name: '鄞州区中河街道春晖学校', classNumber: 29, seq: 283, defaultApplyNum: 3, note: '小学21 中学8 ', type: privateSchool),
                       new ResDept(name: '鄞州区钟公庙长丰学校', classNumber: 25, seq: 284, defaultApplyNum: 3, note: ' 小学9+12 初中4', type: privateSchool),
                       new ResDept(name: '鄞州赫德实验学校', seq: 285, defaultApplyNum: 3, type: privateSchool),
                       new ResDept(name: '鄞州四明山经典学校', seq: 286, defaultApplyNum: 3, type: privateSchool),
                       new ResDept(name: '宁波华茂外国语学校', seq: 287, defaultApplyNum: 3, type: privateSchool),


    ]
}
