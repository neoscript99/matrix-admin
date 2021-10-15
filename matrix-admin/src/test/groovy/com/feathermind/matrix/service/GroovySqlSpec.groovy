package com.feathermind.matrix.service

import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.bean.copier.CopyOptions
import com.feathermind.matrix.MatrixAdminApp
import groovy.sql.Sql
import groovy.transform.ToString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

import javax.sql.DataSource

import static org.junit.jupiter.api.Assertions.*

@ToString(includePackage = false, includeNames = true)
class MenuBean {
    String parentId
    String label
    Integer countChild
}

@SpringBootTest(classes = MatrixAdminApp.class)
@ActiveProfiles('dev')
class GroovySqlSpec {
    @Autowired
    DataSource dataSource

    @Test
    void testGroup() {
        def sql = new Sql(dataSource)
        def str = """SELECT m1.parent_id, m2.label, count(9) count_child
                        FROM SYS_MENU m1,
                             sys_menu m2
                        where m1.parent_id = m2.id
                          and m1.seq >= :seq
                        group by m1.parent_id, m2.label
                        """;
        def res = sql.rows(str, [seq: 0])
        assertTrue(res.size() > 0)

        res.each {
            def bean = new MenuBean()
            BeanUtil.fillBeanWithMap(it, bean, true, CopyOptions.create().ignoreCase().ignoreError())
            //println(it)
            println(bean)
        }
    }

    @Test
    void testSelect() {
        def sql = new Sql(dataSource)
        def str = """select * from sys_user u
                      where u.account is not null""";
        def res = sql.rows(str)
        assertTrue(res.size() > 0)
        println(res)
    }


}
