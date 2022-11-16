package com.feathermind.matrix.config

import com.feathermind.matrix.domain.sys.Param
import com.feathermind.matrix.domain.sys.ParamType
import com.feathermind.matrix.domain.sys.User
import com.feathermind.matrix.initializer.MatrixMigration
import com.feathermind.matrix.repositories.GeneralRepository
import grails.gorm.transactions.Transactional
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
@Slf4j
class MatrixMigrationRunner implements CommandLineRunner {
    @Autowired
    GeneralRepository generalRepository
    @Autowired
    ApplicationContext applicationContext

    @Transactional
    @Override
    void run(String... args) throws Exception {
        def version = Param.findByCode('MigrationVersion')
        if (!version) version = new Param(code: 'MigrationVersion', name: '数据库迁移版本',
                value: 'V000', type: ParamType.SYSTEM, validExp: '^V\\d+$', validDescribe: 'V+数字', lastUser: User.ADMIN)

        applicationContext.getBeansOfType(MatrixMigration)
                .sort { a, b -> a.key <=> b.key }
                .each { entry ->
                    if (entry.key > version.value) {
                        log.debug("$entry.key - 数据初始化开始")
                        version.value = entry.key;
                        entry.value.run();
                        log.debug("$entry.key - 数据初始化完成")
                    } else
                        log.debug("$entry.key <= $version.value , 数据初始化跳过")
                }
        version.save()
    }
}
