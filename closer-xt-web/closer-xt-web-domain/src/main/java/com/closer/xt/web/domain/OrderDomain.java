package com.closer.xt.web.domain;

import com.alibaba.fastjson.JSON;
import com.closer.xt.common.Login.UserThreadLocal;
import com.closer.xt.common.model.BusinessCodeEnum;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.utils.CommonUtils;
import com.closer.xt.pojo.*;
import com.closer.xt.web.domain.pay.WxPayDomain;
import com.closer.xt.web.domain.repository.OrderDomainRepository;
import com.closer.xt.web.model.OrderDisplayModel;
import com.closer.xt.web.model.SubjectModel;
import com.closer.xt.web.model.enums.OrderStatus;
import com.closer.xt.web.model.enums.PayStatus;
import com.closer.xt.web.model.enums.PayType;
import com.closer.xt.web.model.params.OrderParams;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayNativeOrderResult;
import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
public class OrderDomain {
    private OrderDomainRepository orderDomainRepository;
    private OrderParams orderParams;
    public OrderDomain(OrderDomainRepository orderDomainRepository, OrderParams orderParams) {
        this.orderDomainRepository = orderDomainRepository;
        this.orderParams = orderParams;
    }

    public CallResult<Object> submit(OrderParams orderParams) {
        /**
         * 1. 拿到参数
         * 2. 检查课程是否存在
         * 3. 检查优惠券是否可用 （有没有传优惠券，传了 判断优惠劵能不能用）
         * 4. 生成订单 创建订单 进行保存
         * 5. 返回用户订单的详情 涉及科目的信息展示
         */
        Long courseId = orderParams.getCourseId();
        Course course = this.orderDomainRepository.createCourseDomain(null).findCourseByCourseId(courseId);
        if (course == null) {
            return CallResult.fail(BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getCode(), "课程不存在");
        }

        Long couponId = orderParams.getCouponId();
        Long userId = UserThreadLocal.get();
        BigDecimal couponPrice = BigDecimal.ZERO;
        if (couponId != null) {
            //查找相关优惠券
            Coupon coupon = this.orderDomainRepository.createCouponDomain(null).findCouponByCouponId(couponId);
            couponPrice = checkCoupon(coupon,userId,course);
        }

        long createTimeMillis = System.currentTimeMillis();
        String orderId = createTimeMillis + String.valueOf(CommonUtils.random5Num()) + userId%10000;
        Order order = new Order();
        order.setCourseId(courseId);
        order.setCouponId(couponId == null ? 0 : couponId);
        order.setCreateTime(createTimeMillis);
        order.setExpireTime(course.getOrderTime());

//        BigDecimal subtract = couponPrice.subtract(couponPrice);
//        order.setOrderAmount(subtract.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : subtract);

        order.setOrderAmount(BigDecimal.valueOf(0.01));//此处方便测试 订单金额设置1分钱
        order.setOrderId(orderId);
        order.setOrderStatus(OrderStatus.INIT.getCode());
        order.setPayType(PayType.WX.getCode());
        order.setPayStatus(PayStatus.NO_PAY.getCode());
        order.setPayTime(0L);
        order.setUserId(userId);
        order.setPayOrderId(orderId);
        this.orderDomainRepository.saveOrder(order);
        List<SubjectModel> subjectModelList = this.orderDomainRepository.createSubjectDomain(null).findSubjectListByCourseId(courseId);

        OrderDisplayModel orderDisplayModel = new OrderDisplayModel();
        orderDisplayModel.setAmount(order.getOrderAmount());
        orderDisplayModel.setCourseName(course.getCourseName());
        orderDisplayModel.setOrderId(orderId);

        StringBuilder subject = new StringBuilder();
        for (SubjectModel subjectModel : subjectModelList) {
            subject.append(subjectModel.getSubjectName()).append(",");
        }
        if (subject.toString().length() > 0){
            subject = new StringBuilder(subject.substring(0,subject.toString().length() - 1));//去除最后的逗号
        }
        orderDisplayModel.setSubject(subject.toString());

        //订单创建成功了,发送延时消息
        //16代表30分钟，延迟30m执行消费
        //3代表10秒，延迟10s执行消费
        Map<String,String> map = new HashMap<>();
        map.put("orderId",order.getOrderId());
        //在消费方进行消费的时候，订单是否要取消 多长满足取消 以 time为准 s
        map.put("time","1800");
        this.orderDomainRepository.mqService.sendDelayedMessage("create_order_delay",map,16);//测试时传入3，上线时传16
        return CallResult.success(orderDisplayModel);
    }

