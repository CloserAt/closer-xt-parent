package com.closer.xt.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.closer.xt.pojo.Subject;
import com.closer.xt.pojo.SubjectUnit;
import com.closer.xt.web.dao.SubjectMapper;
import com.closer.xt.web.dao.SubjectUnitMapper;
import com.closer.xt.web.domain.SubjectDomain;
import com.closer.xt.web.model.SubjectModel;
import com.closer.xt.web.model.enums.Status;
import com.closer.xt.web.model.params.SubjectParams;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubjectDomainRepository {
    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private SubjectUnitMapper subjectUnitMapper;

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

    public List<SubjectUnit> findSubjectUnitBySubjectId(Long subjectId) {
        LambdaQueryWrapper<SubjectUnit> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SubjectUnit::getSubjectId,subjectId);
        return this.subjectUnitMapper.selectList(queryWrapper);
    }

    public Subject findSubjectBySubjectId(Long subjectId) {
        return subjectMapper.selectById(subjectId);
    }

    public Long findSubjectByInfo(String subjectName, String subjectGrade, String subjectTerm) {
        LambdaQueryWrapper<Subject> queryWrapper = Wrappers.lambdaQuery();
        boolean isNull = true;
        if (StringUtils.isNotBlank(subjectName)){
            queryWrapper.eq(Subject::getSubjectName,subjectName);
            isNull = false;
        }
        if (StringUtils.isNotBlank(subjectGrade)){
            queryWrapper.eq(Subject::getSubjectGrade,subjectGrade);
            isNull = false;
        }
        if (StringUtils.isNotBlank(subjectTerm)){
            queryWrapper.eq(Subject::getSubjectTerm,subjectTerm);
            isNull = false;
        }
        if (isNull) {
            return null;
        }
        Subject subject = this.subjectMapper.selectOne(queryWrapper);
        return subject == null ? null : subject.getId();
    }
}
