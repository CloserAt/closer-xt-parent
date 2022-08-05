package com.closer.xt.web.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.common.Login.UserThreadLocal;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.pojo.Course;
import com.closer.xt.pojo.UserCourse;
import com.closer.xt.web.domain.repository.CourseDomainRepository;
import com.closer.xt.web.model.CourseViewModel;
import com.closer.xt.web.model.ListPageModel;
import com.closer.xt.web.model.SubjectModel;
import com.closer.xt.web.model.params.CourseParams;
import com.closer.xt.web.model.params.SubjectParams;
import com.closer.xt.web.model.params.UserCourseParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
@Slf4j
public class CourseDomain {
    private CourseDomainRepository courseDomainRepository;
    private CourseParams courseParams;
    public CourseDomain(CourseDomainRepository courseDomainRepository, CourseParams courseParams) {
        this.courseDomainRepository = courseDomainRepository;
        this.courseParams = courseParams;
    }

    public CallResult<Object> courseList(CourseParams courseParams) {
        /**
         * 1. 如果根据年级进行查询，需要先找到年级对应的科目列表，根据科目列表去查询课程列表
         * 2. 如果年级为空，查询全部的课程即可
         * 3. 用户购买课程的信息，课程中科目的名称信息
         * 4. 判断用户是否登录，如果登录 去user_course 表 去查询相关信息
         * 5. 根据课程id，去查询对应的科目名称
         */
        int page = courseParams.getPage();
        int pageSize = courseParams.getPageSize();
        String subjectGrade = courseParams.getSubjectGrade();
        Page<Course> coursePage;
        //判断年级是否为空，为空则查询全部课程
        if (StringUtils.isNotBlank(subjectGrade)) {
            coursePage = this.courseDomainRepository.findCourseByGrade(page,pageSize,subjectGrade);
        } else {
            coursePage = this.courseDomainRepository.findAllCourse(page,pageSize);
        }
        List<Course> records = coursePage.getRecords();//获得所有数据记录
        List<CourseViewModel> courseViewModelList = new ArrayList<>();

        for (Course record : records) {
            CourseViewModel courseViewModel = new CourseViewModel();
            BeanUtils.copyProperties(record,courseViewModel);
            //购买数量，需要去UserCourse表中查询
            long studyCount = this.courseDomainRepository.createUserCourseDomain(new UserCourseParams()).countUserCourseByCourseId(record.getId());
            courseViewModel.setStudyCount((int) studyCount);

            //获取用户id，验证是否登录
            Long userId = UserThreadLocal.get();
            log.info("用户："+userId);
            if (userId != null) {
                //代表已登录,则去UserCourse表中获取用户课程
                UserCourse userCourse =this.courseDomainRepository.createUserCourseDomain(new UserCourseParams()).findUserCourse(userId,record.getId(),System.currentTimeMillis());
                //验证用户课程是否为空,然后设置buy属性
                if (userCourse == null) {
                    courseViewModel.setBuy(0);
                } else {
                    courseViewModel.setBuy(1);
                    courseViewModel.setExpireTime(new DateTime(userCourse.getExpireTime()).toString("yyyy-MM-dd"));
                    log.info("过期时间："+userCourse.getExpireTime());
                }
            }
            //根据课程id找对应的科目信息
            List<SubjectModel> subjectModelList = this.courseDomainRepository.createSubjectDomain(new SubjectParams()).findSubjectListByCourseId(record.getId());
            courseViewModel.setSubjectList(subjectModelList);
            courseViewModel.setSubjectInfo(creteSubjectModel(subjectModelList));
            courseViewModelList.add(courseViewModel);
        }
        ListPageModel<CourseViewModel> listPageModel = new ListPageModel<>();
        listPageModel.setSize(coursePage.getTotal());
        listPageModel.setPage(page);
        listPageModel.setPageCount(coursePage.getPages());
        listPageModel.setPageSize(pageSize);
        listPageModel.setList(courseViewModelList);
        return CallResult.success(listPageModel);
    }

    private SubjectModel creteSubjectModel(List<SubjectModel> subjectModelList) {
        SubjectModel subjectModel = new SubjectModel();
        StringBuilder nameBuilder = new StringBuilder();
        StringBuilder termBuilder = new StringBuilder();
        for (SubjectModel model : subjectModelList) {
            if (!nameBuilder.toString().contains(model.getSubjectName())) {
                nameBuilder.append(model.getSubjectName()).append(",");
            }
            if (!termBuilder.toString().contains(model.getSubjectTerm())) {
                termBuilder.append(model.getSubjectTerm()).append(",");
            }
        }
        String name = nameBuilder.toString();
        subjectModel.setSubjectName(name.substring(0,name.lastIndexOf(",")));
        subjectModel.setSubjectGrade(subjectModelList.get(0).getSubjectGrade());
        String term = termBuilder.toString();
        subjectModel.setSubjectTerm(term.substring(0,term.lastIndexOf(",")));
        return subjectModel;
    }
}
