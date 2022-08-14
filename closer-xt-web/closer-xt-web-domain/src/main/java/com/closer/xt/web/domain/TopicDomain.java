package com.closer.xt.web.domain;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.common.Login.UserThreadLocal;
import com.closer.xt.common.enums.TopicType;
import com.closer.xt.common.model.BusinessCodeEnum;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.common.model.ListPageModel;
import com.closer.xt.common.model.topic.ContentAndImage;
import com.closer.xt.common.model.topic.FillBlankChoice;
import com.closer.xt.pojo.Topic;
import com.closer.xt.pojo.UserHistory;
import com.closer.xt.pojo.UserPractice;
import com.closer.xt.pojo.UserProblem;
import com.closer.xt.web.dao.data.TopicDTO;
import com.closer.xt.web.domain.repository.TopicDomainRepository;
import com.closer.xt.web.model.*;
import com.closer.xt.web.model.enums.ErrorStatus;
import com.closer.xt.web.model.enums.HistoryStatus;
import com.closer.xt.web.model.params.TopicParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Slf4j
public class TopicDomain {
    private TopicDomainRepository topicDomainRepository;
    private TopicParams topicParams;
    public TopicDomain(TopicDomainRepository topicDomainRepository, TopicParams topicParams) {
        this.topicDomainRepository = topicDomainRepository;
        this.topicParams = topicParams;
    }

    public CallResult<Object> checkPracticeBiz() {

        /*
        传过来的subjectId,需要判断对应课程用户是否购买
         */
        //根据subjectId在course_subject表中找到对应的courseId，然后在user_course表中判断是否有这个id，即可以得知是否购买

        Long subjectId = this.topicParams.getSubjectId();
        //TODO 此处获取用户id为空
        Long userId = UserThreadLocal.get();
        List<Long> courseIdList = this.topicDomainRepository.createCourseDomain(null).findCourseIdBySubjectId(subjectId);
        //判断查出来的courseId是否为空
        if (CollectionUtils.isEmpty(courseIdList)) {
            return CallResult.fail(BusinessCodeEnum.COURSE_NO_BUY.getCode(),"此课程尚未购买");
        }

        Integer courseCount = this.topicDomainRepository.createUserCourseDomain(null).countUserCourseInCourseIdList(userId,courseIdList,System.currentTimeMillis());
        if (courseCount <= 0) {
            //说明没有购买课程或者过期
            return CallResult.fail(BusinessCodeEnum.COURSE_NO_BUY.getCode(),"此课程还没购买或者已过期");
        }

        return CallResult.success();
    }

