package com.closer.xt.admin.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.admin.domain.repository.CourseDomainRepository;
import com.closer.xt.admin.model.CourseModel;
import com.closer.xt.admin.model.ListPageModel;
import com.closer.xt.admin.params.CourseParams;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.model.ListModel;
import com.closer.xt.pojo.Course;
import com.closer.xt.pojo.Subject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CourseDomain {
    private CourseDomainRepository courseDomainRepository;
    private CourseParams courseParams;
    public CourseDomain(CourseDomainRepository courseDomainRepository, CourseParams courseParams) {
        this.courseDomainRepository = courseDomainRepository;
        this.courseParams = courseParams;
    }

    private CourseModel copy(Course course){
        CourseModel courseModel = new CourseModel();
        BeanUtils.copyProperties(course,courseModel);
        courseModel.setId(course.getId().toString());
        return courseModel;
    }
//    private List<CourseModel> copyList(List<Course> courseList){
//        if (courseList == null || courseList.size() == 0){
//            return new ArrayList<>();
//        }
//        List<Subject> subjectList = this.courseDomainRepository.createSubjectDomain(null).allSubjectList();
//        List<CourseModel> courseModelList = new ArrayList<>();
//        for (Course course : courseList){
//            CourseModel model = copy(course);
//            model.setSubjects(displaySubjects(subjectList,course.getSubjects()));
//            courseModelList.add(model);
//        }
//        return courseModelList;
//    }
    private String displaySubjects(List<Subject> subjectList, String subjects) {
        String[] str = StringUtils.split(subjects, ",");
        String ds = "";
        Map<String,String> map = new HashMap<>();
        for (Subject subject : subjectList) {
            map.put(String.valueOf(subject.getId()), subject.getSubjectName());
        }
        for (String s : str) {
            if (StringUtils.isEmpty(ds)) {
                ds = map.get(s);
            } else {
                ds += "," + map.get(s);
            }
        }
        return ds;
    }
    private String displayCourseStatus(Integer courseStatus) {
        if (0 == courseStatus) {
            return "正常";
        }
        if (1 == courseStatus) {
            return "下架";
        }
        return "无此状态";
    }


    public CallResult<Object> findPage(CourseParams courseParams) {
        int currentPage = courseParams.getPage();
        int pageSize = courseParams.getPageSize();
        //String queryString = courseParams.getQueryString();

        Page<Course> coursePage = this.courseDomainRepository.findCourseListPage(currentPage, pageSize);

        ListPageModel<CourseModel> listPageModel = new ListPageModel<>();
        List<Course> records = coursePage.getRecords();//查询到的记录拿出来
        List<CourseModel> courseModelList = new ArrayList<>();
        for (Course record : records) {
            CourseModel courseModel = copy(record);
            courseModelList.add(courseModel);
        }
        log.info("总数居数：" + coursePage.getTotal());
        //TODO Bug:此处查找的数据多了
        listPageModel.setSize(coursePage.getTotal());
        listPageModel.setList(courseModelList);
        return CallResult.success(listPageModel);
    }

    public CallResult<Object> findCourseById(CourseParams courseParams) {
        Course course = this.courseDomainRepository.findCourseById(courseParams);
        CourseModel courseModel = copy(course);

        List<Subject> subjectList = this.courseDomainRepository.createSubjectDomain(null).findSubjectListByCourseId(course.getId());
        List<Long> subjectIdList = new ArrayList<>();
        for (Subject subject : subjectList) {
            subjectIdList.add(subject.getId());
        }
        courseModel.setSubjectIdList(subjectIdList);
        courseModel.setSubjectList(this.courseDomainRepository.createSubjectDomain(null).allSubjectModelList());
        return CallResult.success(courseModel);
    }

    public CallResult<Object> saveCourse(CourseParams courseParams) {
        String courseName = courseParams.getCourseName();
        if (courseName == null) courseName = "";
        String courseDesc = courseParams.getCourseDesc();
        if (courseDesc == null) courseDesc = "";
        List<Long> subjectIdList = courseParams.getSubjectIdList();
        String subject = "";
        for (Long aLong : subjectIdList) {
            subject += aLong + ",";
        }
        if (!StringUtils.isEmpty(subject)) {
            subject = subject.substring(0, subject.length() - 1);
        }
        if (StringUtils.isEmpty(subject)) {
            return CallResult.fail(-999,"请添加学科");
        }
        List<String> allSubjectList = this.courseDomainRepository.createSubjectDomain(null).allSubjectIdList();

        String[] str = StringUtils.split(subject, ",");
        for (String subjectId : str) {
            if (!allSubjectList.contains(subjectId)) {
                return CallResult.fail(-999,"学科不存在");
            }
        }
        String imageUrl = courseParams.getImageUrl();
        if (StringUtils.isEmpty(imageUrl)) {
            imageUrl = "";
        }
        BigDecimal coursePrice = courseParams.getCoursePrice();
        BigDecimal courseZhePrice = courseParams.getCourseZhePrice();
        Integer courseStatus = courseParams.getCourseStatus();
        Integer orderTime = courseParams.getOrderTime();

        Course course = new Course();
        course.setCourseName(courseName);
        course.setCourseDesc(courseDesc);
        course.setSubjects(subject);
        course.setCoursePrice(coursePrice);
        course.setCourseZhePrice(courseZhePrice);
        course.setCourseStatus(courseStatus);
        course.setOrderTime(orderTime);
        course.setImageUrl(imageUrl);

        this.courseDomainRepository.saveCourse(course);
        for (Long subjectId : subjectIdList) {
            this.courseDomainRepository.saveCourseSubject(subjectId, course.getId());
        }
        return CallResult.success();
    }

    public CallResult<Object> updateCourse(CourseParams courseParams) {
        Course course = this.courseDomainRepository.findCourseById(courseParams);
        if (course == null) {
            return CallResult.fail(-999,"课程不存在");
        }
        String courseName = courseParams.getCourseName();
        if (courseName == null) {
            courseName = "";
        }
        String courseDesc = courseParams.getCourseDesc();
        if (courseDesc == null) {
            courseDesc = "";
        }
        List<Long> subjectIdList = courseParams.getSubjectIdList();
        String subjects = "";
        for (Long subjectId : subjectIdList) {
            subjects += subjectId + ",";
        }
        if (!StringUtils.isEmpty(subjects)) {
            subjects = subjects.substring(0, subjects.length() - 1);
        }
        if (courseParams.getOrderTime() != null) {
            course.setOrderTime(courseParams.getOrderTime());
        }
        String imageUrl = courseParams.getImageUrl();
        if (!StringUtils.isEmpty(imageUrl)) {
            course.setImageUrl(imageUrl);
        }
        BigDecimal coursePrice = courseParams.getCoursePrice();
        BigDecimal courseZhePrice = courseParams.getCourseZhePrice();
        Integer courseStatus = courseParams.getCourseStatus();

        course.setCourseName(courseName);
        course.setCourseDesc(courseDesc);
        course.setSubjects(subjects);
        course.setCoursePrice(coursePrice);
        course.setCourseZhePrice(courseZhePrice);
        course.setCourseStatus(courseStatus);
        course.setOrderTime(courseParams.getOrderTime());
        this.courseDomainRepository.updateCourse(course);
        this.courseDomainRepository.deleteCourseSubject(course.getId());
        for (Long subjectId : subjectIdList) {
            this.courseDomainRepository.saveCourseSubject(subjectId, course.getId());
        }
        return CallResult.success();
    }
}
