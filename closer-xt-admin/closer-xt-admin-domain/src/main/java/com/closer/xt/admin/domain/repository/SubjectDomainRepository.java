package com.closer.xt.admin.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.admin.dao.SubjectMapper;
import com.closer.xt.admin.domain.NewsDomain;
import com.closer.xt.admin.domain.SubjectDomain;
import com.closer.xt.admin.domain.qiniuyun.QiniuConfig;
import com.closer.xt.admin.params.NewsParams;
import com.closer.xt.admin.params.SubjectParams;
import com.closer.xt.pojo.Subject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubjectDomainRepository {
    @Autowired
    public QiniuConfig qiniuConfig;

    @Autowired
    private SubjectMapper subjectMapper;

    public SubjectDomain createDomain(SubjectParams subjectParams) {
        return new SubjectDomain(this, subjectParams);
    }

    public Page<Subject> findSubjectPageByCondition(int currentPage, int pageSize, String queryString) {
        Page<Subject> subjectPage = new Page<>();
        LambdaQueryWrapper<Subject> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(queryString)) {
            queryWrapper.eq(Subject::getSubjectName, queryString);
        }
        return subjectMapper.selectPage(subjectPage, queryWrapper);
    }
}
