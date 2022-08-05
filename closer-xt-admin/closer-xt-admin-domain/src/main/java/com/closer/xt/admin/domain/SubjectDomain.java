package com.closer.xt.admin.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.admin.domain.repository.SubjectDomainRepository;
import com.closer.xt.admin.model.ListPageModel;
import com.closer.xt.admin.model.SubjectModel;
import com.closer.xt.admin.params.SubjectParams;
import com.closer.xt.common.enums.Status;
import com.closer.xt.common.model.BusinessCodeEnum;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.pojo.Subject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SubjectDomain {
    private SubjectDomainRepository subjectDomainRepository;
    private SubjectParams subjectParams;
    public SubjectDomain(SubjectDomainRepository subjectDomainRepository, SubjectParams subjectParams) {
        this.subjectDomainRepository = subjectDomainRepository;
        this.subjectParams = subjectParams;
    }


    public CallResult<Object> findSubjectPage(SubjectParams subjectParams) {
        int currentPage = subjectParams.getCurrentPage();
        int pageSize = subjectParams.getPageSize();
        String queryString = subjectParams.getQueryString();
        //mybatisplus的page对象
        Page<Subject> subjectPage = this.subjectDomainRepository.findSubjectPageByCondition(currentPage, pageSize, queryString);

        //SubjectModel--跟页面进行数据交互的类
        ListPageModel<SubjectModel> listPageModel = new ListPageModel<>();
        List<Subject> records = subjectPage.getRecords();

        //将学科名称的信息拼接上-学科年级-学科学期
        List<SubjectModel> list = copyList(records);
        list.forEach(SubjectModel::fillSubjectName);//lambda表达式法

        listPageModel.setList(list);
        log.info("总数据数："+subjectPage.getTotal());
        listPageModel.setSize(subjectPage.getTotal());//Total代表总条目数
        return CallResult.success(listPageModel);
    }


    public List<SubjectModel> copyList(List<Subject> subjectList){
        List<SubjectModel> subjectModelList = new ArrayList<>();
        for (Subject subject : subjectList) {
            SubjectModel target = new SubjectModel();
            BeanUtils.copyProperties(subject, target);
            subjectModelList.add(target);
        }
        return subjectModelList;
    }

    public CallResult<Object> checkSaveSubjectParam() {
        String subjectName = this.subjectParams.getSubjectName();
        String subjectGrade = this.subjectParams.getSubjectGrade();
        String subjectTerm = this.subjectParams.getSubjectTerm();
        if (StringUtils.isBlank(subjectName)
                || StringUtils.isBlank(subjectGrade)
                || StringUtils.isBlank(subjectTerm)) {
            return CallResult.fail(BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getCode(), "参数不能为空");
        }
        return CallResult.success();
    }

    public CallResult<Object> checkSaveSubjectBiz(SubjectParams subjectParams) {
        String subjectName = this.subjectParams.getSubjectName();
        String subjectGrade = this.subjectParams.getSubjectGrade();
        String subjectTerm = this.subjectParams.getSubjectTerm();
        Subject subject = this.subjectDomainRepository.findSubjectByCondition(subjectName, subjectGrade, subjectTerm);
        if (subject != null) {
            //说明查到了数据，重复了
            return CallResult.fail(BusinessCodeEnum.CHECK_BIZ_NO_RESULT.getCode(),"重复添加");
        }
        return CallResult.success();
    }

    public CallResult<Object> saveSubjectDoAction(SubjectParams subjectParams) {
        Subject subject = new Subject();
        BeanUtils.copyProperties(subjectParams, subject);
        subject.setStatus(Status.NORMAL.getCode());
        this.subjectDomainRepository.saveSubject(subject);
        List<Integer> subjectUnits = this.subjectParams.getSubjectUnits();
        for (Integer subjectUnit : subjectUnits) {
            this.subjectDomainRepository.saveSubjectUnits(subject.getId(),subjectUnit);
        }
        return CallResult.success();
    }



    public CallResult<Object> findSubjectById(SubjectParams subjectParams) {
        /*
        1.查询subject表
        2.查询subjectUnit表
         */
        //第一步
        Subject subject = this.subjectDomainRepository.findSubjectBySubjectId(subjectParams.getId());
        SubjectModel subjectModel = copy(subject);

        //第二步
        List<Integer> list = this.subjectDomainRepository.findSubjectUnitsBySubjectId(subjectParams.getId());
        subjectModel.setSubjectUnits(list);

        return CallResult.success(subjectModel);
    }

    private SubjectModel copy(Subject subject) {
        SubjectModel subjectModel = new SubjectModel();
        BeanUtils.copyProperties(subject, subjectModel);
        return subjectModel;
    }

    public CallResult<Object> update(SubjectParams subjectParams) {
        /*
        1.更新subject表
        2.更新单元表前，先删除原有的关联关系，再更新
         */
        Subject subject = new Subject();
        BeanUtils.copyProperties(subjectParams, subject);
        /*
        判断重复
         */
        String subjectName = this.subjectParams.getSubjectName();
        String subjectGrade = this.subjectParams.getSubjectGrade();
        String subjectTerm = this.subjectParams.getSubjectTerm();
        Subject newSubject = this.subjectDomainRepository.findSubjectByCondition(subjectName, subjectGrade, subjectTerm);
        if (newSubject != null && !newSubject.getId().equals(subject.getId())) {
            return CallResult.fail(-999,"请勿重复添加数据");
        }
        //TODO 差一个逻辑，学科和课程是有关联的，如果课程使用了学科，是不能将学科删除的

        this.subjectDomainRepository.updateSubject(subject);

        //先删单元表后新增
        this.subjectDomainRepository.deleteUnitBySubjectId(subjectParams);//删除
        //新增
        List<Integer> subjectUnits = this.subjectParams.getSubjectUnits();
        for (Integer subjectUnit : subjectUnits) {
            this.subjectDomainRepository.saveSubjectUnits(subject.getId(),subjectUnit);
        }
        return CallResult.success(subject);
    }

    public CallResult<Object> allSubjectList(SubjectParams subjectParams) {
         List<Subject> subjectList =  this.subjectDomainRepository.findAll(subjectParams);
         List<SubjectModel> result = copyList(subjectList);
         result.forEach(SubjectModel::fillSubjectName);
         return CallResult.success(result);
    }

    public List<Subject> findAllSubjectList() {
        return this.subjectDomainRepository.findAll(this.subjectParams);
    }

    public List<Subject> findSubjectListByCourseId(Long courseId) {

        return this.subjectDomainRepository.findSubjectListByCourseId(courseId);
    }

    public List<SubjectModel> allSubjectModelList() {
        List<Subject> all = subjectDomainRepository.findAll(subjectParams);
        return copyList(all);
    }

    public List<String> allSubjectIdList() {
        List<Subject> all = this.subjectDomainRepository.findAll(subjectParams);
        return all.stream().map(subject -> subject.getId().toString()).collect(Collectors.toList());
    }
}
