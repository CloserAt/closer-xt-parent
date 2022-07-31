package com.closer.xt.common.Login;

public class UserThreadLocal {

    //作为线程变量隔离的
    private static final ThreadLocal<Long> LOCAL = new ThreadLocal<>();

    public static void put(Long userId) {
        LOCAL.set(userId);
    }

    public static Long get() {
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }
}
