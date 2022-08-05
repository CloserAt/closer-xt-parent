package com.closer.xt.web.api;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.web.service.SubjectService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("subject")
public class SubjectAPI {
    @Autowired
    private SubjectService subjectService;

    /**
     * 页面上，点击进入学习，会调用一个接口，请求所有的科目信息
     * @return
     */
    @PostMapping("listSubjectNew")
    public CallResult listSubjectNew() {
        return subjectService.listSubjectNew();
    }
}
