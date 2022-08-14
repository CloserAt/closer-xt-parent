package com.closer.xt.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.common.wexin.config.WxPayConfiguration;
import com.closer.xt.pojo.Coupon;
import com.closer.xt.pojo.Course;
import com.closer.xt.pojo.Order;
import com.closer.xt.pojo.OrderTrade;
import com.closer.xt.web.dao.OrderMapper;
import com.closer.xt.web.dao.OrderTradeMapper;
import com.closer.xt.web.domain.*;
import com.closer.xt.web.domain.mq.MqService;
import com.closer.xt.web.domain.thread.orderThread;
import com.closer.xt.web.model.SubjectModel;
import com.closer.xt.web.model.params.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
@Component
public class OrderDomainRepository {
    @Autowired
    public orderThread orderThread;

    @Autowired
    public MqService mqService;

    @Autowired
    public WxPayConfiguration wxPayConfiguration;

    @Resource
    private OrderMapper orderMapper;

    @Autowired
    private OrderTradeMapper orderTradeMapper;

    @Autowired
    private CourseDomainRepository courseDomainRepository;

    @Autowired
    private CouponDomainRepository couponDomainRepository;

    @Autowired
    private SubjectDomainRepository subjectDomainRepository;

    @Autowired
    private UserCourseDomainRepository userCourseDomainRepository;

    @Autowired
    private InviteDomainRepository inviteDomainRepository;

    public OrderDomain createDomain(OrderParams orderParams) {
        return new OrderDomain(this,orderParams);
    }

    public CourseDomain createCourseDomain(CourseParams courseParams) {
        return courseDomainRepository.createDomain(courseParams);
    }

    public CouponDomain createCouponDomain(CouponParams couponParams) {
        return this.couponDomainRepository.creatDomain(couponParams);
    }

    public void saveOrder(Order order) {
        this.orderMapper.insert(order);
    }

    public SubjectDomain createSubjectDomain(SubjectParams subjectParams) {
        return this.subjectDomainRepository.createDomain(subjectParams);
    }

    public Order findOrderByOrderId(String orderId) {
        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Order::getOrderId,orderId);
        return this.orderMapper.selectOne(queryWrapper);
    }

    public void updateOrderStatus(int initCode, Order order) {
        LambdaUpdateWrapper<Order> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(Order::getId,order.getId());
        updateWrapper.eq(Order::getOrderStatus,initCode);
        updateWrapper.set(Order::getOrderStatus,order.getOrderStatus());
        this.orderMapper.update(null,updateWrapper);
    }


    public void updatePayOrderId(Order order) {
        LambdaUpdateWrapper<Order> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(Order::getId,order.getId());
        updateWrapper.eq(Order::getPayOrderId,order.getPayOrderId());
        updateWrapper.set(Order::getPayStatus,order.getPayStatus());
        updateWrapper.set(Order::getPayTime,order.getPayTime());
        this.orderMapper.update(null,updateWrapper);
    }

    public Order findOrderByPayOrderId(String payOrderId) {
        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Order::getPayOrderId, payOrderId);
        return this.orderMapper.selectOne(queryWrapper);
    }

    public void updateOrderStatusAndPayStatus(Order order) {
        LambdaUpdateWrapper<Order> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(Order::getId,order.getId());
        updateWrapper.set(Order::getOrderStatus,order.getOrderStatus());
        updateWrapper.set(Order::getPayStatus,order.getPayStatus());
        updateWrapper.set(Order::getPayTime,order.getPayTime());
        this.orderMapper.update(null,updateWrapper);
    }

    public OrderTrade findOrderTrade(String orderId) {
        LambdaQueryWrapper<OrderTrade> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderTrade::getOrderId,orderId);
        queryWrapper.last("limit 1");
        return this.orderTradeMapper.selectOne(queryWrapper);
    }

    public void saveOrderTrade(OrderTrade orderTrade) {
        this.orderTradeMapper.insert(orderTrade);
    }

    public void updateOrderTrade(OrderTrade orderTrade) {
        LambdaUpdateWrapper<OrderTrade> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(OrderTrade::getId,orderTrade.getId());
        updateWrapper.set(OrderTrade::getPayInfo,orderTrade.getPayInfo());
        this.orderTradeMapper.update(null, updateWrapper);
    }

    public UserCourseDomain createUserCourseDomain(UserCourseParams userCourseParams) {
        return this.userCourseDomainRepository.createDomain(userCourseParams);
    }

    public Page<Order> orderList(int status, int page, int pageSize, Long userId) {
        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Order::getUserId,userId);
        queryWrapper.ne(Order::getOrderStatus,status);
        Page<Order> orderPage = new Page<>(page,pageSize);
        return this.orderMapper.selectPage(orderPage,queryWrapper);

    }

    public InviteDomain createInviteDomain(InviteParams inviteParams) {
        return inviteDomainRepository.createDomain(inviteParams);
    }
}
