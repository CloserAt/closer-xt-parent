package com.closer.xt.admin.service;

import com.closer.xt.admin.model.TopicModel;
import com.closer.xt.admin.params.TopicParams;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.pojo.Topic;

public interface TopicService {
    /**
     * 根据条件进行对应的题目分页查询
     * @param topicParams
     * @return
     */
    CallResult findTopicList(TopicParams topicParams);


    /**
     * 根据标题找题目
     * @param topicTitle
     * @return
     */
    Topic findTopicByTitle(String topicTitle);

    /**
     * 保存题目
     * @param topic
     */
    void updateTopic(Topic topic);

    /**
     * 保存topic
     * @param topic
     */
    void saveTopic(Topic topic);
}
