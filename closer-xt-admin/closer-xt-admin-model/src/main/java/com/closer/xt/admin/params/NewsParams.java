package com.closer.xt.admin.params;

import lombok.Data;

@Data
public class NewsParams {
    private Integer currentPage = 1;
    private Integer pageSize = 20;
    private Long id;
    private Integer tab;
    private String title;
    private String summary;
    private String imageUrl;
    private String content;
    private Long createTime;
    private String author;
    private Integer status;

    /*
    查询条件
     */
    private String queryString;
}
