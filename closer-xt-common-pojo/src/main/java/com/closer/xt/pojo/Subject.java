package com.closer.xt.pojo;

import lombok.Data;

@Data
public class Subject {
    private Long id;
    private String subjectName;
    private String subjectGrade;
    private String subjectTerm;
    private Integer status;
}
