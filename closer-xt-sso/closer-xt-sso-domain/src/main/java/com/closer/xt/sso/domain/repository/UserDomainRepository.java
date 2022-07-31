package com.closer.xt.sso.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.closer.xt.sso.dao.UserMapper;
import com.closer.xt.sso.dao.data.User;
import com.closer.xt.sso.domain.UserDomain;
import com.closer.xt.sso.model.params.UserParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDomainRepository {
    @Autowired
    private UserMapper userMapper;

    public UserDomain createDomain(UserParams userParams) {
         return new UserDomain(this,userParams);
    }

    public User findUserByUnionId(String unionId) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUnionId, unionId);
        queryWrapper.last("limit 1");//查到数据就停止
        return userMapper.selectOne(queryWrapper);
    }

    public void saveUser(User user) {
        userMapper.insert(user);
    }

    public void updateUser(User user) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(User::getLastLoginTime, user.getLastLoginTime());
        updateWrapper.eq(User::getId, user.getId());
        userMapper.update(null, updateWrapper);
    }

    public User findUserByUserId(Long userId) {
        User user = userMapper.selectById(userId);
        return user;
    }
}
