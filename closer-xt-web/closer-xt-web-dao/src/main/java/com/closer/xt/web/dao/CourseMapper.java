package com.closer.xt.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.pojo.Course;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {
    Page<Course> findCourseByGrade(Page<Course> page, String subjectGrade);
}
