package com.closer.xt.web.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderViewModel {

    private String orderId;
    private CourseViewModel course;
    private BigDecimal oAmount;
    private Integer orderStatus;
    private Integer payType;
    private Integer payStatus;
    private String createTime;
    private String expireTime;
    private BigDecimal couponAmount;

    private String userName;

    private String payOrderId;
    private String payTime;

}
