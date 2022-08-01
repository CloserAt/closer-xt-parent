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
}
