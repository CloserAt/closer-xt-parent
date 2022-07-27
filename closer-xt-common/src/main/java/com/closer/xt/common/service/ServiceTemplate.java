package com.closer.xt.common.service;

import com.closer.xt.common.model.CallResult;

public interface ServiceTemplate {
    /**
     * run in  datasource and execute Transaction
     * 将增删改的操作以及事务放在这个模板中
     * @param action
     * @param <T>
     * @return
     */
    <T>CallResult<T> execute(TemplateAction<T> action);

    /**
     * 将查询的操作放在该模板中，不涉及事务
     * @param action
     * @param <T>
     * @return
     */
    <T>CallResult<T> executeQuery(TemplateAction<T> action);
}
