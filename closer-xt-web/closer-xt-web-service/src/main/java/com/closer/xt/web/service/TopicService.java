package com.closer.xt.web.service;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.web.model.params.TopicParams;

public interface TopicService {
    /**
     * 练习接口
     * @param topicParams
     * @return
     */
    CallResult practice(TopicParams topicParams);

    /**
     * 提交答案
     * @param topicParams
     * @return
     */
    CallResult submit(TopicParams topicParams);

    /**
     * 跳转下一题
     * @param topicParams
     * @return
     */
    CallResult jump(TopicParams topicParams);
}
