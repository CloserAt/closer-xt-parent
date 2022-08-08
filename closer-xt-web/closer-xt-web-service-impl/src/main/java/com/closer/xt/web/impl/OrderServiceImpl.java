package com.closer.xt.web.impl;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.service.AbstractTemplateAction;
import com.closer.xt.web.domain.OrderDomain;
import com.closer.xt.web.domain.repository.OrderDomainRepository;
import com.closer.xt.web.model.params.OrderParams;
import com.closer.xt.web.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class OrderServiceImpl extends AbstractService implements OrderService {
    @Autowired
    private OrderDomainRepository orderDomainRepository;

    @Override
    @Transactional
    public CallResult submit(OrderParams orderParams) {
        OrderDomain orderDomain = this.orderDomainRepository.createDomain(orderParams);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return orderDomain.submit(orderParams);
            }
        });
    }

    @Override
    @Transactional
    public CallResult wxPay(OrderParams orderParams) {
        OrderDomain orderDomain = this.orderDomainRepository.createDomain(orderParams);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return orderDomain.wxPay(orderParams);
            }
        });
    }

    @Override
    public CallResult notifyOrder(String xmlData) {
        OrderDomain orderDomain = this.orderDomainRepository.createDomain(null);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return orderDomain.notifyOrder(xmlData);
            }
        });
    }

    @Override
    public CallResult findOrder(OrderParams orderParams) {
        OrderDomain orderDomain = this.orderDomainRepository.createDomain(null);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return orderDomain.findOrder(orderParams);
            }
        });
    }

    @Override
    public CallResult orderList(OrderParams orderParams) {
        OrderDomain orderDomain = this.orderDomainRepository.createDomain(null);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return orderDomain.orderList(orderParams);
            }
        });
    }
}
