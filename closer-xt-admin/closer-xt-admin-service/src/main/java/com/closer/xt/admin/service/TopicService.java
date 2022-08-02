package com.closer.xt.admin.service;

import com.closer.xt.admin.params.TopicParams;
import com.closer.xt.common.model.CallResult;

public interface TopicService {
    /**
     * 根据条件进行对应的题目分页查询
     * @param topicParams
     * @return
     */
    CallResult findTopicList(TopicParams topicParams);
}
