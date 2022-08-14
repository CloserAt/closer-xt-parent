package com.closer.xt.web.api;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.web.model.params.OrderParams;
import com.closer.xt.web.service.OrderService;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import groovy.transform.AutoClone;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("order")
public class OrderAPI {
    @Autowired
    private OrderService orderService;

    @PostMapping("submitOrder")
    public CallResult submitOrder(HttpServletRequest request,
                                  @RequestBody OrderParams orderParams) {
        orderParams.setRequest(request);
        return orderService.submit(orderParams);
    }

    @PostMapping("wxPay")
    public CallResult wxPay(@RequestBody OrderParams orderParams) {
        return orderService.wxPay(orderParams);
    }

    //这个接口会重复调用直到成功
    @PostMapping("notify")
    public String notifyOrder(@RequestBody String xmlData) {
        log.info("notify 数据{}",xmlData);
        CallResult callResult = orderService.notifyOrder(xmlData);
        if (callResult.isSuccess()){
            return WxPayNotifyResponse.success("成功");
        }
        return WxPayNotifyResponse.fail("失败");
    }

    @PostMapping("findOrder")
    public CallResult findOrder(@RequestBody OrderParams orderParams) {
        return orderService.findOrder(orderParams);
    }

    @PostMapping("orderList")
    public CallResult orderList(@RequestBody OrderParams orderParams) {
        return orderService.orderList(orderParams);
    }
}
