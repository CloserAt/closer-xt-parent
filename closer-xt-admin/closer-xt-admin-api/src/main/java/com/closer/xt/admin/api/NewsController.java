package com.closer.xt.admin.api;

import com.closer.xt.admin.params.NewsParams;
import com.closer.xt.admin.service.NewsService;
import com.closer.xt.common.model.CallResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("news")
public class NewsController {
    @Autowired
    private NewsService newsService;
    /*
    //分页查询
                findPage() {
                    //post @RequestBody
                    axios.post("/lzadmin/news/findPage",this.pagination).then((res)=>{
                        //res.data = CallResult
                        if (res.data.success){
                            this.dataList = res.data.result.list;
                            this.pagination.total = res.data.result.size;
                        }
                    });
                },
     */
    @PostMapping("findPage")
    public CallResult findPage(@RequestBody NewsParams newsParams) {
        return newsService.findPage(newsParams);
    }

    @PostMapping("save")
    public CallResult save(@RequestBody NewsParams newsParams) {
        return newsService.saveNews(newsParams);
    }

    @RequestMapping("findNewsById")
    public CallResult findNewsById(@RequestBody NewsParams newsParams) {
        return newsService.findNewsById(newsParams);
    }

    @PostMapping("update")
    public CallResult update(@RequestBody NewsParams newsParams) {
        return newsService.update(newsParams);
    }

    @PostMapping("delete")
    public CallResult delete(@RequestBody NewsParams newsParams) {
        return newsService.delete(newsParams);
    }
}
