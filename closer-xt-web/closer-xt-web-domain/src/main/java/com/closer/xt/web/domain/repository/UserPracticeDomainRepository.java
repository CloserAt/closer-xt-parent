package com.closer.xt.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.closer.xt.pojo.UserPractice;
import com.closer.xt.web.dao.UserPracticeMapper;
import com.closer.xt.web.domain.UserPracticeDomain;
import com.closer.xt.web.model.params.UserPracticeParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class UserPracticeDomainRepository {
    @Autowired
    private UserPracticeMapper userPracticeMapper;

    public UserPracticeDomain createDomain(UserPracticeParams userPracticeParams) {
        return new UserPracticeDomain(this,userPracticeParams);
    }

    public void saveUserPractice(UserPractice userPractice) {
        this.userPracticeMapper.insert(userPractice);
    }

    public int countUserPracticeTrueNum(Long userId, Long userHistoryId, int i) {
        LambdaQueryWrapper<UserPractice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPractice::getUserId,userId);
        queryWrapper.eq(UserPractice::getHistoryId,userHistoryId);
        queryWrapper.eq(UserPractice::getPStatus,i);
        return this.userPracticeMapper.selectCount(queryWrapper);
    }

    public int countUserPracticeWrongNum(Long userId, Long userHistoryId, int i) {
        LambdaQueryWrapper<UserPractice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPractice::getUserId,userId);
        queryWrapper.eq(UserPractice::getHistoryId,userHistoryId);
        queryWrapper.eq(UserPractice::getPStatus,i);
        return this.userPracticeMapper.selectCount(queryWrapper);
    }

    public UserPractice findUserPracticeByTopicId(Long userId, Long topicId, Long userHistoryId) {
        LambdaQueryWrapper<UserPractice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserPractice::getUserId,userId);
        queryWrapper.eq(UserPractice::getTopicId,topicId);
        queryWrapper.eq(UserPractice::getHistoryId,userHistoryId);
        return this.userPracticeMapper.selectOne(queryWrapper);
    }

    public List<Map<String, Object>> findUserPracticeAll(Long userId, Long userHistoryId) {
        QueryWrapper<UserPractice> queryWrapper = Wrappers.query();
        queryWrapper.eq("history_id",userHistoryId);
        queryWrapper.eq("user_id",userId);
        queryWrapper.select("topic_id as topicId","p_status as pStatus","user_answer as userAnswer");
        return userPracticeMapper.selectMaps(queryWrapper);
    }

    public Long findUserPracticeTopicId(Long userId, Long userHistoryId, Integer progress) {
        int pre = progress - 1;
        LambdaQueryWrapper<UserPractice> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(UserPractice::getUserId, userId);
        queryWrapper.eq(UserPractice::getHistoryId,userHistoryId);
        queryWrapper.select(UserPractice::getTopicId);
        queryWrapper.last("limit " + pre + ",1");
        UserPractice userPractice = this.userPracticeMapper.selectOne(queryWrapper);
        return null;
    }

    public void updateUserPractice(Long userHistoryId, Long topicId, Long userId, String answer, int i) {
        LambdaUpdateWrapper<UserPractice> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper
                .eq(UserPractice::getUserId,userId)
                .eq(UserPractice::getHistoryId,userHistoryId)
                .eq(UserPractice::getTopicId,topicId);
        updateWrapper.set(UserPractice::getUserAnswer,answer);
        //1 错误 2 正确
        updateWrapper.set(UserPractice::getPStatus,i);
        userPracticeMapper.update(null, updateWrapper);
    }
}
