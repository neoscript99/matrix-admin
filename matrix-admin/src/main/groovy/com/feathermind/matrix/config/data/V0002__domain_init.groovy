package com.feathermind.matrix.config.data

import com.feathermind.matrix.initializer.InitializeDomainRunner
import com.feathermind.matrix.initializer.MatrixFlywayMigration
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

import java.lang.reflect.Field

@Component
@Slf4j
class V0002__domain_init extends MatrixFlywayMigration {

    @Autowired
    ApplicationContext applicationContext
    @Autowired
    Environment environment

    @Override
    void run() {
        new InitializeDomainRunner(applicationContext, generalRepository, environment).run();
    }
}
