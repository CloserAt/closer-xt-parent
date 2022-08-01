package com.closer.xt.admin.params;

import lombok.Data;

import java.util.List;

@Data
public class SubjectParams {
    private Long id;
    private String subjectName;
    private String subjectGrade;
    private String subjectTerm;
    private List<Integer> subjectUnits;

    private Integer status;

    private int CurrentPage = 1;
    private int pageSize = 20;

    private String queryString;
}
