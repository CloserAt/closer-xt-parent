package com.closer.xt.admin.service.impl;

import com.closer.xt.admin.domain.TopicDomain;
import com.closer.xt.admin.domain.repository.TopicDomainRepository;
import com.closer.xt.admin.model.TopicModel;
import com.closer.xt.admin.params.TopicParams;
import com.closer.xt.admin.service.TopicService;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.service.AbstractTemplateAction;
import com.closer.xt.pojo.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TopicServiceImpl extends AbstractService implements TopicService {
    @Autowired
    private TopicDomainRepository topicDomainRepository;

    @Override
    public CallResult findTopicList(TopicParams topicParams) {
        TopicDomain topicDomain = topicDomainRepository.createDomain(topicParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return topicDomain.findTopicList(topicParams);
            }
        });
    }

    @Override
    public Topic findTopicByTitle(String topicTitle) {
        TopicDomain topicDomain = topicDomainRepository.createDomain(null);
        CallResult<Topic> topicCallResult = this.serviceTemplate.executeQuery(new AbstractTemplateAction<Topic>() {
            @Override
            public CallResult<Topic> doAction() {
                return topicDomain.findTopicByTitle(topicTitle);
            }
        });
        if (topicCallResult.isSuccess()){
            return topicCallResult.getResult();
        }
        return null;
    }

    @Override
    public void updateTopic(Topic topic) {
        TopicDomain topicDomain = topicDomainRepository.createDomain(null);
        this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return topicDomain.updateTopic(topic);
            }
        });
    }

    @Override
    public void saveTopic(Topic topic) {
        TopicDomain topicDomain = topicDomainRepository.createDomain(null);
        this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return topicDomain.savaTopic(topic);
            }
        });
    }
}
