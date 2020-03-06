package com.feathermind.matrix.controller.bean

import com.feathermind.matrix.util.JsonUtil

class ResBean {
    ResBean() {
        success = true
    }

    ResBean(String code, String error) {
        success = false
        this.code = code
        this.error = error
    }
    Boolean success
    String code
    String error

    String toString() {
        JsonUtil.toJson(this)
    }

    static String json(String code, Object error) {
        new ResBean(code, error.toString()).toString()
    }
}
