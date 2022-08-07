package com.closer.xt.log.consumer;

import com.closer.xt.log.mongo.data.UserLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@RocketMQMessageListener(topic = "xt_log_sso_login",consumerGroup = "sso_consumer_group")
public class SSOLogConsumer implements RocketMQListener<UserLog> {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void onMessage(UserLog userLog) {
        log.info("消息消费{}",userLog);
        mongoTemplate.save(userLog);
    }
}
