package com.closer.xt.web.model.params;

import lombok.Data;

@Data
public class NewsParams {
    private Integer page = 1;
    private Integer pageSize = 20;
    private Integer tab;

    private Long id;
    private String title;
    private String summary;
    private String imageUrl;
    private String content;
    private Long createTime;
    private String author;
}
