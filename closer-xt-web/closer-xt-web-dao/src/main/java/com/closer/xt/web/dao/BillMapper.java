package com.closer.xt.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.closer.xt.pojo.Bill;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.backoff.BackOff;

@Mapper
public interface BillMapper extends BaseMapper<Bill> {
}
