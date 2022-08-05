package com.closer.xt.sso.service.dubbo;

import com.closer.xt.common.service.ServiceTemplate;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class AbstractService {
    @Autowired
    protected ServiceTemplate serviceTemplate;//抽象方法，作用域使用protect
}
