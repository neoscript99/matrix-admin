package com.feathermind.matrix.util

import cn.hutool.core.date.DateField

import static cn.hutool.core.date.DateUtil.*

class DateUtil {
    public static String dayStr(int offsets = 0) {
        return formatDate(offset(date(), DateField.DAY_OF_YEAR, offsets));
    }
}
