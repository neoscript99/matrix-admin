package com.feathermind.matrix.util

import cn.hutool.crypto.digest.DigestUtil
import java.nio.charset.StandardCharsets

/**
 * Functions
 * @since Dec 22, 2010
 * @author wangchu
 */
class EncoderUtil {
    /**
     * @param inputString
     * @return encoded string
     */
    static public String md5(String inputString) {
        return DigestUtil.md5Hex(inputString.getBytes(StandardCharsets.UTF_8))
    }

    static public String sha256(String inputString) {
        return sha256(inputString.getBytes(StandardCharsets.UTF_8))
    }

    static public String sha256(byte[] bytes) {
        return DigestUtil.sha256Hex(bytes)
    }
}
