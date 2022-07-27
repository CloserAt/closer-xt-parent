package com.closer.xt.common.service;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.service.TemplateAction;
//定义一个抽象类，可以根据具体需求来进行加载相应的方法
public abstract class AbstractTemplateAction<T> implements TemplateAction<T> {
    @Override
    public CallResult<T> checkParam() {
        return CallResult.success();
    }

    @Override
    public CallResult<T> checkBiz() {
        return CallResult.success();
    }

    @Override
    public void finishUp(CallResult<T> callResult) {

    }
}
