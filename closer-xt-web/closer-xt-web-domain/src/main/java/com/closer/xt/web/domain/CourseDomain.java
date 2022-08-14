package com.closer.xt.web.domain;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.common.Login.UserThreadLocal;
import com.closer.xt.common.model.BusinessCodeEnum;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.pojo.*;
import com.closer.xt.web.domain.repository.CourseDomainRepository;
import com.closer.xt.web.model.*;
import com.closer.xt.common.model.ListPageModel;
import com.closer.xt.web.model.enums.HistoryStatus;
import com.closer.xt.web.model.params.CourseParams;
import com.closer.xt.web.model.params.SubjectParams;
import com.closer.xt.web.model.params.UserCourseParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
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

    public CallResult<Object> courseList() {
        /**
         * 1. 如果根据年级进行查询，需要先找到年级对应的科目列表，根据科目列表去查询课程列表
         * 2. 如果年级为空，查询全部的课程即可
         * 3. 用户购买课程的信息，课程中科目的名称信息
         * 4. 判断用户是否登录，如果登录 去user_course 表 去查询相关信息
         * 5. 根据课程id，去查询对应的科目名称
         */
        int page = this.courseParams.getPage();
        int pageSize = this.courseParams.getPageSize();
        String subjectGrade = this.courseParams.getSubjectGrade();
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
            log.info("用户2："+userId);
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
            courseViewModel.setSubjectInfo(createSubjectModel(subjectModelList));
            courseViewModelList.add(courseViewModel);
        }
        ListPageModel<CourseViewModel> listPageModel = new ListPageModel<>();
        listPageModel.setSize(coursePage.getTotal());
        listPageModel.setPageCount(coursePage.getPages());
        listPageModel.setPage(page);
        listPageModel.setPageSize(pageSize);
        listPageModel.setList(courseViewModelList);
        return CallResult.success(listPageModel);
    }

    private SubjectModel createSubjectModel(List<SubjectModel> subjectModelList) {
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

    public CallResult<Object> subjectInfo() {
        /**
         * 1. 根据课程id 查询 学科列表
         * 2. 根据学科 查询对应的单元
         * 3. 返回对应的模型数据即可
         * 4. 不做的业务逻辑（如果此课程的学科已经在学习中，返回已经当初选择的单元）
         */

        Long userId = UserThreadLocal.get();
        log.info("userId是多少：？" + userId);
        Long courseId = this.courseParams.getCourseId();
        //根据id找到对应的课程

        List<SubjectModel> subjectList = this.courseDomainRepository.createSubjectDomain(new SubjectParams()).findSubjectListByCourseId(courseId);
        List<SubjectViewModel> subjectViewModelList = new ArrayList<>();
        for (SubjectModel subjectModel : subjectList) {
            //查询单元信息
            SubjectViewModel subjectViewModel = new SubjectViewModel();
            subjectViewModel.setId(subjectModel.getId());
            subjectViewModel.setSubjectName(subjectModel.getSubjectName());
            subjectViewModel.setSubjectGrade(subjectModel.getSubjectGrade());
            subjectViewModel.setSubjectTerm(subjectModel.getSubjectTerm());
            subjectViewModel.fillSubjectName();
            List<Integer> subjectUnitList = this.courseDomainRepository.createSubjectDomain(new SubjectParams()).findSubjectUnit(subjectModel.getId());
            subjectViewModel.setSubjectUnitList(subjectUnitList);

            if (userId != null) {
                UserHistory userHistory = this.courseDomainRepository.createUserHistoryDomain(null).findUserHistory(userId,subjectModel.getId(), HistoryStatus.NO_FINISH.getCode());
                if (userHistory != null) {
                    String subjectUnits = userHistory.getSubjectUnits();
                    if (StringUtils.isNotEmpty(subjectUnits)) {
                        List<Integer> strings = JSON.parseArray(subjectUnits, Integer.class);
                        subjectViewModel.setSubjectUnitSelectedList(strings);
                    }
                }
            }
            subjectViewModelList.add(subjectViewModel);
        }

        return CallResult.success(subjectViewModelList);
    }

    public CallResult<Object> checkSubjectInfoParam() {
        Long courseId = this.courseParams.getCourseId();
        Course course = this.courseDomainRepository.findCourseById(courseId);
        if (course == null){
            return CallResult.fail(BusinessCodeEnum.TOPIC_PARAM_ERROR.getCode(),"参数错误");
        }
        return CallResult.success();
    }

    public List<Long> findCourseIdBySubjectId(Long subjectId) {
        return courseDomainRepository.findCourseIdListBySubject(subjectId);
    }

    public CallResult<Object> courseDetail(CourseParams courseParams) {
        Long courseId = courseParams.getCourseId();
        Course course = this.courseDomainRepository.findCourseById(courseId);

        if (course == null) {
            return CallResult.fail(BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getCode(),"课程已下架");
        }
        CourseDetailModel courseDetailModel = new CourseDetailModel();
        courseDetailModel.setCourseId(courseId);
        courseDetailModel.setCourseName(course.getCourseName());
        courseDetailModel.setCourseTime(course.getOrderTime());
        courseDetailModel.setPrice(course.getCourseZhePrice());

        //根据课程id去subject表中找到所有相关的subject，形成list集合
        List<SubjectModel> subjectModelList = this.courseDomainRepository.createSubjectDomain(new SubjectParams()).findSubjectListByCourseId(courseId);
        StringBuilder subjectStr = new StringBuilder();
        for (SubjectModel subjectModel : subjectModelList) {
            subjectStr.append(subjectModel.getSubjectName())
                    .append("-")
                    .append(subjectModel.getSubjectGrade())
                    .append("-")
                    .append(subjectModel.getSubjectTerm())
                    .append(" ,");
        }
        String subjectInfo = subjectStr.toString().substring(0,subjectStr.length() - 1);
        courseDetailModel.setSubjectInfo(subjectInfo);

        return CallResult.success(courseDetailModel);
    }

    public CallResult<Object> myCoupon(CourseParams courseParams) {
        /**
         * 1. 根据课程和当前的登录用户id 查询用户所有可用的优惠券
         * 2. 判断优惠券是否可用 开始时间 不等于-1 过期时间 不等于-1
         */
        Long userId = UserThreadLocal.get();
        Long courseId = courseParams.getCourseId();
        Course course = this.courseDomainRepository.findCourseById(courseId);
        if (course == null) {
            return CallResult.fail(BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getCode(),"课程已下架");
        }
        List<UserCoupon> userCouponList = this.courseDomainRepository.createCouponDomain(null).findUserCoupon(userId);
        List<UserCouponModel> userCouponModelList = new ArrayList<>();
        for (UserCoupon userCoupon : userCouponList) {
            //遍历每张优惠券并判断优惠券是否过期
            Long startTime = userCoupon.getStartTime();
            Long expireTime = userCoupon.getExpireTime();
            long currentTimeMillis = System.currentTimeMillis();
            if (startTime != -1 && currentTimeMillis < startTime) continue;//时间没到不能用
            if (expireTime != -1 && currentTimeMillis > expireTime) continue;//过期了不能用

            //判断优惠券是否能够满减，是否被使用
            Long couponId = userCoupon.getCouponId();
            Coupon coupon = this.courseDomainRepository.createCouponDomain(null).findCouponByCouponId(couponId);
            if (coupon == null){
                continue;
            }
            Integer status = coupon.getStatus();
            if (2 == status){
                continue;
            }
            //判断满减
            Integer disStatus = coupon.getDisStatus();
            if (1 == disStatus) {
                //需要满足满减条件
                BigDecimal courseZhePrice = course.getCourseZhePrice();
                if (coupon.getMax().compareTo(courseZhePrice) > 0) {
                    //最大满减，大雨课程价格则不可用
                    continue;
                }
            }
            UserCouponModel userCouponModel = new UserCouponModel();
            userCouponModel.setName(coupon.getName());
            userCouponModel.setCouponId(couponId);
            userCouponModel.setAmount(coupon.getPrice());

            userCouponModelList.add(userCouponModel);
        }
        return CallResult.success(userCouponList);
    }

    public Course findCourseByCourseId(Long courseId) {
        return this.courseDomainRepository.findCourseById(courseId);
    }

    public CourseViewModel findCourseViewModel(Long courseId) {
        Course course = this.courseDomainRepository.findCourseById(courseId);
        return copyViewModel(course);
    }

    private CourseViewModel copyViewModel(Course course) {
        CourseViewModel courseViewModel = new CourseViewModel();
        courseViewModel.setId(course.getId());
        courseViewModel.setCourseDesc(course.getCourseDesc());
        courseViewModel.setCourseName(course.getCourseName());
        courseViewModel.setCoursePrice(course.getCoursePrice());
        courseViewModel.setCourseZhePrice(course.getCourseZhePrice());
        courseViewModel.setOrderTime(course.getOrderTime());
        courseViewModel.setImageUrl(course.getImageUrl());
        List<SubjectModel> subjectModelList = courseDomainRepository.createSubjectDomain(null).findSubjectListByCourseId(course.getId());
        courseViewModel.setSubjectList(subjectModelList);
        return courseViewModel;
    }

    public CallResult<Object> myCourse() {
        Long userId = UserThreadLocal.get();
        List<UserCourse> userCourseList = this.courseDomainRepository.createUserCourseDomain(null).findUserCourseList(userId);
        List<UserCourseModel> userCourseModelList = new ArrayList<>();
        long currentTimeMillis = System.currentTimeMillis();
        for (UserCourse userCourse : userCourseList) {
            UserCourseModel userCourseModel = new UserCourseModel();
            userCourseModel.setCourseId(userCourse.getCourseId());
            Course course = this.courseDomainRepository.findCourseById(userCourse.getCourseId());
            userCourseModel.setCourseName(course.getCourseName());
            if (currentTimeMillis > userCourse.getExpireTime()) {
                userCourseModel.setStatus(2);
            } else {
                userCourseModel.setStatus(1);
            }
            userCourseModel.setExpireTime(new DateTime(userCourse.getExpireTime()).toString("yyyy年MM月dd日"));
            userCourseModel.setBuyTime(new DateTime(userCourse.getCreateTime()).toString("yyyy年MM月dd日"));
            List<SubjectModel> subjectInfoByCourseId = this.courseDomainRepository.createSubjectDomain(new SubjectParams()).findSubjectListByCourseId(userCourse.getCourseId());

            Integer count = this.courseDomainRepository.createUserHistoryDomain(null).countUserHistoryBySubjectList(userId,subjectInfoByCourseId);
            userCourseModel.setStudyCount(count);

            userCourseModel.setCourseId(course.getId());
            userCourseModelList.add(userCourseModel);
        }
        return CallResult.success(userCourseModelList);
    }

    public List<Long> findCourseIdListBySubjectId(Long subjectId) {
        return this.courseDomainRepository.findCourseIdListBySubject(subjectId);
    }
}
