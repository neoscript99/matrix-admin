package com.feathermind.matrix.security

import groovy.transform.CompileStatic

@CompileStatic
class TokenHolder {

    /**
     * ThreadLocal to hold the Token for Threads to access.
     */
    private static final ThreadLocal<TokenDetails> threadLocal = new ThreadLocal<TokenDetails>();

    /**
     * Retrieve the assertion from the ThreadLocal.
     *
     * @return the Asssertion associated with this thread.
     */
    public static TokenDetails getToken() {
        return threadLocal.get();
    }

    /**
     * Add the Token to the ThreadLocal.
     *
     * @param assertion the assertion to add.
     */
    public static void setToken(final TokenDetails token) {
        threadLocal.set(token);
    }

    /**
     * Clear the ThreadLocal.
     */
    public static void clear() {
        threadLocal.set(null);
    }
}
