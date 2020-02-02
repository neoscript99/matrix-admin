package com.feathermind.research.config.dev

import com.feathermind.matrix.initializer.AbstractDataInitializerRunner
import com.feathermind.matrix.initializer.InitializeOrder
import groovy.transform.CompileStatic
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@CompileStatic
@Order(InitializeOrder.THIRD)
@Profile('dev')
class ResDevInitRunner extends AbstractDataInitializerRunner {
}
