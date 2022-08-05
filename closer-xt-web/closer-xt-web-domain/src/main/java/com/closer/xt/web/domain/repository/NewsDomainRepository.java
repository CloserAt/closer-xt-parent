package com.closer.xt.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.pojo.News;
import com.closer.xt.web.dao.NewsMapper;
import com.closer.xt.web.domain.NewsDomain;
import com.closer.xt.web.domain.qiniuConfig.QiniuConfig;
import com.closer.xt.web.model.enums.Status;
import com.closer.xt.web.model.params.NewsParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewsDomainRepository {

    @Autowired
    public QiniuConfig qiniuConfig;

    @Autowired
    private NewsMapper newsMapper;

    public NewsDomain createDomain(NewsParams newsParams) {
        return new NewsDomain(this,newsParams);
    }

    //TODO bug:没查出来数据
    public Page<News> findNewsListByTab(int currentPage, int pageSize, Integer tab) {
        //实现分页查询
        Page<News> page = new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<News> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(News::getTab, tab);
        queryWrapper.eq(News::getStatus, Status.NORMAL.getCode());
        queryWrapper.select(News::getId);
        queryWrapper.select(News::getTitle);
//        queryWrapper.select(News::getImageUrl);
        return newsMapper.selectPage(page, queryWrapper);
    }

    public News newsDetailById(Long id) {
        return newsMapper.selectById(id);
    }
}
