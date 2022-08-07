package com.closer.xt.web.service;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.web.model.params.OrderParams;

public interface OrderService {
    /**
     * 提交订单
     * @param orderParams
     * @return
     */
    CallResult submit(OrderParams orderParams);

    /**
     * 微信支付
     * @param orderParams
     * @return
     */
    CallResult wxPay(OrderParams orderParams);

    /**
     * 微信回调
     * @param orderParams
     * @return
     */
    CallResult notifyOrder(String xmlData);
}