    public CallResult<Object> practice(TopicParams topicParams) {
        /**
         * 1. 判断 此用户 此学科 之前是否已经练习过 UserHistory 有没有
         * 2. 如果没有 新的练习 ，开始一个新的练习
         * 3. 如果有，拿到之前的练习，返回对应的练习进度
         */
        Long userId = UserThreadLocal.get();
        Long subjectId = this.topicParams.getSubjectId();
        //从课程立即学习进入 practiceId不传，但是如果从我的学习模块进入，会传递当前的练习id（学习记录id）
        //如果是从我的学习中 直接进入，根据学习记录id 进行查询
        //如果不是，根据传递的学科id，进行查询，判断是否进行过学习，上一次学习未完成不能开启下一次练习
        Long practiceId = this.topicParams.getPracticeId();
        UserHistory userHistory = null;
        if (practiceId == null) {
            userHistory = this.topicDomainRepository.createUserHistoryDomain(null).findUserHistory(userId,subjectId,HistoryStatus.NO_FINISH.getCode());
        } else {
            userHistory = this.topicDomainRepository.createUserHistoryDomain(null).findUserHistoryById(practiceId);
        }
        if (userHistory == null) {
            //开始新的学习
            return startNewStudy(subjectId, userId);
        }

        //userHistory不为空，说明有学习记录，继续学习
        Long userHistoryId = userHistory.getId();
        Integer progress = userHistory.getProgress();//当前进度
        //当前题目id
        Long topicId = this.topicDomainRepository.createUserPracticeDomain(null).findUserPracticeTopicId(userId,userHistoryId,progress);
        if (topicId == null) return CallResult.fail();
        TopicDTO topic = this.topicDomainRepository.findTopicAnswer(topicId,userId,userHistory.getId());
        PracticeDetailModel practiceModel = new PracticeDetailModel();
        practiceModel.setProgress(progress);
        practiceModel.setTotal(userHistory.getTopicTotal());
        practiceModel.setTopic(getTopicModelView(topic));
        practiceModel.setPracticeId(userHistory.getId().toString());
        int answered = userHistory.getProgress();
        if (answered != userHistory.getTopicTotal()){
            answered = userHistory.getProgress()-1;
        }
        practiceModel.setAnswered(answered);
        int trueNum = this.topicDomainRepository.createUserPracticeDomain(null).countUserPracticeTrueNum(userId,userHistory.getId());
        int wrongNum = this.topicDomainRepository.createUserPracticeDomain(null).countUserPracticeWrongNum(userId,userHistory.getId());
        practiceModel.setTrueNum(trueNum);
        practiceModel.setWrongNum(wrongNum);
        practiceModel.setNoAnswer(0);

        SubjectModel subject = this.topicDomainRepository.createSubjectDomain(null).findSubject(userHistory.getSubjectId());
        practiceModel.setSubjectName(subject.getSubjectName()+" "+subject.getSubjectGrade()+" "+subject.getSubjectTerm());
        List<Integer> subjectUnitList1 = JSON.parseArray(userHistory.getSubjectUnits(), Integer.class);
        practiceModel.setSubjectUnitList(subjectUnitList1);
        practiceModel.setCreateTime(new DateTime(userHistory.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));
        practiceModel.setFinishTime(userHistory.getFinishTime() == 0 ? "":new DateTime(userHistory.getFinishTime()).toString("yyyy-MM-dd HH:mm:ss"));
        //习题练习耗时时间
        practiceModel.setUseTime(userHistory.getFinishTime() == 0 ? "":useTime(userHistory.getFinishTime(),userHistory.getCreateTime()));

        List<Map<String,Object>> topicAnswerStatusList = this.topicDomainRepository.createUserPracticeDomain(null).findUserPracticeAll(userId,userHistory.getId());
        practiceModel.setTopicAnswerStatusList(topicAnswerStatusList);
        return CallResult.success(practiceModel);

    }

