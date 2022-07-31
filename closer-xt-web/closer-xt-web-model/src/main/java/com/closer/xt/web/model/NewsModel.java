package com.closer.xt.web.model;

import lombok.Data;

@Data
public class NewsModel {
    private Long id;
    private String title;
    private String summary;
    private String imageUrl;
    private String content;
    private Integer tab;
    private String createTime;
    private String author;
}
