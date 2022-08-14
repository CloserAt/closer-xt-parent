package com.closer.xt.web.impl;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.service.AbstractTemplateAction;
import com.closer.xt.web.domain.TopicDomain;
import com.closer.xt.web.domain.repository.TopicDomainRepository;
import com.closer.xt.web.model.params.TopicParams;
import com.closer.xt.web.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TopicServiceImpl extends AbstractService implements TopicService {
    @Autowired
    private TopicDomainRepository topicDomainRepository;

    @Override
    @Transactional
    public CallResult practice(TopicParams topicParams) {
        TopicDomain topicDomain = this.topicDomainRepository.createDomain(topicParams);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> checkBiz() {
                return topicDomain.checkPracticeBiz();
            }

            @Override
            public CallResult<Object> doAction() {
                return topicDomain.practice(topicParams);
            }
        });
    }

    @Override
    @Transactional
    public CallResult submit(TopicParams topicParams) {
        TopicDomain topicDomain = this.topicDomainRepository.createDomain(topicParams);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> checkBiz() {
                return topicDomain.checkSubmitBiz();
            }

            @Override
            public CallResult<Object> doAction() {
                return topicDomain.submit(topicParams);
            }
        });
    }

    @Override
    public CallResult jump(TopicParams topicParams) {
        TopicDomain topicDomain = this.topicDomainRepository.createDomain(topicParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return topicDomain.jump(topicParams);
            }
        });
    }

    @Override
    public CallResult practiceHistory(TopicParams topicParams) {
        TopicDomain topicDomain = this.topicDomainRepository.createDomain(topicParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return topicDomain.practiceHistory(topicParams);
            }
        });
    }

    @Override
    public CallResult userProblemSearch(TopicParams topicParams) {
        TopicDomain topicDomain = this.topicDomainRepository.createDomain(topicParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return topicDomain.userProblemSearch(topicParams);
            }
        });
    }
}
