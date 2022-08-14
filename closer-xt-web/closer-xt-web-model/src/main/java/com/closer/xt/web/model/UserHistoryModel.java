package com.closer.xt.web.model;

import lombok.Data;

import java.util.List;

@Data
public class UserHistoryModel {
    private Long id;
    private String createTime;
    private List<Integer> subjectUnitList;
    private String subjectName;
    private Long subjectId;
    private String finishTime;
    private String useTime;
    private Integer historyStatus;//1代表未完成 2代表完成 3代表取消
    private Integer status;//0代表正常 1代表过期
}
