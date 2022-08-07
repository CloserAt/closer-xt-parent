package com.closer.xt.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.closer.xt.pojo.Coupon;
import com.closer.xt.pojo.UserCoupon;
import com.closer.xt.web.dao.CouponMapper;
import com.closer.xt.web.dao.UserCouponMapper;
import com.closer.xt.web.domain.CouponDomain;
import com.closer.xt.web.domain.CourseDomain;
import com.closer.xt.web.model.params.CouponParams;
import com.closer.xt.web.model.params.CourseParams;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class CouponDomainRepository {
    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private CourseDomainRepository courseDomainRepository;

    public CouponDomain creatDomain(CouponParams couponParams) {
        return new CouponDomain(this,couponParams);
    }

    public List<UserCoupon> findUserCouponByUserId(Long userId) {
        LambdaQueryWrapper<UserCoupon> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserCoupon::getUserId,userId);
        queryWrapper.eq(UserCoupon::getStatus,0);
        return this.userCouponMapper.selectList(queryWrapper);
    }

    public Coupon findCouponByCouponId(Long couponId) {
        return this.couponMapper.selectById(couponId);
    }


    public UserCoupon findUserCouponByUserIdAndCouponId(Long userId, Long couponId) {
        LambdaQueryWrapper<UserCoupon> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserCoupon::getUserId,userId);
        queryWrapper.eq(UserCoupon::getCouponId,couponId);
        queryWrapper.eq(UserCoupon::getStatus,0);
        queryWrapper.last("limit 1");
        UserCoupon userCoupon = this.userCouponMapper.selectOne(queryWrapper);
        return userCoupon;
    }

    public void updateCouponStatus(UserCoupon userCoupon) {
        LambdaUpdateWrapper<UserCoupon> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(UserCoupon::getId,userCoupon.getId());
        updateWrapper.set(UserCoupon::getStatus,userCoupon.getStatus());
        this.userCouponMapper.update(null,updateWrapper);
    }

    public void updateCouponNoUseStatus(Long userId, Long couponId, int status) {
        LambdaUpdateWrapper<UserCoupon> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(UserCoupon::getCouponId,couponId);
        updateWrapper.eq(UserCoupon::getUserId,userId);
        updateWrapper.eq(UserCoupon::getStatus,status);
        updateWrapper.set(UserCoupon::getStatus,0);
        this.userCouponMapper.update(null, updateWrapper);
    }
}
