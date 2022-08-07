package com.closer.xt.web.domain;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.closer.xt.common.cache.Cache;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.pojo.Coupon;
import com.closer.xt.pojo.Course;
import com.closer.xt.pojo.UserCoupon;
import com.closer.xt.web.domain.repository.CouponDomainRepository;
import com.closer.xt.web.model.UserCouponModel;
import com.closer.xt.web.model.params.CouponParams;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CouponDomain {
    private CouponDomainRepository couponDomainRepository;
    private CouponParams couponParams;
    public CouponDomain(CouponDomainRepository couponDomainRepository, CouponParams couponParams) {
        this.couponDomainRepository = couponDomainRepository;
        this.couponParams = couponParams;
    }

    public List<UserCoupon> findUserCoupon(Long userId) {
        return couponDomainRepository.findUserCouponByUserId(userId);
    }

    public Coupon findCouponByCouponId(Long couponId) {
        return couponDomainRepository.findCouponByCouponId(couponId);
    }

    public UserCoupon findUserCouponByUserIdAndCouponId(Long userId, Long couponId) {
        return couponDomainRepository.findUserCouponByUserIdAndCouponId(userId,couponId);
    }

    public void updateCouponStatus(UserCoupon userCoupon) {
        this.couponDomainRepository.updateCouponStatus(userCoupon);
    }

    public void updateCouponNoUseStatus(Long userId, Long couponId, int status) {
        this.couponDomainRepository.updateCouponNoUseStatus(userId,couponId,status);
    }
}
