package com.closer.xt.admin.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.admin.domain.repository.SubjectDomainRepository;
import com.closer.xt.admin.model.ListPageModel;
import com.closer.xt.admin.model.NewsModel;
import com.closer.xt.admin.model.SubjectModel;
import com.closer.xt.admin.params.SubjectParams;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.pojo.News;
import com.closer.xt.pojo.Subject;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class SubjectDomain {
    private SubjectDomainRepository subjectDomainRepository;
    private SubjectParams subjectParams;
    public SubjectDomain(SubjectDomainRepository subjectDomainRepository, SubjectParams subjectParams) {
        this.subjectDomainRepository = subjectDomainRepository;
        this.subjectParams = subjectParams;
    }


    public CallResult<Object> findSubjectPage(SubjectParams subjectParams) {
        int currentPage = this.subjectParams.getCurrentPage();
        int pageSize = this.subjectParams.getPageSize();
        String queryString = this.subjectParams.getQueryString();
        //mybatisplus的page对象
        Page<Subject> subjectPage = this.subjectDomainRepository.findSubjectPageByCondition(currentPage, pageSize, queryString);

        //SubjectModel--跟页面进行数据交互的类
        ListPageModel<SubjectModel> listPageModel = new ListPageModel<>();
        List<Subject> records = subjectPage.getRecords();

        //将学科名称的信息拼接上-学科年级-学科学期
        List<SubjectModel> list = copyList(records);
        list.forEach(SubjectModel::fillSubjectName);//lamda表达式法

        listPageModel.setList(list);
        listPageModel.setSize(subjectPage.getSize());
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
}
