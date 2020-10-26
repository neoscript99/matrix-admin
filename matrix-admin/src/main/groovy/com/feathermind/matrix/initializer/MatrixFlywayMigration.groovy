package com.feathermind.matrix.initializer

import com.feathermind.matrix.repositories.GeneralRepository
import groovy.transform.CompileStatic
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@CompileStatic
abstract class MatrixFlywayMigration extends BaseJavaMigration {
    @Autowired
    GeneralRepository generalRepository

    @Override
    @Transactional
    void migrate(Context context) throws Exception { run(); }

    abstract void run();

    def save(def entity) {
        generalRepository.saveEntity(entity)
    }

    def saveList(List entityList) {
        entityList.each { save(it) }
    }
}
