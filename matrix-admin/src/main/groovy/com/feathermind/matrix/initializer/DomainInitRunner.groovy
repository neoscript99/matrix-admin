package com.feathermind.matrix.initializer

import com.feathermind.matrix.repositories.GeneralRepository
import org.springframework.transaction.annotation.Transactional
import groovy.util.logging.Slf4j
import org.grails.datastore.mapping.core.Datastore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

import java.lang.reflect.Field

/**
 * InitializeDomain方式数据初始化
 * <p> InitializeDomain方式为最高优先级，所有DataInitializer方式的数据初始化优先级必须低于本类
 * <p> 通过命令行参数"--init"执行初始化，同时如果传入profiles执行对应的多套初始化方案，
 * <p> 命令行如果未传profiles，默认为[default],也就是注解默认值
 * Created by Neo on 2017-09-27.
 * @see InitializeDomain* 2020-10-26 Deprecated 改用flyway
 */
@Deprecated
@Order(InitializeOrder.DOMAIN_INIT)
@Slf4j
class DomainInitRunner implements CommandLineRunner {

    @Autowired
    ApplicationContext applicationContext
    @Autowired
    GeneralRepository generalRepository
    @Autowired
    Environment environment
    @Autowired
    ApplicationArguments applicationArguments

    @Override
    @Transactional
    void run(String... args) throws Exception {
        if (applicationArguments.containsOption('init') || System.getProperty('init') != null) {
            new InitializeDomainRunner(applicationContext, generalRepository, environment).run();
        }
    }
}
