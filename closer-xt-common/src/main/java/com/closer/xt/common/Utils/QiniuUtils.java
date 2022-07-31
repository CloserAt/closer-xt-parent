package com.closer.xt.common.Utils;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QiniuUtils {

    public static final String url = "http://rfw6zripd.bkt.clouddn.com/";
    @Value("bBAcnR3f6gwI9X4_I_gPbyLBF3N42nIE4OV6udzP")
    private String accessKey;
    @Value("UgQQuCapA5dWCa-e3o2aJbt6MrSiwp7bhMu34LED")
    private String SecretKey;

    public static boolean upload(String accessKey,String secretKey,String bucket,byte[] uploadBytes,String key){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huabei());
//...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
//默认不指定key的情况下，以文件内容的hash值作为文件名
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(uploadBytes, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            return true;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
            return false;
        }
    }
}
