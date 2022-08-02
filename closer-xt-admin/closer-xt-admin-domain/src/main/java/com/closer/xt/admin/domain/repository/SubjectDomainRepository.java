package com.closer.xt.admin.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.admin.dao.SubjectMapper;
import com.closer.xt.admin.dao.SubjectUnitMapper;
import com.closer.xt.admin.domain.NewsDomain;
import com.closer.xt.admin.domain.SubjectDomain;
import com.closer.xt.admin.domain.qiniuyun.QiniuConfig;
import com.closer.xt.admin.params.NewsParams;
import com.closer.xt.admin.params.SubjectParams;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.pojo.Subject;
import com.closer.xt.pojo.SubjectUnit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubjectDomainRepository {
    @Autowired
    public QiniuConfig qiniuConfig;

    @Autowired
    private SubjectMapper subjectMapper;

    @Autowired
    private SubjectUnitMapper subjectUnitMapper;

    public SubjectDomain createDomain(SubjectParams subjectParams) {
        return new SubjectDomain(this, subjectParams);
    }

    public Page<Subject> findSubjectPageByCondition(int currentPage, int pageSize, String queryString) {
        Page<Subject> subjectPage = new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<Subject> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(queryString)) {
            queryWrapper.eq(Subject::getSubjectName, queryString);
        }
        return subjectMapper.selectPage(subjectPage, queryWrapper);
    }


    public Subject findSubjectByCondition(String subjectName, String subjectGrade, String subjectTerm) {
        LambdaQueryWrapper<Subject> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Subject::getSubjectName,subjectName)
                .eq(Subject::getSubjectGrade,subjectGrade)
                .eq(Subject::getSubjectTerm,subjectTerm);
        queryWrapper.last("limit 1");
        return subjectMapper.selectOne(queryWrapper);
    }

    public void saveSubject(Subject subject) {
        this.subjectMapper.insert(subject);
    }

    public void saveSubjectUnits(Long subjectId, Integer subjectUnit) {
        SubjectUnit subjectUnit1 = new SubjectUnit();
        subjectUnit1.setSubjectId(subjectId);
        subjectUnit1.setSubjectUnit(subjectUnit);
        this.subjectUnitMapper.insert(subjectUnit1);
    }

    public Subject findSubjectBySubjectId(Long subjectId) {
        return subjectMapper.selectById(subjectId);
    }

    public List<Integer> findSubjectUnitsBySubjectId(Long id) {
        LambdaQueryWrapper<SubjectUnit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(SubjectUnit::getSubjectUnit);
        queryWrapper.eq(SubjectUnit::getSubjectId,id);
        List<SubjectUnit> subjectUnits = subjectUnitMapper.selectList(queryWrapper);
        return subjectUnits.stream().map(SubjectUnit::getSubjectUnit).collect(Collectors.toList());
    }

    public void updateSubject(Subject subject) {
        subjectMapper.updateById(subject);
    }

    public void deleteUnitBySubjectId(SubjectParams subjectParams) {
        Long subjectId = subjectParams.getId();
        LambdaQueryWrapper<SubjectUnit> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SubjectUnit::getSubjectId, subjectId);
        this.subjectUnitMapper.delete(queryWrapper);
    }

    public List<Subject> findAll(SubjectParams subjectParams) {
        return subjectMapper.selectList(new LambdaQueryWrapper<>());
    }
}