    /**
     * 检测优惠劵是否合法 合法 返回对应的优惠金额
     * @param coupon
     * @return
     */
    private BigDecimal checkCoupon(Coupon coupon, Long userId, Course course) {
        CouponDomain couponDomain = this.orderDomainRepository.createCouponDomain(null);
        UserCoupon userCoupon = couponDomain.findUserCouponByUserIdAndCouponId(userId,coupon.getId());
        if (userCoupon == null) {
            //用户优惠券不存在
            return BigDecimal.ZERO;
        }

        Long startTime = userCoupon.getStartTime();
        Long expireTime = userCoupon.getExpireTime();
        long currentTimeMillis = System.currentTimeMillis();
        if (expireTime != -1 && currentTimeMillis > expireTime){
            //过期了
            return BigDecimal.ZERO;
        }
        if (startTime != -1 && currentTimeMillis < startTime){
            //未到使用时间
            return BigDecimal.ZERO;
        }
        if (1 == coupon.getDisStatus()) {
            if (coupon.getMax().compareTo(course.getCourseZhePrice())>0){
                return BigDecimal.ZERO;
            }
        }
        //标记为 已使用 未消费  如果用户取消了订单 把优惠券 还回来
        userCoupon.setStatus(4);
        couponDomain.updateCouponStatus(userCoupon);
        return coupon.getPrice();
    }

    public CallResult<Object> wxPay(OrderParams orderParams) {
        /**
         * 1. 获取到登录用户
         * 2. 根据订单号 查询订单 检查订单状态和支付状态
         * 3. 根据课程id 查询课程 确保课程的状态正常
         * 4. 组装微信支付需要的参数，发起微信的调用，微信会给我们返回对应的二维码链接
         * 5. 更改订单状态 为 已提交
         */
        Long userId = UserThreadLocal.get();
        String orderId = orderParams.getOrderId();
        Order order = this.orderDomainRepository.findOrderByOrderId(orderId);
        if (order == null) {
            return CallResult.fail(BusinessCodeEnum.ORDER_NOT_EXIST.getCode(),"订单不存在");
        }

        Integer orderStatus = order.getOrderStatus();
        if (orderStatus != OrderStatus.INIT.getCode()) {
            return CallResult.fail(BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getCode(),"订单已被更改");
        }

        Integer payStatus = order.getPayStatus();
        if (payStatus != PayStatus.NO_PAY.getCode()) {
            return CallResult.fail(BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getCode(),"订单已被支付");
        }

        Long courseId = order.getCourseId();
        Course course = this.orderDomainRepository.createCourseDomain(null).findCourseByCourseId(courseId);
        if (course == null) {
            return CallResult.fail(BusinessCodeEnum.COURSE_NOT_EXIST.getCode(),"课程不存在");
        }

        //最好每次重新生成订单号
        String payOrderId = System.currentTimeMillis() + String.valueOf(CommonUtils.random5Num()) + userId%10000;
        order.setPayOrderId(payOrderId);
        Integer payType = orderParams.getPayType();
        order.setPayType(payType);

        //组装微信支付需要的参数，发起微信的调用，微信会给我们返回对应的二维码链接
        this.orderDomainRepository.updatePayOrderId(order);//更新订单的支付id
        WxPayDomain wxPayDomain = new WxPayDomain(this.orderDomainRepository.wxPayConfiguration);
        WxPayService wxPayService = wxPayDomain.getWxPayService();
        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setNotifyUrl(this.orderDomainRepository.wxPayConfiguration.wxNotifyUrl);//微信支付的回调通知接口
        orderRequest.setBody(course.getCourseName());//课程名称
        orderRequest.setOutTradeNo(payOrderId);//交易id
        orderRequest.setProductId(String.valueOf(courseId));//产品id

        orderRequest.setTotalFee(BaseWxPayRequest.yuanToFen(String.valueOf(order.getOrderAmount().doubleValue())));//价格 - 元转成分
        orderRequest.setSpbillCreateIp("182.92.102.161");
        orderRequest.setTradeType("NATIVE");
        try {
            WxPayNativeOrderResult nativeOrderResult = wxPayService.createOrder(orderRequest);
            String codeUrl = nativeOrderResult.getCodeUrl();
            this.orderDomainRepository.updateOrderStatus(OrderStatus.INIT.getCode(),order);//更新订单状态
            return CallResult.success(codeUrl);
        } catch (WxPayException e) {
            e.printStackTrace();
            log.info("wxpay error:{}",e);
            return CallResult.fail(BusinessCodeEnum.PAY_ORDER_CREATE_FAIL.getCode(),"订单支付失败");
        }
    }

