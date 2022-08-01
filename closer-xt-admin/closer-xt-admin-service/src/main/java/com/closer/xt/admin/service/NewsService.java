package com.closer.xt.admin.service;

import com.closer.xt.admin.params.NewsParams;
import com.closer.xt.common.model.CallResult;
import org.springframework.web.multipart.MultipartFile;

public interface NewsService {
    CallResult findPage(NewsParams newsParams);

    /**
     * 新建新闻信息
     * @param newsParams
     * @return
     */
    CallResult saveNews(NewsParams newsParams);

    /**
     * 编辑新闻
     * @param id
     * @return
     */
    CallResult findNewsById(NewsParams newsParams);

    /**
     * 编辑完成后的点击确定，调用update方法
     * @param newsParams
     * @return
     */
    CallResult update(NewsParams newsParams);

    /**
     * 新闻删除
     * @param newsParams
     * @return
     */
    CallResult delete(NewsParams newsParams);

    /**
     * 七牛云图片上传
     * @param file
     * @return
     */
    CallResult upload(MultipartFile file);
}
