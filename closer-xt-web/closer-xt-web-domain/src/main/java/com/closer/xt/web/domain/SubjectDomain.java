package com.closer.xt.web.domain;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.utils.CommonUtils;
import com.closer.xt.pojo.Subject;
import com.closer.xt.pojo.SubjectUnit;
import com.closer.xt.web.dao.SubjectUnitMapper;
import com.closer.xt.web.domain.repository.SubjectDomainRepository;
import com.closer.xt.web.model.SubjectModel;
import com.closer.xt.web.model.params.SubjectParams;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class SubjectDomain {

    private SubjectDomainRepository subjectDomainRepository;
    private SubjectParams subjectParams;
    public SubjectDomain(SubjectDomainRepository subjectDomainRepository, SubjectParams subjectParams) {
        this.subjectDomainRepository = subjectDomainRepository;
        this.subjectParams = subjectParams;
    }
    private List<SubjectModel> copyList(List<Subject> subjectList) {
        List<SubjectModel> subjectModels = new ArrayList<>();
        for (Subject subject : subjectList) {
            SubjectModel target = new SubjectModel();
            BeanUtils.copyProperties(subject, target);
            subjectModels.add(target);
        }

        return subjectModels;
    }

    public CallResult<Object> listSubjectNew() {
        /**
         * 1. 先查询所有的科目信息
         * 2. 将名字，年级，学期 组成TreeSet集合 用TreeSet去重
         * 3. 进行排序处理
         */
        List<Subject> subjectList = this.subjectDomainRepository.findSubjectList();
        //传回前端需要用前端需要的model实体类
        //List<SubjectModel> subjectModelList = copyList(subjectList);

        //将得到的subjectModelList赋值给Set集合
        Set<String> subjectNameList = new TreeSet<>();
        Set<String> subjectGradeList = new TreeSet<>();
        Set<String> subjectTermList = new TreeSet<>();
        for (Subject subjectModel : subjectList) {
            subjectGradeList.add(subjectModel.getSubjectGrade());
            subjectTermList.add(subjectModel.getSubjectTerm());
            subjectNameList.add(subjectModel.getSubjectName());
        }

        Set<String> sortSet = new TreeSet<String>((o1,o2) -> {
            /**
             * 1. 要将年级名称 取第一个字 七 八 九
             * 2. 转换为 7,8,9
             * 3. 进行数字排序,借助于CommonUtils工具类
             */
            String numberStr1 = CommonUtils.getNumberStr(o1);
            String numberStr2 = CommonUtils.getNumberStr(o2);
            long number2Int1 = CommonUtils.chineseNumber2Int(numberStr1);
            long number2Int2 = CommonUtils.chineseNumber2Int(numberStr2);
            return (int)(number2Int1 - number2Int2);
        });
        sortSet.addAll(subjectGradeList);//将年级进行排序并得到排序后的结果
        Map<String,Set<String>> map = new HashMap<>();
        map.put("subjectNameList",subjectNameList);
        map.put("subjectGradeList",sortSet);
        map.put("subjectTermList", subjectTermList);
        return CallResult.success(map);
    }

    public List<SubjectModel> findSubjectListByCourseId(Long courseId) {
        List<Subject> subjectList = this.subjectDomainRepository.findSubjectListByCourseId(courseId);
        return copyList(subjectList);
    }

    public List<Integer> findSubjectUnit(Long subjectId) {
        List<SubjectUnit> subjectUnitList = this.subjectDomainRepository.findSubjectUnitBySubjectId(subjectId);
        return subjectUnitList.stream().map(SubjectUnit::getSubjectUnit).collect(Collectors.toList());
    }

    public SubjectModel findSubject(Long subjectId) {
        Subject subject = this.subjectDomainRepository.findSubjectBySubjectId(subjectId);
        SubjectModel subjectModel = new SubjectModel();
        BeanUtils.copyProperties(subject,subjectModel);
        return subjectModel;
    }
}