    public CallResult<Object> notifyOrder(String xmlData) {
        /**
         * 1. 解析微信参数 payOrderId
         * 2. 根据payOrderId进行订单查询
         * 3. 如果订单存在 订单更改为已支付
         * 4. 处理了交易的流水信息 做了一个保存
         * 5. 处理用户的课程，要给用户将购买的课程标识为已购买
         * 6. 优惠券 标识为已使用
         */
        WxPayDomain wxPayDomain = new WxPayDomain(this.orderDomainRepository.wxPayConfiguration);
        try {
            WxPayOrderNotifyResult notifyResult = wxPayDomain.getWxPayService().parseOrderNotifyResult(xmlData);
            String returnCode = notifyResult.getReturnCode();
            if ("SUCCESS".equals(returnCode)) {
                //支付成功
                //1.交易id：传递到微信的支付订单号 payOrderId
                String payOrderId = notifyResult.getOutTradeNo();
                //微信方交易订单号
                String transactionId = notifyResult.getTransactionId();
                Order order = this.orderDomainRepository.findOrderByPayOrderId(payOrderId);
                if (order == null){
                    return CallResult.fail(BusinessCodeEnum.ORDER_NOT_EXIST.getCode(),"订单不存在");
                }
                if (order.getOrderStatus() == OrderStatus.PAYED.getCode()
                        && order.getPayStatus() == PayStatus.PAYED.getCode()){
                    //代表订单已经处理过了，无需进行重复处理
                    return CallResult.success();
                }
                order.setOrderStatus(OrderStatus.PAYED.getCode());
                order.setPayStatus(PayStatus.PAYED.getCode());
                order.setPayTime(System.currentTimeMillis());
                this.orderDomainRepository.updateOrderStatusAndPayStatus(order);

                //订单
                //添加支付信息
                OrderTrade orderTrade = this.orderDomainRepository.findOrderTrade(order.getOrderId());
                if (orderTrade == null) {
                    //是第一次添加流水
                    orderTrade = new OrderTrade();
                    orderTrade.setPayInfo(JSON.toJSONString(notifyResult));
                    orderTrade.setOrderId(order.getOrderId());
                    orderTrade.setUserId(order.getUserId());
                    orderTrade.setPayType(order.getPayType());
                    orderTrade.setTransactionId(transactionId);
                    this.orderDomainRepository.saveOrderTrade(orderTrade);
                } else {
                    //不是第一次，更新即可
                    orderTrade.setPayInfo(JSON.toJSONString(notifyResult));
                    this.orderDomainRepository.updateOrderTrade(orderTrade);
                }

                //给用户添加课程
                this.orderDomainRepository.createUserCourseDomain(null).saveUserCourse(order);

                //优惠券标记已使用
                Long couponId = order.getCouponId();
                if (couponId > 0) {
                    UserCoupon userCoupon = this.orderDomainRepository.createCouponDomain(null).findUserCouponByUserIdAndCouponId(order.getUserId(),couponId);
                    if (userCoupon != null) {
                        userCoupon.setStatus(1);
                        this.orderDomainRepository.createCouponDomain(null).updateCouponStatus(userCoupon);
                    }
                }
                return CallResult.success();
            }
        } catch (WxPayException e) {
            e.printStackTrace();
            return CallResult.fail(BusinessCodeEnum.PAY_ORDER_CREATE_FAIL.getCode(),"微信支付信息处理失败");
        }
        return CallResult.fail();
    }
}
