package com.closer.xt.web.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.pojo.UserProblem;
import com.closer.xt.web.domain.repository.UserProblemDomainRepository;
import com.closer.xt.web.model.params.UserProblemParams;

public class UserProblemDomain {
    private UserProblemDomainRepository userProblemDomainRepository;
    private UserProblemParams userProblemParams;
    public UserProblemDomain(UserProblemDomainRepository userProblemDomainRepository, UserProblemParams userProblemParams) {
        this.userProblemDomainRepository = userProblemDomainRepository;
        this.userProblemParams = userProblemParams;
    }

    public UserProblem getUserProblem(Long userId, Long topicId) {
        return userProblemDomainRepository.getUserProblem(userId,topicId);
    }

    public void saveUserProblem(UserProblem userProblem) {
        userProblemDomainRepository.saveUserProblem(userProblem);
    }

    public void updateUserProblemErrorCount(Long userId, Long topicId, String answer) {
        userProblemDomainRepository.updateUserProblemErrorCount(userId,topicId,answer);
    }

    public Page<UserProblem> findUserProblemList(Long userId, int code, Integer page, Integer pageSize) {
        return userProblemDomainRepository.findUserProblemList(userId,code,page,pageSize);
    }

    public Page<UserProblem> findUserProblemListBySubjectId(Long searchSubjectId, Long userId, int code, Integer page, Integer pageSize) {
        return this.userProblemDomainRepository.findUserProblemListBySubjectId(searchSubjectId,userId,code,page,pageSize);
    }
}
