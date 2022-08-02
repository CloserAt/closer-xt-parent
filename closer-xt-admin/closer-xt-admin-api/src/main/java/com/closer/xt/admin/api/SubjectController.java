package com.closer.xt.admin.api;

import com.closer.xt.admin.params.SubjectParams;
import com.closer.xt.admin.service.SubjectService;
import com.closer.xt.common.model.CallResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("subject")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @PostMapping("findPage")
    public CallResult findPage(@RequestBody SubjectParams subjectParams) {
        return subjectService.findPage(subjectParams);
    }

    @PostMapping("saveSubject")
    public CallResult saveSubject(@RequestBody SubjectParams subjectParams) {
        return subjectService.saveSubject(subjectParams);
    }

    @PostMapping("findSubjectById")//编辑前需要找到数据回显到页面
    public CallResult findSubjectById(@RequestBody SubjectParams subjectParams) {
        return subjectService.findSubjectById(subjectParams);
    }

    @PostMapping("updateSubject")
    public CallResult updateSubject(@RequestBody SubjectParams subjectParams) {
        return subjectService.updateSubject(subjectParams);
    }

    @PostMapping("allSubjectList")
    public CallResult allSubjectList(@RequestBody SubjectParams subjectParams) {
        return subjectService.allSubjectList(subjectParams);
    }
}
