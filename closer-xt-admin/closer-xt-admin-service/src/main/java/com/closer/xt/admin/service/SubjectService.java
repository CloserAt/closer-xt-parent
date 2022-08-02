package com.closer.xt.admin.service;

import com.closer.xt.admin.params.SubjectParams;
import com.closer.xt.common.model.CallResult;

public interface SubjectService {
    /**
     * 学科管理页面-分页查询
     * @param subjectParams
     * @return
     */
    CallResult findPage(SubjectParams subjectParams);

    /**
     * 新增学科
     * @param subjectParams
     * @return
     */
    CallResult saveSubject(SubjectParams subjectParams);

    /**
     * 编辑学科信息
     * @param subjectParams
     * @return
     */
    CallResult updateSubject(SubjectParams subjectParams);

    /**
     * 根据id找到学科，然后才能进行编辑
     * @param subjectParams
     * @return
     */
    CallResult findSubjectById(SubjectParams subjectParams);

    /**
     * 查询所有学科信息
     * @param subjectParams
     * @return
     */
    CallResult allSubjectList(SubjectParams subjectParams);
}
