package com.closer.xt.admin.model;

import lombok.Data;

@Data
public class SubjectModel {
    private Long id;
    private String subjectName;
    private String subjectGrade;
    private String subjectTerm;
    private Integer status;

    public  void fillSubjectName() {
        this.subjectName = this.subjectName + "-" + this.subjectGrade + "-" + this.subjectTerm;
    }
}
