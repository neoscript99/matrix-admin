package com.feathermind.matrix

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * 创建spring boot自动配置
 */
@Configuration
@ComponentScan
@EnableScheduling
class MatrixAdminAutoConfiguration {
}
