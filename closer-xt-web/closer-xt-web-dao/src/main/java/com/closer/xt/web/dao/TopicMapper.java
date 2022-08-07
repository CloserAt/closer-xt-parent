package com.closer.xt.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.closer.xt.pojo.Topic;
import com.closer.xt.web.dao.data.TopicDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface TopicMapper extends BaseMapper<Topic> {
//    TopicDTO findTopicAnswer(@Param("topicId") Long topicId,
//                             @Param("userId") Long userId,
//                             @Param("userHistoryId")Long userHistoryId);

}
