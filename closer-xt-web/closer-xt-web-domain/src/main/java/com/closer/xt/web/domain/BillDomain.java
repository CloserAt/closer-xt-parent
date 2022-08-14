package com.closer.xt.web.domain;

import com.closer.xt.common.Login.UserThreadLocal;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.utils.AESUtils;
import com.closer.xt.pojo.Bill;
import com.closer.xt.web.domain.repository.BillDomainRepository;
import com.closer.xt.web.model.BillModel;
import com.closer.xt.web.model.params.BillParams;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class BillDomain {
    private BillDomainRepository billDomainRepository;
    private BillParams billParams;
    public BillDomain(BillDomainRepository billDomainRepository, BillParams billParams) {
        this.billDomainRepository = billDomainRepository;
        this.billParams = billParams;
    }

    public CallResult<Object> findAllBillModelList(BillParams billParams) {
        List<Bill> billList = this.billDomainRepository.findAll();
        List<BillModel> billModelList = new ArrayList<>();
        for (Bill bill : billList) {
            BillModel billModel = new BillModel();
            BeanUtils.copyProperties(bill,billModel);
            billModelList.add(billModel);
        }
        return CallResult.success(billModelList);
    }


    public CallResult gen(BillParams billParams) {
        /**
         * 1. 根据id 查询海报信息
         * 2. 加密登录的用户id
         * 3. 拼接推广链接
         */
        Long id = billParams.getId();
        Bill bill = this.billDomainRepository.findBillById(id);
        if (bill == null) {
            return CallResult.fail(-999,"海报不存在");
        }
        String billType = bill.getBillType();
        Long userId = UserThreadLocal.get();
        //AES加密处理
        String encrypt = AESUtils.encrypt(userId.toString());

        //拼接推广链接
        return CallResult.success(billDomainRepository.inviteUrl+billType+"/"+encrypt);
    }
}
