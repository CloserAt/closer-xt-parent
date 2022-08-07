package com.closer.xt.web.api;

import com.closer.xt.common.model.CallResult;
import com.closer.xt.web.model.params.TopicParams;
import com.closer.xt.web.service.TopicService;
import net.sf.jsqlparser.statement.select.Top;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("topic")
public class TopicAPI {
    @Autowired
    private TopicService topicService;

    @RequestMapping(value = "practice",method = RequestMethod.POST)
    public CallResult practice(@RequestBody TopicParams topicParams) {
        return topicService.practice(topicParams);
    }

    @PostMapping("submit")
    public CallResult submit(@RequestBody TopicParams topicParams) {
        return topicService.submit(topicParams);
    }

    @PostMapping("jump")
    public CallResult jump(@RequestBody TopicParams topicParams) {
        return topicService.jump(topicParams);
    }
}
