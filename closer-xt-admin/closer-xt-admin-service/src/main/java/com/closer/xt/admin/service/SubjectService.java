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

}
