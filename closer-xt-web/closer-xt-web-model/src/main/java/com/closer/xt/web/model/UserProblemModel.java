package com.closer.xt.web.model;

import lombok.Data;

@Data
public class UserProblemModel {
    private Long problemId;
    private TopicModelView topic;
    private SubjectModel subject;
    private Integer errorCount;
    private String errorAnswer;
    private String errorTime;
}