package com.feathermind.research.config.common

import com.feathermind.matrix.initializer.AbstractDataInitializerRunner
import com.feathermind.matrix.initializer.InitializeOrder
import groovy.transform.CompileStatic
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@CompileStatic
@Order(InitializeOrder.SECOND)
class ResCommonInitRunner  extends AbstractDataInitializerRunner {
}
