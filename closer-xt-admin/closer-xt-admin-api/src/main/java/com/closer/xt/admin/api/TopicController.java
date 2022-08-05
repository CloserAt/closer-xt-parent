package com.closer.xt.admin.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONAware;
import com.closer.xt.admin.model.TopicModel;
import com.closer.xt.admin.params.TopicParams;
import com.closer.xt.admin.service.TopicService;
import com.closer.xt.common.enums.TopicType;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.model.topic.ContentAndImage;
import com.closer.xt.common.utils.POIUtils;
import com.closer.xt.pojo.Topic;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("topic")
public class TopicController {
    @Autowired
    private TopicService topicService;

    @RequestMapping("findPage")
    public CallResult findTopicList(@RequestBody TopicParams topicParams) {
        return topicService.findTopicList(topicParams);
    }

    //@RequestParam("excelFile")：前端设定的括号中的值为excelFile
    @RequestMapping("uploadExcel/{subjectId}")
    public CallResult upload(@RequestParam("excelFile") MultipartFile multipartFile, @PathVariable("subjectId") Long subjectId){
        int updateNum = 0;
        if (subjectId == -1){
            return CallResult.fail(-999,"subjectId 不能为空");
        }
        try {
            //调用POI工具类来读取excel
            List<String[]> list = POIUtils.readExcel(multipartFile);
            for (String[] strs : list){
                //strs[0]代表学科单元
                if (StringUtils.isEmpty(strs[0])){
                    continue;
                }
                Integer subjectUnit = Integer.parseInt(strs[0]);

                //str[1]代表题目类型
                if (StringUtils.isEmpty(strs[1])){
                    continue;
                }
                Integer topicType = Integer.parseInt(strs[1]);

                //str[2]代表具体题目
                String topicTitle = strs[2];
                List<Map<String, ContentAndImage>> choiceList = new ArrayList<>();

                //str[3]代表选项A
                String choiceA = strs[3];
                ContentAndImage a = ContentAndImage.deal(choiceA);
                if (a != null){
                    Map<String,ContentAndImage> aMap = new HashMap<>();
                    aMap.put("A",a);
                    choiceList.add(aMap);
                }

                //str[4]代表选项B
                String choiceB = strs[4];
                ContentAndImage b = ContentAndImage.deal(choiceB);
                if (b != null){
                    Map<String,ContentAndImage> bMap = new HashMap<>();
                    bMap.put("B",b);
                    choiceList.add(bMap);
                }

                //str[5]代表选项C
                String choiceC = strs[5];
                ContentAndImage c = ContentAndImage.deal(choiceC);
                if (c != null){
                    Map<String,ContentAndImage> cMap = new HashMap<>();
                    cMap.put("C",c);
                    choiceList.add(cMap);
                }

                //str[6]代表选项D
                String choiceD = strs[6];
                ContentAndImage d = ContentAndImage.deal(choiceD);
                if (d != null){
                    Map<String,ContentAndImage> dMap = new HashMap<>();
                    dMap.put("D",d);
                    choiceList.add(dMap);
                }

                //str[7]代表选项E
                String choiceE = strs[7];
                ContentAndImage e = ContentAndImage.deal(choiceE);
                if (e != null){
                    Map<String,ContentAndImage> eMap = new HashMap<>();
                    eMap.put("E",e);
                    choiceList.add(eMap);
                }

                //str[8]代表选项F
                String choiceF = "";
                if (strs.length >= 9) {
                    choiceF = strs[8];
                }
                ContentAndImage f = ContentAndImage.deal(choiceF);
                if (f != null){
                    Map<String,ContentAndImage> fMap = new HashMap<>();
                    fMap.put("F",f);
                    choiceList.add(fMap);
                }

                //str[9]代表选项G
                String choiceG = "";
                if (strs.length >= 10) {
                    choiceG = strs[9];
                }
                ContentAndImage g = ContentAndImage.deal(choiceG);
                if (g != null){
                    Map<String,ContentAndImage> gMap = new HashMap<>();
                    gMap.put("G",g);
                    choiceList.add(gMap);
                }

                //str[10]代表选项H
                String choiceH = "";
                if (strs.length >= 11) {
                    choiceH = strs[10];
                }
                ContentAndImage h = ContentAndImage.deal(choiceH);
                if (h != null){
                    Map<String,ContentAndImage> hMap = new HashMap<>();
                    hMap.put("H",h);
                    choiceList.add(hMap);
                }

                //str[11]代表答案
                String topicAnswer = "";
                if (strs.length >= 12) {
                    topicAnswer = strs[11];
                }

                //str[12]代表解析
                String topicAnalyze = "";
                if (strs.length >= 13) {
                    topicAnalyze = strs[12];
                }

                //str[13]代表难度
                Integer topicStar = 3;
                if (strs.length >= 14) {
                    if (!StringUtils.isEmpty(strs[13])) {
                        topicStar = Integer.parseInt(strs[13]);
                    }
                }

                //str[14]代表区域
                String topicAreaCity = "";
                if (strs.length >= 15) {
                    topicAreaCity = strs[14];
                }

                //str[15]代表图片
                String topicImage = null;
                if (strs.length >= 16) {
                    topicImage = strs[15];
                }
                List<String> topicImageList = new ArrayList<>();
                if (!StringUtils.isEmpty(topicImage)) {
                    Pattern pt = Pattern.compile("http?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
                    Matcher mt = pt.matcher(topicImage);
                    if (mt.find()) {
                        String imagesStr = mt.group();
                        String[] images = imagesStr.split(",");
                        topicImageList.addAll(Arrays.asList(images));
                    }
                }

                //传入topic对象
                Topic topic = new Topic();
                topic.setTopicTitle(topicTitle);
                topic.setTopicType(topicType);
                topic.setSubjectUnit(subjectUnit);
                topic.setTopicAnalyze(topicAnalyze);
                if (TopicType.FILL_BLANK.getCode() == topicType){
                    String[] strAnswer = topicAnswer.split("\\$;\\$");
                    List<Map<String,Object>> map = new ArrayList<>();
                    for (int i = 1;i<=strAnswer.length;i++){
                        Map<String,Object> m = new HashMap<>();
                        m.put("id",i);
                        m.put("content",strAnswer[i-1]);
                        map.add(m);
                    }
                    topic.setTopicChoice(JSON.toJSONString(map));
                }else{
                    topic.setTopicChoice(JSON.toJSONString(choiceList));
                }
                topic.setTopicAnswer(topicAnswer);

                topic.setCreateTime(System.currentTimeMillis());
                topic.setLastUpdateTime(System.currentTimeMillis());
                topic.setTopicStar(topicStar);
                topic.setTopicAreaCity(topicAreaCity);
                topic.setTopicImg(JSON.toJSONString(topicImageList));
                topic.setAddAdmin("admin");
                topic.setTopicAreaPro("");
                topic.setTopicSubject(subjectId);
                Topic t = this.topicService.findTopicByTitle(topic.getTopicTitle());
                if (t != null){
                    log.info("update topic:{}",topic.getTopicTitle());
                    topic.setId(t.getId());
                    updateNum++;
                    this.topicService.updateTopic(topic);
                }else{
                    this.topicService.saveTopic(topic);
                }
//
            }
//            System.out.println(JSON.toJSONString(list));
        } catch (IOException e) {
            e.printStackTrace();
            return CallResult.fail();
        }
        log.info("update num:{}",updateNum);
        return CallResult.success();
    }
}
