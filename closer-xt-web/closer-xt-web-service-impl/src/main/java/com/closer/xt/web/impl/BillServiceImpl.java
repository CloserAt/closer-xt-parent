package com.closer.xt.web.impl;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.service.AbstractTemplateAction;
import com.closer.xt.web.domain.BillDomain;
import com.closer.xt.web.domain.repository.BillDomainRepository;
import com.closer.xt.web.model.params.BillParams;
import com.closer.xt.web.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;

public class BillServiceImpl extends AbstractService implements BillService {
    @Autowired
    private BillDomainRepository billDomainRepository;

    @Override
    public CallResult all(BillParams billParams) {
        BillDomain billDomain = this.billDomainRepository.createDomain(billParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return billDomain.findAllBillModelList(billParams);
            }
        });
    }

    @Override
    public CallResult gen(BillParams billParams) {
        BillDomain billDomain = this.billDomainRepository.createDomain(billParams);
        return billDomain.gen(billParams);
    }
}
