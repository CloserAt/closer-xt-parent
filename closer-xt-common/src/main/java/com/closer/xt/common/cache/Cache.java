package com.closer.xt.common.cache;

import java.lang.annotation.*;

//aop方式实现统一缓存处理，无需加在每个接口上
//统一缓存处理的原因是因为内存的访问速度远远大于硬盘的访问速度
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
    /**
     * 过期时间 默认60s
     * @return
     */
    int time() default 1 * 60 * 1000;

    /**
     * 缓存前缀名称
     * @return
     */
    String name() default "";

    /**
     * 是否需要当前的用户作为key的一部分
     * @return
     */
    boolean hasUser() default false;
}
