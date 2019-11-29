package com.feathermind.matrix.util


import groovy.transform.CompileStatic

@CompileStatic
class TokenHolder {

    /**
     * ThreadLocal to hold the Token for Threads to access.
     */
    private static final ThreadLocal<com.feathermind.matrix.domain.sys.Token> threadLocal = new ThreadLocal<com.feathermind.matrix.domain.sys.Token>();

    /**
     * Retrieve the assertion from the ThreadLocal.
     *
     * @return the Asssertion associated with this thread.
     */
    public static com.feathermind.matrix.domain.sys.Token getToken() {
        return threadLocal.get();
    }

    /**
     * Add the Token to the ThreadLocal.
     *
     * @param assertion the assertion to add.
     */
    public static void setToken(final com.feathermind.matrix.domain.sys.Token token) {
        threadLocal.set(token);
    }

    /**
     * Clear the ThreadLocal.
     */
    public static void clear() {
        threadLocal.set(null);
    }
}
