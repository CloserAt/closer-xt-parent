package com.closer.xt.web.service;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.web.model.params.BillParams;

public interface BillService {
    /**
     * 生成推广连接接口
     * @param billParams
     * @return
     */
    CallResult all(BillParams billParams);

    /**
     * 生成推广链接
     * @param billParams
     * @return
     */
    CallResult gen(BillParams billParams);
}
