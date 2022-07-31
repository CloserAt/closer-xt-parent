package com.closer.xt.web.impl;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.service.AbstractTemplateAction;
import com.closer.xt.web.domain.NewsDomain;
import com.closer.xt.web.domain.repository.NewsDomainRepository;
import com.closer.xt.web.model.params.NewsParams;
import com.closer.xt.web.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl extends AbstractService implements NewsService {
    @Autowired
    private NewsDomainRepository newsDomainRepository;

    @Override
    public CallResult newsList(NewsParams newsParams) {
        NewsDomain newsDomain = newsDomainRepository.createDomain(newsParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            //参数检测
            @Override
            public CallResult<Object> checkParam() {
                return newsDomain.checkNewsListParams();
            }

            @Override
            public CallResult<Object> doAction() {
                return newsDomain.newsList();
            }
        });
    }

    @Override
    public CallResult newsById(NewsParams newsParams) {
        NewsDomain newsDomain = newsDomainRepository.createDomain(newsParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return newsDomain.newsDetailById();
            }
        });
    }
}
