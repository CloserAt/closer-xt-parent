package com.closer.xt.web.domain;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.closer.xt.pojo.UserPractice;
import com.closer.xt.web.dao.UserPracticeMapper;
import com.closer.xt.web.domain.repository.UserPracticeDomainRepository;
import com.closer.xt.web.model.params.UserPracticeParams;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class UserPracticeDomain {
    private UserPracticeDomainRepository userPracticeDomainRepository;
    private UserPracticeParams userPracticeParams;
    public UserPracticeDomain(UserPracticeDomainRepository userPracticeDomainRepository, UserPracticeParams userPracticeParams) {
        this.userPracticeDomainRepository = userPracticeDomainRepository;
        this.userPracticeParams = userPracticeParams;
    }

    public Long findUserPracticeTopicId(Long userId, Long userHistoryId, Integer progress) {
        return userPracticeDomainRepository.findUserPracticeTopicId(userId,userHistoryId,progress);
    }

    public void saveUserPractice(UserPractice userPractice) {
        this.userPracticeDomainRepository.saveUserPractice(userPractice);
    }

    public int countUserPracticeTrueNum(Long userId, Long userHistoryId) {
        return userPracticeDomainRepository.countUserPracticeTrueNum(userId,userHistoryId,2);//2代表正确答案
    }

    public int countUserPracticeWrongNum(Long userId, Long userHistoryId) {
        return userPracticeDomainRepository.countUserPracticeWrongNum(userId,userHistoryId,1);//1代表错误答案
    }

    public UserPractice findUserPracticeByTopicId(Long userId, Long topicId, Long userHistoryId) {

        return userPracticeDomainRepository.findUserPracticeByTopicId(userId,topicId,userHistoryId);
    }

    public List<Map<String, Object>> findUserPracticeAll(Long userId, Long userHistoryId) {
        return userPracticeDomainRepository.findUserPracticeAll(userId,userHistoryId);
    }

    public void updateUserPractice(Long userHistoryId, Long topicId, Long userId, String answer, int i) {
        userPracticeDomainRepository.updateUserPractice(userHistoryId,topicId,userId,answer,i);
    }
}
