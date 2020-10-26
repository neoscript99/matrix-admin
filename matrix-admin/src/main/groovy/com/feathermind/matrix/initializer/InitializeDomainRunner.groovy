package com.feathermind.matrix.initializer

import com.feathermind.matrix.repositories.GeneralRepository
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.grails.datastore.mapping.core.Datastore
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.Environment

import java.lang.reflect.Field

/**
 * InitializeDomain方式数据初始化
 * <p> InitializeDomain方式为最高优先级，所有DataInitializer方式的数据初始化优先级必须低于本类
 * <p> 通过命令行参数"--init"执行初始化，同时如果传入profiles执行对应的多套初始化方案，
 * <p> 命令行如果未传profiles，默认为[default],也就是注解默认值
 * Created by Neo on 2017-09-27.
 * @see InitializeDomain
 * 2020-10-26 改用flyway调用
 */
@CompileStatic
@Slf4j
class InitializeDomainRunner {
    ApplicationContext applicationContext
    GeneralRepository generalRepository
    Environment environment

    InitializeDomainRunner(ApplicationContext applicationContext, GeneralRepository generalRepository, Environment environment) {
        this.applicationContext = applicationContext
        this.generalRepository = generalRepository
        this.environment = environment
    }

    void run() {
        def profiles = new HashSet(['default'])
        profiles.addAll(environment.getActiveProfiles())
        initStaticList(profiles)
    }

    /**
     * 初始化InitializeDomain注解的domain类
     * @see InitializeDomain
     */
    void initStaticList(Set profiles) {
        log.info("initialize system with StaticList profiles: $profiles")
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        Set doneSet = new HashSet()
        //遍历entity类，执行InitializeDomain初始化
        configurableApplicationContext.getBeansOfType(Datastore).each { String key, Datastore datastore ->
            datastore.getMappingContext().getPersistentEntities().each {
                initDomain(it.javaClass, doneSet, profiles)
            }
        }
    }

    /**
     * 根据InitializeDomain的profiles配置，决定是否进行初始化
     * @param domain
     * @param doneSet
     * @param profiles
     */
    void initDomain(Class domain, Set doneSet, Set profiles) {
        if (doneSet.contains(domain))
            return;
        log.info('initDomain: {}', domain)
        InitializeDomain initializeDomain = domain.getAnnotation(InitializeDomain.class)
        //执行条件：1、是否注解InitializeDomain；2、profiles和命令行参数是否匹配（命令行如果未传profiles，默认为[default],也就是注解默认值）
        if (initializeDomain && initializeDomain.profiles().any { profiles.contains(it) }) {
            log.debug("$domain 初始化开始")
            //如果有依赖，先处理依赖类
            initializeDomain.depends().each { initDomain(it, doneSet, profiles) }
            //表数据为空
            if (!generalRepository.countAll(domain)) {
                Field initField = domain.getDeclaredField(initializeDomain.value())
                initField.setAccessible(true)
                initField.get(domain).each { generalRepository.saveEntity(it) }
            }
            log.debug("$domain 初始化完成")
        }
        doneSet.add(domain)
    }
}
