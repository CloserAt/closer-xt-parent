package com.closer.xt.web.service;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.web.model.params.NewsParams;

public interface NewsService {
    /**
     * 分页查询 新闻列表接口
     * @param newsParams
     * @return
     */
    CallResult newsList(NewsParams newsParams);

    /**
     * 新闻详情接口
     * @param newsParams
     * @return
     */
    CallResult newsById(NewsParams newsParams);

}
