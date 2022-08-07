package com.closer.xt.web.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserCouponModel {
    private String name;
    private BigDecimal amount;
    private Long couponId;
}
