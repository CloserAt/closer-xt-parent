package com.closer.xt.admin.service.impl;

import com.closer.xt.admin.domain.NewsDomain;
import com.closer.xt.admin.domain.repository.NewsDomainRepository;
import com.closer.xt.admin.params.NewsParams;
import com.closer.xt.admin.service.NewsService;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.service.AbstractTemplateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewsServiceImpl extends AbstractService implements NewsService {
    @Autowired
    private NewsDomainRepository newsDomainRepository;

    @Override
    public CallResult findPage(NewsParams newsParams) {
        NewsDomain newsDomain = newsDomainRepository.createDomain(newsParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return newsDomain.findNewsPage();
            }
        });
    }

    @Override
    @Transactional//serviceTemplate.execute的操作必须加事务
    public CallResult saveNews(NewsParams newsParams) {
        NewsDomain newsDomain = newsDomainRepository.createDomain(newsParams);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return newsDomain.saveNewNews();
            }
        });
    }

    @Override
    public CallResult findNewsById(NewsParams newsParams) {
        NewsDomain newsDomain = newsDomainRepository.createDomain(newsParams);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return newsDomain.findNewsById();
            }
        });
    }

    @Override
    public CallResult update(NewsParams newsParams) {
        NewsDomain newsDomain = newsDomainRepository.createDomain(newsParams);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return newsDomain.update();
            }
        });
    }

    @Override
    public CallResult delete(NewsParams newsParams) {
        NewsDomain newsDomain = newsDomainRepository.createDomain(newsParams);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return newsDomain.delete(newsParams);
            }
        });
    }
}
