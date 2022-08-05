package com.closer.xt.web.model;

import lombok.Data;
import java.util.List;

@Data
public class SubjectModel {
    private Long code;
    private String subjectGrade;
    private String subjectName;
    private String subjectTerm;
    private Integer status;

    private List<Integer> subjectUnits;

    public void fillSubjectName() {
        this.subjectName = this.subjectName + "-" +subjectGrade + "-" + subjectTerm;
    }
}
