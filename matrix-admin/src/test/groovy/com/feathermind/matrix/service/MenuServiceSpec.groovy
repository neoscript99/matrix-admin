package com.feathermind.matrix.service

import com.feathermind.matrix.config.data.V0003__menu_init
import com.feathermind.matrix.domain.sys.Menu
import com.feathermind.matrix.repositories.GeneralRepository
import org.grails.datastore.gorm.GormEntity
import spock.lang.Shared
import spock.lang.Specification

class MenuServiceSpec extends Specification {
    @Shared
    GeneralRepository generalRepository = Mock(GeneralRepository)
    @Shared
    def menuList = []

    def setupSpec() {
        generalRepository.countAll(_) >> 0
        generalRepository.saveEntity(_) >> { GormEntity entity ->
            entity.id = UUID.randomUUID().toString()
            if (Menu.isAssignableFrom(entity.class))
                menuList << entity
            return entity
        }
        generalRepository.get(_, _) >> { Class cls, String id ->
            return menuList.find { it.id == id }
        }

        V0003__menu_init menuInitializer = new V0003__menu_init(generalRepository: generalRepository)
        menuInitializer.run()
    }

    def 'menu tree test'() {
        given:

        def menuService = new MenuService(generalRepository: generalRepository)
        def tree = menuService.generateTree(menuList)
        println(tree)

        expect:
        tree.subMenus.size() == 4
    }
}
