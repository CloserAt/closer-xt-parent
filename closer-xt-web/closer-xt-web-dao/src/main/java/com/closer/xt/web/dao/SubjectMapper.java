package com.closer.xt.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.closer.xt.pojo.Subject;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SubjectMapper extends BaseMapper<Subject> {
    List<Subject> findSubjectListByCourseId(Long courseId);
}
