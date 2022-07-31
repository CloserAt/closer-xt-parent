package com.closer.xt.admin.service.impl;

import com.closer.xt.common.service.ServiceTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractService {
    @Autowired
    protected ServiceTemplate serviceTemplate;
}
