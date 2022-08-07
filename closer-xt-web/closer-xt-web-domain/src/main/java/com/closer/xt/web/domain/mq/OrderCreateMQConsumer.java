package com.closer.xt.web.domain.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.closer.xt.pojo.Order;
import com.closer.xt.web.domain.repository.OrderDomainRepository;
import com.closer.xt.web.model.enums.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
@Slf4j
@Component
@RocketMQMessageListener(topic = "create_order_delay",consumerGroup = "create_order_group")
public class OrderCreateMQConsumer implements RocketMQListener<String> {

    @Autowired
    private OrderDomainRepository orderDomainRepository;

    //消息发送过来 会转为json的字符串
    @Override
    public void onMessage(String messageOn) {
        log.info("收到的消息:{},时间:{}",messageOn,new DateTime().toString("HH:mm:ss"));

        Map<String,String> message = JSON.parseObject(messageOn, new TypeReference<Map<String, String>>() {
        });
        /*
        1.收到消息了 订单id 还有时间（用于判断订单是否取消）
        2.根据订单id查询订单
         */
        String orderId = message.get("orderId");
        Order order = this.orderDomainRepository.findOrderByOrderId(orderId);
        //秒
        Integer time = Integer.parseInt(message.get("time"));
        if (order != null) {
            if (order.getOrderStatus() != OrderStatus.INIT.getCode()
                || order.getOrderStatus() != OrderStatus.COMMIT.getCode()) {
                log.info("订单已经被其他程序修改：{}",orderId);
                return;
            }
            Long createTime = order.getCreateTime();
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - createTime < time*1000) {
                throw  new RuntimeException("时间没到，不能取消订单");
            }
            order.setOrderStatus(OrderStatus.CANCEL.getCode());
            int initCode = OrderStatus.INIT.getCode();
            this.orderDomainRepository.updateOrderStatus(initCode,order);

            //把优惠券还回去
            Long userId = order.getUserId();
            Long couponId = order.getCouponId();
            if (couponId > 0) {
                this.orderDomainRepository.createCouponDomain(null).updateCouponNoUseStatus(userId,couponId,4);
            }
            log.info("订单长时间未支付，已取消：{}",orderId);
        }

    }
}
