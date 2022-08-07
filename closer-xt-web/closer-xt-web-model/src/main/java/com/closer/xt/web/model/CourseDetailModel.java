package com.closer.xt.web.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseDetailModel {
    private Long courseId;
    private String courseName;
    private String subjectInfo;
    private Integer courseTime;
    private BigDecimal price;
}
