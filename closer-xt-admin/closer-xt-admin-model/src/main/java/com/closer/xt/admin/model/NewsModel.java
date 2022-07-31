package com.closer.xt.admin.model;

import lombok.Data;

@Data
public class NewsModel {
    private Long id;
    private String title;
    private String summary;
    private String imageUrl;
    private String content;
    /**
     * 1 题库 2 升学 3 其他
     */
    private Integer tab;
    private String createTime;
    private String author;
    /**
     * 0 正常 1 删除
     */
    private Integer status;

    private String createTimeStr;
}