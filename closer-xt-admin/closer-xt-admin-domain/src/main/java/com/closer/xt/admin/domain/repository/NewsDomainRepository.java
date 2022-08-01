package com.closer.xt.admin.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.admin.dao.NewsMapper;
import com.closer.xt.admin.domain.NewsDomain;
import com.closer.xt.admin.domain.qiniuyun.QiniuConfig;
import com.closer.xt.admin.domain.qiniuyun.QiniuUtils;
import com.closer.xt.admin.params.NewsParams;
import com.closer.xt.pojo.News;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewsDomainRepository {
    @Autowired
    public QiniuUtils qiniuUtils;

    @Autowired
    public QiniuConfig qiniuConfig;

    @Autowired
    private NewsMapper newsMapper;

    public NewsDomain createDomain(NewsParams newsParams) {
        return new NewsDomain(this, newsParams);
    }

    public Page<News> findNewsPageByCondition(Integer currentPage, Integer pageSize, String queryString) {
        Page<News> page = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<News> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(queryString)) {
            queryWrapper.like(News::getTitle, queryString);
        }
        return newsMapper.selectPage(page, queryWrapper);
    }

    public void save(News news) {
        this.newsMapper.insert(news);
    }

    public News findNewsById(Long id) {
        return this.newsMapper.selectById(id);
    }

    public void update(News news) {
        this.newsMapper.updateById(news);
    }

    public void delete(Long id) {
        this.newsMapper.deleteById(id);
    }
}
