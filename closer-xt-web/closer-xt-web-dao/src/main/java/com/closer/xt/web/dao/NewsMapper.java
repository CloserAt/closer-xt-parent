package com.closer.xt.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.closer.xt.pojo.News;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsMapper extends BaseMapper<News> {
}
