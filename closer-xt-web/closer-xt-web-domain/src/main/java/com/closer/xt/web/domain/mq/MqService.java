package com.closer.xt.web.domain.mq;

import org.joda.time.DateTime;
import org.springframework.messaging.support.MessageBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

@Component
@Slf4j
public class MqService {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    //延迟等级 RocketMQ不支持任意时间的延时，只支持以下几个固定的延时等级  "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h"
    public void sendDelayedMessage(String topic,Object data,int delay){
        MessageBuilder<Object> messageBuilder = MessageBuilder.withPayload(data);
        //timeout 发送的超时时间
        rocketMQTemplate.syncSend(topic, messageBuilder.build(), 3000, delay);
        //采用同步发送，确保消息一定发送成功
        log.info("MqService 发延迟消息的时间:{},消息内容:{},topic:{},delay:{}",new DateTime(),data,topic,delay);
    }
}