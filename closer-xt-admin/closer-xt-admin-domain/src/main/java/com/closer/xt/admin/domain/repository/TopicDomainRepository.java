package com.closer.xt.admin.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.admin.dao.TopicMapper;
import com.closer.xt.admin.domain.SubjectDomain;
import com.closer.xt.admin.domain.TopicDomain;
import com.closer.xt.admin.params.SubjectParams;
import com.closer.xt.admin.params.TopicParams;
import com.closer.xt.pojo.Topic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicDomainRepository {
    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private SubjectDomainRepository subjectDomainRepository;

    public TopicDomain createDomain(TopicParams topicParams) {
        return new TopicDomain(this, topicParams);
    }


    public Page<Topic> findPage(int currentPage, int pageSize, String topicTitle, Long subjectId) {
        Page<Topic> topicPage = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<Topic> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(topicTitle)) {
            queryWrapper.like(Topic::getTopicTitle, topicTitle);
        }
        if (subjectId != null) {
            queryWrapper.eq(Topic::getTopicSubject,subjectId);
        }
        return topicMapper.selectPage(topicPage,queryWrapper);
    }
    public SubjectDomain createSubjectDomain(SubjectParams subjectParams) {
        return subjectDomainRepository.createDomain(subjectParams);
    }

    public Topic findTopicByTitle(String topicTitle) {
        LambdaQueryWrapper<Topic> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Topic::getTopicTitle,topicTitle);
        queryWrapper.last("limit 1");
        return topicMapper.selectOne(queryWrapper);
    }

    public void updateTopic(Topic topic) {
        this.topicMapper.updateById(topic);
    }

    public void saveTopic(Topic topic) {
        this.topicMapper.insert(topic);
    }
}
