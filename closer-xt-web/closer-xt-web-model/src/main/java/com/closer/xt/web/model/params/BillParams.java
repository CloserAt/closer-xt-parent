package com.closer.xt.web.model.params;

import lombok.Data;

@Data
public class BillParams {
    private Integer page = 1;
    private Integer pageSize = 10;

    private Long id;
    private String name;
    private String billDesc;
    private String billType;
    /**
     * 0 正常 1 删除
     */
    private Integer status;

    private Long userId;
}
