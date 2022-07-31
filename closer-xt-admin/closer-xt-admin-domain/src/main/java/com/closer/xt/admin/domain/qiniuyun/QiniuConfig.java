package com.closer.xt.admin.domain.qiniuyun;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class QiniuConfig {
    @Value("${qiniu.file.server.url}")
    private String fileServerUrl;
}
