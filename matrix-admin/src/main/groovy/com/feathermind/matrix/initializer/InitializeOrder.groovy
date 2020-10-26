package com.feathermind.matrix.initializer

/**
 * Created by Neo on 2017-09-28.
 * 2020-10-26 Deprecated 改用flyway
 */
@Deprecated
interface InitializeOrder {
    int DOMAIN_INIT = 0
    int FIRST = 100
    int SECOND = 200
    int THIRD = 300
    int FOURTH = 400
}
