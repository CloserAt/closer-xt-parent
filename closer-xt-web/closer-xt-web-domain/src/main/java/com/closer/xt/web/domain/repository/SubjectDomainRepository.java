package com.closer.xt.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.closer.xt.pojo.Subject;
import com.closer.xt.web.dao.SubjectMapper;
import com.closer.xt.web.domain.SubjectDomain;
import com.closer.xt.web.model.SubjectModel;
import com.closer.xt.web.model.enums.Status;
import com.closer.xt.web.model.params.SubjectParams;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubjectDomainRepository {
    @Autowired
    private SubjectMapper subjectMapper;

    public SubjectDomain createDomain(SubjectParams subjectParams) {
        return new SubjectDomain(this, subjectParams);
    }

    public List<Subject> findSubjectList() {
        LambdaQueryWrapper<Subject> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Subject::getStatus, Status.NORMAL.getCode());
        List<Subject> subjectList = this.subjectMapper.selectList(queryWrapper);
        return subjectList;
    }

    public List<Subject> findSubjectListByCourseId(Long courseId) {
        return this.subjectMapper.findSubjectListByCourseId(courseId);
    }
}
