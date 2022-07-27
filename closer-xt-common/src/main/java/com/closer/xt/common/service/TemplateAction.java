package com.closer.xt.common.service;

import com.closer.xt.common.model.CallResult;

public interface TemplateAction<T> {
    //1-检查参数是否合法
    CallResult<T> checkParam();

    //2-检查业务逻辑是否负荷需求
    CallResult<T> checkBiz();

    //3-执行
    CallResult<T> doAction();

    //执行完成后要进行的操作
    void finishUp(CallResult<T> callResult);
}