    private CallResult<Object> startNewStudy(Long subjectId, Long userId) {
        /*
        1.根据对应的学科id和传递的学科对应的单元列表 进行习题的随机练习
        2.随记生成50道题
        3.生成 UserHistory xu儿媳记录
        4.生成 UserPractice 此用户 学习记录id  题目id 完成状态 答案
        5.返回生成题目的第一道题
         */

        List<Integer> subjectUnitList = this.topicParams.getSubjectUnitList();
        String topicAreaPro = this.topicParams.getTopicAreaPro();//保留参数，理论上应当有参数传进来，但是前端未实现

        //随机50道题
        if (subjectUnitList == null) {
            subjectUnitList = new ArrayList<>();
        }
        List<Long> topicIdList = this.topicDomainRepository.findTopicRandom(subjectId,subjectUnitList);
        if (topicIdList.size() <= 0){
            return CallResult.fail(BusinessCodeEnum.TOPIC_NO_PRACTICE.getCode(),"没有习题");
        }

        //生成userHistory
        UserHistory userHistory = new UserHistory();
        userHistory.setCreateTime(System.currentTimeMillis());//创建时间
        userHistory.setHistoryStatus(HistoryStatus.NO_FINISH.getCode());//初始状态
        userHistory.setSubjectId(subjectId);//学科id
        userHistory.setProgress(1);//进度
        userHistory.setSubjectUnits(JSON.toJSONString(subjectUnitList));//学科单元
        userHistory.setTopicTotal(topicIdList.size());//题目总数
        if (StringUtils.isEmpty(topicAreaPro)){
            topicAreaPro = "全国";//题目区域
        }
        userHistory.setTopicPro(topicAreaPro);
        userHistory.setUserId(userId);//用户id
        userHistory.setFinishTime(0L);//完成时间
        userHistory.setErrorCount(0);//错误数目
        this.topicDomainRepository.createUserHistoryDomain(null).saveUserHistory(userHistory);

        for (Long topicId : topicIdList) {
            //生成 练习的用户回答状态详情
            UserPractice userPractice = new UserPractice();
            userPractice.setHistoryId(userHistory.getId());
            userPractice.setPStatus(0);
            userPractice.setTopicId(topicId);
            userPractice.setUserAnswer("");
            userPractice.setUserId(userId);
            this.topicDomainRepository.createUserPracticeDomain(null).saveUserPractice(userPractice);
        }

        TopicDTO topic = this.topicDomainRepository.findTopicAnswer(topicIdList.get(0),userId,userHistory.getId());
        TopicModelView topicModelView = getTopicModelView(topic);

        PracticeDetailModel practiceModel = new PracticeDetailModel();
        practiceModel.setTotal(userHistory.getTopicTotal());//设置随机题目总数
        practiceModel.setPracticeId(userHistory.getId().toString());//设置记录id
        //记录
        int answered = userHistory.getProgress();
        if (answered != userHistory.getTopicTotal()){
            //已答题目不等于题目总数，就-1
            answered = userHistory.getProgress()-1;
        }
        practiceModel.setAnswered(answered);

        int trueNum = this.topicDomainRepository.createUserPracticeDomain(null).countUserPracticeTrueNum(userId,userHistory.getId());
        int wrongNum = this.topicDomainRepository.createUserPracticeDomain(null).countUserPracticeWrongNum(userId,userHistory.getId());
        practiceModel.setTrueNum(trueNum);//答对正确数
        practiceModel.setWrongNum(wrongNum);//答错数
        practiceModel.setNoAnswer(0);//未答数，新练习未答数==0

        SubjectModel subject = this.topicDomainRepository.createSubjectDomain(null).findSubject(userHistory.getSubjectId());
        practiceModel.setSubjectName(subject.getSubjectName()+" "+subject.getSubjectGrade()+" "+subject.getSubjectTerm());
        practiceModel.setSubjectUnitList(subjectUnitList);
        practiceModel.setCreateTime(new DateTime(userHistory.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));
        practiceModel.setFinishTime(userHistory.getFinishTime() == 0 ? "":new DateTime(userHistory.getFinishTime()).toString("yyyy-MM-dd HH:mm:ss"));
        //习题练习耗时时间
        practiceModel.setUseTime(userHistory.getFinishTime() == 0 ? "":useTime(userHistory.getFinishTime(),userHistory.getCreateTime()));
        //题目详情
        practiceModel.setTopic(topicModelView);
        practiceModel.setProgress(1);

        List<Map<String,Object>> topicAnswerStatusList = this.topicDomainRepository.createUserPracticeDomain(null).findUserPracticeAll(userId,userHistory.getId());
        practiceModel.setTopicAnswerStatusList(topicAnswerStatusList);
        return CallResult.success(practiceModel);
    }

    private TopicModelView getTopicModelView(Topic topic) {
        TopicModelView topicModel = copyView(topic);
        if (topic == null){
            return null;
        }
        String topicImg = topic.getTopicImg();
        if (!StringUtils.isEmpty(topicImg)){
            List<String> topicImgList = JSON.parseArray(topicImg, String.class);
            topicModel.setTopicImgList(topicImgList);
        }
        if (topic.getTopicType() == TopicType.FILL_BLANK.getCode()){
            List<FillBlankChoice> fillBlankChoiceList = JSON.parseArray(topic.getTopicChoice(), FillBlankChoice.class);
            topicModel.setFillBlankTopicChoice(fillBlankChoiceList.size());
            topicModel.setFillBlankAnswer(fillBlankChoiceList);
        }
        if (topic.getTopicType() == TopicType.RADIO.getCode()){
            List<Map<String, ContentAndImage>> list = JSON.parseObject(topic.getTopicChoice(), new com.alibaba.fastjson.TypeReference<List<Map<String,ContentAndImage>>>(){});
            topicModel.setRadioChoice(list);
        }
        if (topic.getTopicType() == TopicType.MUL_CHOICE.getCode()){
            List<Map<String,ContentAndImage>> list = JSON.parseObject(topic.getTopicChoice(), new com.alibaba.fastjson.TypeReference<List<Map<String,ContentAndImage>>>(){});
            topicModel.setMulChoice(list);
        }

        topicModel.setLastUpdateTime(new DateTime(topic.getLastUpdateTime()).toString("yyyy-MM-dd"));
        return topicModel;
    }

