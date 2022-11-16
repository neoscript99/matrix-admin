package com.feathermind.matrix.initializer

import com.feathermind.matrix.repositories.GeneralRepository
import grails.gorm.transactions.Transactional
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired

/**
 * Created by Neo on 2017-08-25.
 */
@Slf4j
abstract class MatrixMigration {
    @Autowired
    GeneralRepository generalRepository

    @Transactional
    abstract void run();

    def save(def entity) {
        generalRepository.saveEntity(entity)
    }

    def saveList(List entityList) {
        entityList.each { save(it) }
    }
}
