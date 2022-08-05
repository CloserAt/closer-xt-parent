package com.closer.xt.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Course {
    private Long id;
    private String courseName;
    private String courseDesc;
    private String subjects;
    private BigDecimal coursePrice;
    private BigDecimal courseZhePrice;
    private Integer courseStatus;//课程状态 0正常 1下架
    private Integer orderTime;//天数
    private String imageUrl;
}