    private TopicModelView getTopicModelView(TopicDTO topic) {
        TopicModelView topicModel = copyView(topic);
        if (topic == null){
            return null;
        }
        String topicImg = topic.getTopicImg();
        if (!StringUtils.isEmpty(topicImg)){
            List<String> topicImgList = JSON.parseArray(topicImg, String.class);
            topicModel.setTopicImgList(topicImgList);
        }
        if (topic.getTopicType() == TopicType.FILL_BLANK.getCode()){
            List<FillBlankChoice> fillBlankChoiceList = JSON.parseArray(topic.getTopicChoice(), FillBlankChoice.class);
            topicModel.setFillBlankTopicChoice(fillBlankChoiceList.size());
            topicModel.setFillBlankAnswer(fillBlankChoiceList);
            topicModel.setAnswer(null);
            String userAnswer = topic.getUserAnswer();
            if (!StringUtils.isEmpty(userAnswer)){
                String[] split = userAnswer.split("\\$#\\$");
                String sss = "";
                for (String s : split) {
                    sss += s + "  ";
                }
                topicModel.setUserAnswer(sss);
            }
        }
        if (topic.getTopicType() == TopicType.RADIO.getCode()){
            List<Map<String, ContentAndImage>> list = JSON.parseObject(topic.getTopicChoice(), new com.alibaba.fastjson.TypeReference<List<Map<String,ContentAndImage>>>(){});
            topicModel.setRadioChoice(list);
        }
        if (topic.getTopicType() == TopicType.MUL_CHOICE.getCode()){
            List<Map<String,ContentAndImage>> list = JSON.parseObject(topic.getTopicChoice(), new com.alibaba.fastjson.TypeReference<List<Map<String,ContentAndImage>>>(){});
            topicModel.setMulChoice(list);
        }

        topicModel.setLastUpdateTime(new DateTime(topic.getLastUpdateTime()).toString("yyyy-MM-dd"));
        return topicModel;
    }

    private TopicModelView copyView(TopicDTO topic){
        TopicModelView topicModel = new TopicModelView();
        if (topic == null){
            return null;
        }
        BeanUtils.copyProperties(topic,topicModel);
        topicModel.setId(topic.getId().toString());
        return topicModel;
    }

    private TopicModelView copyView(Topic topic){
        TopicModelView topicModel = new TopicModelView();
        if (topic == null){
            return null;
        }
        BeanUtils.copyProperties(topic,topicModel);
        topicModel.setId(topic.getId().toString());
        return topicModel;
    }


