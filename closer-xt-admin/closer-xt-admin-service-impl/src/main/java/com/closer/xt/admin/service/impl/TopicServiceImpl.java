package com.closer.xt.admin.service.impl;

import com.closer.xt.admin.domain.TopicDomain;
import com.closer.xt.admin.domain.repository.TopicDomainRepository;
import com.closer.xt.admin.params.TopicParams;
import com.closer.xt.admin.service.TopicService;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.service.AbstractTemplateAction;
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
}
