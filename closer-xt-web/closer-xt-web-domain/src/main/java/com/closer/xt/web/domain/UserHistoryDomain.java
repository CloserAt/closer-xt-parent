package com.closer.xt.web.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.pojo.UserHistory;
import com.closer.xt.web.domain.repository.UserHistoryDomainRepository;
import com.closer.xt.web.model.SubjectModel;
import com.closer.xt.web.model.params.UserHistoryParams;

import java.util.List;

public class UserHistoryDomain {
    private UserHistoryDomainRepository userHistoryDomainRepository;
    private UserHistoryParams userHistoryParams;
    public UserHistoryDomain(UserHistoryDomainRepository userHistoryDomainRepository, UserHistoryParams userHistoryParams) {
        this.userHistoryDomainRepository = userHistoryDomainRepository;
        this.userHistoryParams = userHistoryParams;
    }

    public UserHistory findUserHistory(Long userId, Long subjectId, int historyStatus) {
        return userHistoryDomainRepository.findUserHistory(userId,subjectId,historyStatus);
    }

    public UserHistory findUserHistoryById(Long practiceId) {
        return userHistoryDomainRepository.findUserHistoryById(practiceId);
    }

    public void saveUserHistory(UserHistory userHistory) {
        this.userHistoryDomainRepository.saveUserHistory(userHistory);
    }

    public void updateUserHistoryStatus(Long historyId, int code, long currentTimeMillis) {
        this.userHistoryDomainRepository.updateUserHistoryStatus(historyId,code,currentTimeMillis);
    }

    public Integer countUserHistoryBySubjectList(Long userId, List<SubjectModel> subjectInfoByCourseId) {
        return userHistoryDomainRepository.countUserHistoryBySubjectList(userId,subjectInfoByCourseId);
    }

    public Page<UserHistory> findUserHistoryList(Long userId, Integer page, Integer pageSize) {
        return userHistoryDomainRepository.findUserHistoryList(userId,page,pageSize);
    }
}
