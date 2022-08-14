package com.closer.xt.admin.config;

import com.closer.xt.admin.template.StringTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class FreemarkerConfig {
    @Autowired
    private freemarker.template.Configuration configuration;

    @Autowired
    private StringTemplate stringTemplate;

    @PostConstruct
    public void init() {
        configuration.setSharedVariable(stringTemplate.getName(),stringTemplate);
    }
}
