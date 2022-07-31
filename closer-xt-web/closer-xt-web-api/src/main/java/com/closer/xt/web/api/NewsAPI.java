package com.closer.xt.web.api;

import com.closer.xt.common.cache.Cache;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.web.model.params.NewsParams;
import com.closer.xt.web.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("news")
public class NewsAPI {

    @Autowired
    private NewsService newsService;

    @PostMapping("newsList")
    @Cache(name = "newsList", time = 2*60)
    public CallResult newsList(@RequestBody NewsParams newsParams) {
        return newsService.newsList(newsParams);
    }

    @PostMapping("detail")
    @Cache(name = "newsDetail", time = 30)
    public CallResult news(@RequestBody NewsParams newsParams) {
        return newsService.newsById(newsParams);
    }

}
