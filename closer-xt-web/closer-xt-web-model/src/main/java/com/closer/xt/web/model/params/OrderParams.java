package com.closer.xt.web.model.params;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

@Data
public class OrderParams {
    private int page = 1;
    private int pageSize = 20;
    private Long userId;
    private Long courseId;
    private Long couponId;
    private Integer payType;
    private String orderId;

    private Integer orderStatus;

    private HttpServletRequest request;

    private String nickname;
}
