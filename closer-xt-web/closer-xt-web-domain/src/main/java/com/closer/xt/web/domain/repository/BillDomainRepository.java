package com.closer.xt.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.closer.xt.pojo.Bill;
import com.closer.xt.web.dao.BillMapper;
import com.closer.xt.web.domain.BillDomain;
import com.closer.xt.web.model.enums.DeleteStatus;
import com.closer.xt.web.model.params.BillParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class BillDomainRepository {
    @Value("${invite.url}")
    public String inviteUrl;

    @Autowired
    private BillMapper billMapper;

    public BillDomain createDomain(BillParams billParams) {
        return new BillDomain(this,billParams);
    }

    public List<Bill> findAll() {
        LambdaQueryWrapper<Bill> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Bill::getDeleteStatus, DeleteStatus.NORMAL.getCode());
        return this.billMapper.selectList(queryWrapper);
    }

    public Bill findBillById(Long id) {
        return this.billMapper.selectById(id);
    }
}
