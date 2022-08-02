package com.closer.xt.admin.params;

import lombok.Data;

import java.util.List;

@Data
public class TopicParams {
    private Long id;
    private Integer topicType;
    private String topicTitle;
    private String topicImg;
    private String answer;
    private String topicAnalyze;
    private Long topicSubject;
    private Integer topicStar;
    private String topicAreaPro;
    private String topicAreaCity;
    private Integer page = 1;
    private Integer pageSize = 20;

    //subject
    private Long subjectId;

    private Long userId;
    //题目ID
    private Long topicId;
    //单元
    private Integer subjectUnit;
    //单元
    private List<Integer> subjectUnitList;

    private String subjectName;
    private String subjectGrade;
    private String subjectTerm;
}
