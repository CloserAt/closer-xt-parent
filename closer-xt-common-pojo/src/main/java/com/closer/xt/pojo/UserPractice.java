package com.closer.xt.pojo;

import lombok.Data;

@Data
public class UserPractice {
    private Long id;
    private Long historyId;
    private Long topicId;
    private Long userId;
    private Integer pStatus;
    private String userAnswer;
}
