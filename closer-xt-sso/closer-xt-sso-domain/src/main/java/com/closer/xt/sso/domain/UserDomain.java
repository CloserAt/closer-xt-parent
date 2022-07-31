package com.closer.xt.sso.domain;

import com.closer.xt.common.Login.UserThreadLocal;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.sso.dao.data.User;
import com.closer.xt.sso.domain.repository.UserDomainRepository;
import com.closer.xt.sso.model.UserModel;
import com.closer.xt.sso.model.params.UserParams;
import org.springframework.beans.BeanUtils;

/*
用户领域，用户得操作应该交给UserDomain来实现
 */
public class UserDomain {
    private UserDomainRepository userDomainRepository;

    private UserParams userParams;
    public UserDomain(UserDomainRepository userDomainRepository, UserParams userParams) {
        this.userDomainRepository = userDomainRepository;
        this.userParams = userParams;
    }

    public void updateUser(User user) {
        userDomainRepository.updateUser(user);
    }

    public User findUserByUnionId(String unionId) {
        return userDomainRepository.findUserByUnionId(unionId);
    }

    public void saveUser(User user) {
        userDomainRepository.saveUser(user);
    }

    public CallResult<Object> userInfo() {
        Long userId = UserThreadLocal.get();
        //这个地方 可以考虑做一个缓存，将用户的信息临时存储起来，用于在访问过程中的user信息的提取，后续做优化
        User userByUserId = userDomainRepository.findUserByUserId(userId);
        //此处不能直接返回user信息， user只是数据库字段表得映射实体，和业务中使用得User不一样
        //view 需要有自己得实体对象去映射页面得显示
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userByUserId, userModel);
        return CallResult.success(userModel);

    }
}
