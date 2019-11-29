package com.feathermind.matrix

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class MatrixAdminApp {

    static void main(String[] args) {
        SpringApplication.run(MatrixAdminApp.class, args);
    }
}