    //计时工具类
    private String useTime(Long finishTime, Long createTime) {
        long diff = finishTime - createTime;
        long dayTime = 24 * 60 * 60 * 1000;
        String useTime = "";
        long day = diff / dayTime;
        if (day > 0){
            useTime = day +"天";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(finishTime);
        int finishHour = calendar.get(Calendar.HOUR_OF_DAY);
        int finishMinute = calendar.get(Calendar.MINUTE);
        int finishSecond = calendar.get(Calendar.SECOND);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(createTime);
        int createHour = calendar1.get(Calendar.HOUR_OF_DAY);
        int createMinute = calendar1.get(Calendar.MINUTE);
        int createSecond = calendar1.get(Calendar.SECOND);
        int diffHour = finishHour - createHour;
        if (diffHour < 0){
            diffHour = -diffHour;
        }
        if (diffHour < 10){
            useTime += "0"+diffHour +":";
        }else {
            useTime += diffHour + ":";
        }
        int diffMinute = finishMinute - createMinute;
        if (diffMinute < 0){
            diffMinute = -diffMinute;
        }
        if (diffMinute < 10){
            useTime += "0"+diffMinute + ":";
        }else {
            useTime += diffMinute + ":";
        }
        int diffSecond = finishSecond - createSecond;
        if (diffSecond < 0){
            diffSecond = -diffSecond;
        }
        if (diffSecond < 10){
            useTime += "0"+ diffSecond;
        }else {
            useTime += String.valueOf(diffSecond);
        }
        return useTime;
    }

    private Topic topic;
    private UserHistory userHistory;
    public CallResult<Object> checkSubmitBiz() {
        //检查业务
        //主要检查  参数所对应的数据 是否合法 比如 题目是否存在 练习是否正常（未完成且可练习）
        Long topicId = this.topicParams.getTopicId();
        Long practiceId = this.topicParams.getPracticeId();
        Long userId = UserThreadLocal.get();

        if (topic == null) {
            //题目为空
            return CallResult.fail(BusinessCodeEnum.TOPIC_NOT_EXIST.getCode(),BusinessCodeEnum.TOPIC_NOT_EXIST.getMsg());
        }

        //以防 做这个业务逻辑的时候 已经被别的线程 修改过了
        UserHistory userHistory = this.topicDomainRepository.createUserHistoryDomain(null).findUserHistoryById(practiceId);
        if (userHistory == null) {
            return CallResult.fail(BusinessCodeEnum.PRACTICE_NO_EXIST.getCode(),"练习题不存在");
        }
        if (userHistory.getHistoryStatus() == HistoryStatus.FINISHED.getCode()) {
            return CallResult.fail(BusinessCodeEnum.PRACTICE_FINISHED.getCode(),BusinessCodeEnum.PRACTICE_FINISHED.getMsg());
        }
        if (userHistory.getHistoryStatus() == HistoryStatus.CANCEL.getCode()){
            return CallResult.fail(BusinessCodeEnum.PRACTICE_CANCEL.getCode(),BusinessCodeEnum.PRACTICE_CANCEL.getMsg());
        }
        this.topic = topic;
        this.userHistory = userHistory;
        return CallResult.success();
    }

    public CallResult<Object> submit(TopicParams topicParams) {
        Integer topicType = topicParams.getTopicType();
        Long topicId = topicParams.getTopicId();
        Long practiceId = topicParams.getPracticeId();
        Long userId = UserThreadLocal.get();
        String answer = topicParams.getAnswer();
        String topicAnswer = topic.getTopicAnswer();
        boolean finish = false;
        boolean answerFlag = false;
        if (topicType == TopicType.FILL_BLANK.getCode()) {
            if (!StringUtils.isEmpty(answer)) {
                String[] str = answer.split("\\$#\\$");
                TopicModelView topicModel = getTopicModelView(topic);
                if (str.length == topicModel.getFillBlankTopicChoice()) {
                    for (int i = 0; i < str.length; i++) {
                        FillBlankChoice anObject = topicModel.getFillBlankAnswer().get(i);
                        log.info("提交答案:{}", str[i]);
                        log.info("正确答案:{}", anObject);
                        if (str[i].equals(anObject.getContent())) {
                            answerFlag = true;
                        } else {
                            answerFlag = false;
                            break;
                        }
                    }
                }
            }
        }
        if (topicType == TopicType.RADIO.getCode()){
            if (topicAnswer.equals(answer)){
                answerFlag = true;
            }
        }
        if (topicType == TopicType.MUL_CHOICE.getCode()){
            if (topicAnswer.equals(answer)){
                answerFlag = true;
            }
        }
        if (topicType == TopicType.JUDGE.getCode()){
            if (topicAnswer.equals(answer)){
                answerFlag = true;
            }
        }
        UserHistoryDomain historyDomain = this.topicDomainRepository.createUserHistoryDomain(null);
        UserProblemDomain userProblemDomain = this.topicDomainRepository.createUserProblem(null);
        UserPracticeDomain userPracticeDomain = this.topicDomainRepository.createUserPracticeDomain(null);

        //如果是错题，加入错题本，如果错题本有此数据 错误数量加一
        //更新 用户练习表 对此次练习的数据 标识状态 把用户的答案进行记录
        if (answerFlag != true) {
            //加入错题本
            UserProblem userProblem = userProblemDomain.getUserProblem(userId, topicId);
            if (userProblem == null) {
                userProblem = new UserProblem();
                userProblem.setErrorCount(1);
                userProblem.setErrorStatus(ErrorStatus.NO_SOLVE.getCode());
                userProblem.setUserId(userId);
                userProblem.setTopicId(topicId);
                userProblem.setSubjectId(topicParams.getSubjectId());
                userProblem.setErrorAnswer(answer);
                userProblem.setErrorTime(System.currentTimeMillis());
                userProblemDomain.saveUserProblem(userProblem);
            } else {
                userProblemDomain.updateUserProblemErrorCount(userId, topicId, answer);
            }
            userPracticeDomain.updateUserPractice(userHistory.getId(), topicId, userId, answer, 1);
        } else {
            userPracticeDomain.updateUserPractice(userHistory.getId(), topicId, userId, answer, 2);
        }
        if (userHistory.getProgress() >= userHistory.getTopicTotal()){
            //已经是最后一道题了,更新状态已完成和完成时间
            historyDomain.updateUserHistoryStatus(userHistory.getId(), HistoryStatus.FINISHED.getCode(), System.currentTimeMillis());
            finish = true;
        }

        TopicSubmitModel topicSubmitModel = new TopicSubmitModel();//返回前端的模型
        topicSubmitModel.setTopicType(topicType);

        //返回的是 正确答案
        topicSubmitModel.setAnswer(topicAnswer);

        //特例：填空题的答案不是 topicAnswer字段 而是 topic_choice字段 （json字符串）
        if (topicType == TopicType.FILL_BLANK.getCode()){
            TopicModelView topicModelView = getTopicModelView(topic);
            StringBuilder fillBlankAnswer = new StringBuilder();
            for (FillBlankChoice fillBlankChoice : topicModelView.getFillBlankAnswer()){
                fillBlankAnswer.append(fillBlankChoice.getId()).append(".").append(fillBlankChoice.getContent()).append(";");
            }
            topicSubmitModel.setAnswer(fillBlankAnswer.toString());
        }

        //此次练习是否完成 ，做到最后一道题 提交 就是完成
        topicSubmitModel.setFinish(finish);

        //题是否正确
        topicSubmitModel.setAnswerFlag(answerFlag);

        return CallResult.success(topicSubmitModel);
    }


    public CallResult<Object> jump(TopicParams topicParams) {
        Integer page = topicParams.getPage();
        if (page == null) {
            return CallResult.fail(BusinessCodeEnum.TOPIC_PARAM_ERROR.getCode(),"参数错误");
        }
        Long userId = UserThreadLocal.get();
        Long practiceId = topicParams.getPracticeId();
        UserHistory userHistory = this.topicDomainRepository.createUserHistoryDomain(null).findUserHistoryById(practiceId);
        Long topicId = this.topicDomainRepository.createUserPracticeDomain(null).findUserPracticeTopicId(userId,userHistory.getId(),page);
        TopicDTO topic = this.topicDomainRepository.findTopicAnswer(topicId,userId,userHistory.getId());
        TopicModelView topicModelView = getTopicModelView(topic);
        Integer topicTotal = userHistory.getTopicTotal();
        Map<String,Object> map = new HashMap<>();
        map.put("total",topicTotal);
        map.put("progress",userHistory.getProgress());
        map.put("topic",topicModelView);

        return CallResult.success(map);
    }

    public CallResult<Object> practiceHistory(TopicParams topicParams) {
        Long userId = UserThreadLocal.get();
        Integer page = topicParams.getPage();
        Integer pageSize = topicParams.getPageSize();
        Page<UserHistory> userHistoryPage = this.topicDomainRepository.createUserHistoryDomain(null).findUserHistoryList(userId,page,pageSize);

        List<UserHistory> userHistoryList = userHistoryPage.getRecords();
        List<UserHistoryModel> userHistoryModelList = new ArrayList<>();

        for (UserHistory userHistory : userHistoryList) {

            UserHistoryModel userHistoryModel = new UserHistoryModel();
            userHistoryModel.setId(userHistory.getId());
            userHistoryModel.setCreateTime(new DateTime(userHistory.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));
            userHistoryModel.setHistoryStatus(userHistory.getHistoryStatus());
            SubjectModel subjectModel = this.topicDomainRepository.createSubjectDomain(null).findSubject(userHistory.getSubjectId());
            userHistoryModel.setSubjectId(subjectModel.getId());
            userHistoryModel.setFinishTime(userHistory.getFinishTime() == 0 ? "":new DateTime(userHistory.getFinishTime()).toString("yyyy-MM-dd HH:mm:ss"));
            List<Integer> subjectUnitList = JSON.parseArray(userHistory.getSubjectUnits(),Integer.class);
            userHistoryModel.setSubjectUnitList(subjectUnitList);
            userHistoryModel.setUseTime(userHistory.getFinishTime() == 0 ? "":useTime(userHistory.getFinishTime(),userHistory.getCreateTime()));
            List<Long> courseIdList = this.topicDomainRepository.createCourseDomain(null).findCourseIdListBySubjectId(subjectModel.getId());
            int count = this.topicDomainRepository.createUserCourseDomain(null).countUserCourseInCourseIdList(userId,courseIdList,System.currentTimeMillis());
            if (count > 0) {
                //判断是否此学习还在购买内
                userHistoryModel.setStatus(0);
            } else {
                userHistoryModel.setStatus(1);
            }
            userHistoryModelList.add(userHistoryModel);
        }
        ListPageModel<UserHistoryModel> listPageModel = new ListPageModel<>();
        listPageModel.setList(userHistoryModelList);
        listPageModel.setSize(userHistoryPage.getTotal());
        listPageModel.setPage(page);
        listPageModel.setPageSize(pageSize);
        listPageModel.setPageCount(userHistoryPage.getPages());

        return CallResult.success(listPageModel);
    }

    public CallResult<Object> userProblemSearch(TopicParams topicParams) {
        Integer page = topicParams.getPage();
        Integer pageSize = topicParams.getPageSize();
        String subjectName = topicParams.getSubjectName();
        String subjectGrade = topicParams.getSubjectGrade();
        String subjectTerm = topicParams.getSubjectTerm();
        Long userId = UserThreadLocal.get();

        //去查询是否有查询的学科条件
        Long searchSubjectId = this.topicDomainRepository.createSubjectDomain(null).findSubjectByInfo(subjectName,subjectGrade,subjectTerm);
        Page<UserProblem> userProblemPage = null;
        if (searchSubjectId == null) {
            userProblemPage = this.topicDomainRepository.createUserProblem(null).findUserProblemList(userId,ErrorStatus.NO_SOLVE.getCode(),page,pageSize);
        } else {
            userProblemPage = this.topicDomainRepository.createUserProblem(null).findUserProblemListBySubjectId(searchSubjectId,userId,ErrorStatus.NO_SOLVE.getCode(),page,pageSize);
        }
        List<UserProblemModel> userProblemModelList = new ArrayList<>();
        List<UserProblem> userProblemList = userProblemPage.getRecords();
        for (UserProblem userProblem : userProblemList) {
            Long topicId = userProblem.getTopicId();
            Long subjectId = userProblem.getSubjectId();
            Topic topic = this.topicDomainRepository.findTopicById(topicId);
            TopicModelView topicModelView = getTopicModelView(topic);
            SubjectModel subjectModel = this.topicDomainRepository.createSubjectDomain(null).findSubject(subjectId);
            UserProblemModel userProblemModel = new UserProblemModel();
            userProblemModel.setErrorCount(userProblem.getErrorCount());
            userProblemModel.setTopic(topicModelView);
            userProblemModel.setSubject(subjectModel);
            userProblemModel.setProblemId(userProblem.getId());
            userProblemModel.setErrorTime(new DateTime(userProblem.getErrorTime()).toString("yyyy-MM-dd HH:mm:ss"));
            userProblemModel.setErrorAnswer(userProblem.getErrorAnswer());
            userProblemModelList.add(userProblemModel);
        }
        ListPageModel<UserProblemModel> listPageModel = new ListPageModel<>();
        listPageModel.setList(userProblemModelList);
        listPageModel.setPage(page);
        listPageModel.setPageSize(pageSize);
        listPageModel.setSize((int)userProblemPage.getTotal());
        return CallResult.success(listPageModel);
    }
}
