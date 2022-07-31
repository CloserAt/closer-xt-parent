package com.closer.xt.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class News {
    private Long id;
    private String title;
    private String summary;
    private String imageUrl;
    private String content;
    private Integer tab;
    private Long createTime;
    private String author;

    @TableField("n_status")
    private Integer status;
}
