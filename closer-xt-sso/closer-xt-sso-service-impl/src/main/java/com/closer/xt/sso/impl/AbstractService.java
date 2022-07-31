package com.closer.xt.sso.impl;

import com.closer.xt.common.service.ServiceTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public abstract class AbstractService {
    @Autowired
    protected ServiceTemplate serviceTemplate;//抽象方法，作用域使用protect
}
