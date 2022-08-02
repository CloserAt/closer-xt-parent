package com.closer.xt.admin.service.impl;

import com.closer.xt.admin.domain.SubjectDomain;
import com.closer.xt.admin.domain.repository.SubjectDomainRepository;
import com.closer.xt.admin.params.SubjectParams;
import com.closer.xt.admin.service.SubjectService;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.service.AbstractTemplateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SubjectServiceImpl extends AbstractService implements SubjectService {
    @Autowired
    private SubjectDomainRepository subjectDomainRepository;

    @Override
    public CallResult findPage(SubjectParams subjectParams) {
        SubjectDomain subjectDomain = subjectDomainRepository.createDomain(subjectParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return subjectDomain.findSubjectPage(subjectParams);
            }
        });
    }

    @Override
    @Transactional
    public CallResult saveSubject(SubjectParams subjectParams) {
        SubjectDomain subjectDomain = subjectDomainRepository.createDomain(subjectParams);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            //判断参数不为空
            @Override
            public CallResult<Object> checkParam() {
                return subjectDomain.checkSaveSubjectParam();
            }

            //判断参数不重复
            @Override
            public CallResult<Object> checkBiz() {
                return subjectDomain.checkSaveSubjectBiz(subjectParams);
            }

            @Override
            public CallResult<Object> doAction() {
                return subjectDomain.saveSubjectDoAction(subjectParams);
            }
        });
    }

    @Override
    public CallResult findSubjectById(SubjectParams subjectParams) {
        SubjectDomain subjectDomain = subjectDomainRepository.createDomain(subjectParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return subjectDomain.findSubjectById(subjectParams);
            }
        });
    }

    @Override
    @Transactional
    public CallResult updateSubject(SubjectParams subjectParams) {
        SubjectDomain subjectDomain = subjectDomainRepository.createDomain(subjectParams);
        return this.serviceTemplate.execute(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> checkParam() {
                return subjectDomain.checkSaveSubjectParam();
            }

            @Override
            public CallResult<Object> doAction() {
                return subjectDomain.update(subjectParams);
            }
        });
    }

    @Override
    public CallResult allSubjectList(SubjectParams subjectParams) {
        SubjectDomain subjectDomain = subjectDomainRepository.createDomain(subjectParams);
        return this.serviceTemplate.executeQuery(new AbstractTemplateAction<Object>() {
            @Override
            public CallResult<Object> doAction() {
                return subjectDomain.allSubjectList(subjectParams);
            }
        });
    }
}
