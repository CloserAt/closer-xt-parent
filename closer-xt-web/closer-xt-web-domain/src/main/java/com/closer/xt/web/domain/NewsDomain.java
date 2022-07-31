package com.closer.xt.web.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.common.model.BusinessCodeEnum;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.pojo.News;
import com.closer.xt.web.domain.repository.NewsDomainRepository;
import com.closer.xt.web.model.ListPageModel;
import com.closer.xt.web.model.NewsModel;
import com.closer.xt.web.model.enums.TabEnum;
import com.closer.xt.web.model.params.NewsParams;

import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class NewsDomain {
    private NewsDomainRepository newsDomainRepository;
    private NewsParams newsParams;
    public NewsDomain(NewsDomainRepository newsDomainRepository, NewsParams newsParams) {
        this.newsDomainRepository = newsDomainRepository;
        this.newsParams = newsParams;
    }

    public CallResult<Object> checkNewsListParams() {
        //检查参数：1.分页参数，让pageSize=5 2.tab是否合法
        Integer pageSize = newsParams.getPageSize();
        if (pageSize > 5) {
            pageSize = 5;
            newsParams.setPageSize(pageSize);
        }
        Integer page = newsParams.getPage();
        if (page <= 0) return CallResult.fail(BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getCode(), BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getMsg());
        Integer tab = newsParams.getTab();
        if (TabEnum.valueOfCode(tab) == null) {
            return CallResult.fail(BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getCode(), BusinessCodeEnum.CHECK_PARAM_NO_RESULT.getMsg());
        }
        return CallResult.success();
    }

    public CallResult<Object> newsList() {
        int page = newsParams.getPage();
        int pageSize = newsParams.getPageSize();
        Integer tab = newsParams.getTab();
        Page<News> newsPage = this.newsDomainRepository.findNewsListByTab(page, pageSize, tab);
        List<News> records = newsPage.getRecords();
        List<NewsModel> newsModelsList = copyList(records);

        //分页模型 所有的分页都需要返回此模型--固定写法模式
        ListPageModel<NewsModel> listPageModel = new ListPageModel();
        listPageModel.setList(newsModelsList);
        listPageModel.setPage(page);
        listPageModel.setPageSize(pageSize);
        listPageModel.setPageCount(newsPage.getPages());
        listPageModel.setSize(newsPage.getTotal());


        return CallResult.success(listPageModel);
    }

    public NewsModel copy(News news){
        if (news == null){
            return null;
        }
        NewsModel newsModel = new NewsModel();
        //属性copy
        BeanUtils.copyProperties(news,newsModel);
        if (news.getCreateTime() != null) {
            newsModel.setCreateTime(new DateTime(news.getCreateTime()).toString("yyyy年MM月dd日 HH:mm:ss"));
        }
        if (news.getImageUrl() != null) {
            if (!news.getImageUrl().startsWith("http")) {
                newsModel.setImageUrl(newsDomainRepository.qiniuConfig.getFileServerUrl() + news.getImageUrl());
            }
        }
        return newsModel;
    }

    public List<NewsModel> copyList(List<News> newsList){
        List<NewsModel> newsModelList = new ArrayList<>();
        for (News news : newsList){
            newsModelList.add(copy(news));
        }
        return newsModelList;
    }


    public CallResult<Object> newsDetailById() {
        News news = this.newsDomainRepository.newsDetailById(newsParams.getId());
        NewsModel newsModel = copy(news);
        return CallResult.success(newsModel);
    }
}
