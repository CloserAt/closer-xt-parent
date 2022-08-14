package com.closer.xt.web.domain.thread;

import com.closer.xt.common.model.enums.InviteType;
import com.closer.xt.common.utils.AESUtils;
import com.closer.xt.pojo.Invite;
import com.closer.xt.pojo.Order;
import com.closer.xt.web.domain.repository.OrderDomainRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class orderThread {
    @Autowired
    private OrderDomainRepository orderDomainRepository;

    @Async("taskExecutor")
    public void fillInvite(List<Map<String,String>> billTypeList, Order order, Long userId) {
        for (Map<String, String> inviteMap : billTypeList) {
            //有推荐信息，构建邀请信息
            Invite invite = new Invite();
            invite.setInviteInfo(order.getOrderId());
            invite.setInviteStatus(0);
            invite.setInviteTime(System.currentTimeMillis());
            invite.setInviteType(InviteType.LOGIN.getCode());
            invite.setInviteUserId(userId);
            invite.setUserId(Long.parseLong(AESUtils.decrypt(inviteMap.get("userId"))));
            invite.setBillType(inviteMap.get("billType"));
            invite.setCreateTime(System.currentTimeMillis());
            this.orderDomainRepository.createInviteDomain(null).save(invite);
        }
    }
}
