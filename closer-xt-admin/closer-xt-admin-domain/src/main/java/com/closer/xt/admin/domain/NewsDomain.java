package com.closer.xt.admin.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.admin.domain.repository.NewsDomainRepository;
import com.closer.xt.admin.domain.repository.TopicDomainRepository;
import com.closer.xt.admin.model.ListPageModel;
import com.closer.xt.admin.model.NewsModel;
import com.closer.xt.admin.params.NewsParams;
import com.closer.xt.admin.params.TopicParams;
import com.closer.xt.common.utils.QiniuUtils;
import com.closer.xt.common.model.BusinessCodeEnum;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.pojo.News;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NewsDomain {
    private NewsDomainRepository newsDomainRepository;
    private NewsParams newsParams;
    public NewsDomain(NewsDomainRepository newsDomainRepository, NewsParams newsParams) {
        this.newsDomainRepository = newsDomainRepository;
        this.newsParams = newsParams;
    }

    public NewsDomain(TopicDomainRepository topicDomainRepository, TopicParams topicParams) {

    }

    public CallResult<Object> findNewsPage() {
        Integer currentPage = this.newsParams.getCurrentPage();
        Integer pageSize = this.newsParams.getPageSize();
        String queryString = this.newsParams.getQueryString();
        Page<News> page = newsDomainRepository.findNewsPageByCondition(currentPage, pageSize, queryString);
        ListPageModel<NewsModel> listPageModel = new ListPageModel<>();
        List<News> records = page.getRecords();
        //此处的newsModel虽然和web模块里的一样，但是代表含义不同
        List<NewsModel> newsModelList = copyList(records);
        listPageModel.setList(newsModelList);
        listPageModel.setSize(page.getTotal());
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

    public CallResult<Object> saveNewNews() {
        News news = new News();
        BeanUtils.copyProperties(this.newsParams, news);
        news.setCreateTime(System.currentTimeMillis());
        news.setStatus(0);
        this.newsDomainRepository.save(news);
        return CallResult.success();
    }

    public CallResult<Object> findNewsById() {
        Long id = newsParams.getId();
        News news = this.newsDomainRepository.findNewsById(id);
        return CallResult.success(news);
    }

    public CallResult<Object> update() {
        News news = new News();
        BeanUtils.copyProperties(this.newsParams, news);
        this.newsDomainRepository.update(news);
        return CallResult.success(news);
    }

    public CallResult<Object> delete(NewsParams newsParams) {
        Long id = newsParams.getId();
        this.newsDomainRepository.delete(id);
        return CallResult.success();
    }

    public CallResult upload(MultipartFile file) {
        //原始文件名称
        String originalFilename = file.getOriginalFilename();
        //切割得到后缀后，再加上随机字符串得到唯一文件名称
        String fileName = UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(originalFilename, ".");
        //上传文件上传至七牛云服务器
        boolean upload = this.newsDomainRepository.qiniuUtils.upload(file, fileName);
        if (upload) {
            return CallResult.success(QiniuUtils.url + fileName);
        }
        return CallResult.fail(BusinessCodeEnum.UPLOAD_FAIL.getCode(),BusinessCodeEnum.UPLOAD_FAIL.getMsg());
    }
}
