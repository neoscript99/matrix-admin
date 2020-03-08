package com.feathermind.matrix.util

import groovy.transform.CompileStatic

@CompileStatic
class MatrixException extends RuntimeException {
    String errorCode

    MatrixException(String errorCode, Object message) {
        super(message.toString())
        this.errorCode = errorCode
    }

    MatrixException(String errorCode, Object message, Throwable cause) {
        super(message.toString(), cause)
        this.errorCode = errorCode
    }

}
