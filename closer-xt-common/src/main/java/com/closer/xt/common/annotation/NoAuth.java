package com.closer.xt.common.annotation;

import java.lang.annotation.*;
//需要登陆信息，但是未登录，不进行拦截
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoAuth {
}
