package com.closer.xt.admin.api;

import com.closer.xt.admin.params.TopicParams;
import com.closer.xt.admin.service.TopicService;
import com.closer.xt.common.model.CallResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("topic")
public class TopicController {
    @Autowired
    private TopicService topicService;

    @RequestMapping("findPage")
    public CallResult findTopicList(@RequestBody TopicParams topicParams) {
        return topicService.findTopicList(topicParams);
    }

}
