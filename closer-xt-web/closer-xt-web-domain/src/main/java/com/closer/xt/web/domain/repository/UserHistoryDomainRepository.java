package com.closer.xt.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.closer.xt.pojo.UserHistory;
import com.closer.xt.web.dao.UserHistoryMapper;
import com.closer.xt.web.domain.UserHistoryDomain;
import com.closer.xt.web.model.params.UserHistoryParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserHistoryDomainRepository {
    @Autowired
    private UserHistoryMapper userHistoryMapper;

    public UserHistoryDomain createDomain(UserHistoryParams userHistoryParams) {
        return new UserHistoryDomain(this,userHistoryParams);
    }

    public UserHistory findUserHistory(Long userId, Long subjectId, int historyStatus) {
        LambdaQueryWrapper<UserHistory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserHistory::getUserId,userId);
        queryWrapper.eq(UserHistory::getSubjectId,subjectId);
        queryWrapper.eq(UserHistory::getHistoryStatus,historyStatus);
        queryWrapper.last("limit 1");
        return this.userHistoryMapper.selectOne(queryWrapper);
    }

    public UserHistory findUserHistoryById(Long practiceId) {
        return this.userHistoryMapper.selectById(practiceId);
    }

    public void saveUserHistory(UserHistory userHistory) {
        this.userHistoryMapper.insert(userHistory);
    }

    public void updateUserHistoryStatus(Long historyId, int historyStatus, long finishTime) {
        UserHistory userHistory = new UserHistory();
        userHistory.setId(historyId);
        userHistory.setHistoryStatus(historyStatus);
        userHistory.setFinishTime(finishTime);
        userHistoryMapper.updateById(userHistory);
    }
}
