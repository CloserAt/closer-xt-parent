package com.closer.xt.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.closer.xt.pojo.UserProblem;
import com.closer.xt.web.dao.UserProblemMapper;
import com.closer.xt.web.domain.UserProblemDomain;
import com.closer.xt.web.model.params.UserProblemParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserProblemDomainRepository {
    @Autowired
    private UserProblemMapper userProblemMapper;

    public UserProblemDomain createDomain(UserProblemParams userProblemParams) {
        return new UserProblemDomain(this,userProblemParams);
    }

    public UserProblem getUserProblem(Long userId, Long topicId) {
        LambdaQueryWrapper<UserProblem> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserProblem::getTopicId,topicId);
        queryWrapper.eq(UserProblem::getUserId,userId);
        queryWrapper.last("limit 1");
        return userProblemMapper.selectOne(queryWrapper);
    }

    public void saveUserProblem(UserProblem userProblem) {
        userProblemMapper.insert(userProblem);
    }

    public void updateUserProblemErrorCount(Long userId, Long topicId, String answer) {
        UpdateWrapper<UserProblem> update = Wrappers.update();
        update.eq("user_id",userId);
        update.eq("topic_id",topicId);
        update.set("error_count","error_count+1");
        update.set("answer",answer);
        //加锁操作 没有别的操作 会影响
        //update table set error_count=error_count+1 where user_id=111 and topic_id=222
        userProblemMapper.update(null, update);
    }
}
