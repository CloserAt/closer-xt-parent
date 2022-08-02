package com.closer.xt.admin.domain;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.closer.xt.admin.domain.repository.TopicDomainRepository;
import com.closer.xt.admin.model.ListPageModel;
import com.closer.xt.admin.model.TopicModel;
import com.closer.xt.admin.params.TopicParams;
import com.closer.xt.common.enums.TopicType;
import com.closer.xt.common.model.CallResult;
import com.closer.xt.pojo.Subject;
import com.closer.xt.pojo.Topic;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class TopicDomain {
    private TopicDomainRepository topicDomainRepository;
    private TopicParams topicParams;
    public TopicDomain(TopicDomainRepository topicDomainRepository, TopicParams topicParams) {
        this.topicDomainRepository = topicDomainRepository;
        this.topicParams = topicParams;
    }

    public CallResult<Object> findTopicList(TopicParams topicParams) {
        int pageSize = topicParams.getPageSize();
        int page = topicParams.getPage();
        String topicTitle = topicParams.getTopicTitle();
        Long subjectId = topicParams.getSubjectId();
        Page<Topic> topicList = this.topicDomainRepository.findPage(page, pageSize, topicTitle, subjectId);
        List<TopicModel> topicModelList = copyList(topicList.getRecords());
        ListPageModel<TopicModel> listModel = new ListPageModel<>();
        listModel.setList(topicModelList);
        listModel.setSize(topicList.getTotal());
        return CallResult.success(listModel);
    }

    private TopicModel copy(Topic topic){
        TopicModel topicModel = new TopicModel();
        BeanUtils.copyProperties(topic,topicModel);
        return topicModel;
    }
    private List<TopicModel> copyList(List<Topic> topicList){
        if (topicList == null){
            return null;
        }
        List<Subject> subjectList = this.topicDomainRepository.createSubjectDomain(null).findAllSubjectList();
        List<TopicModel> topicModelList = new ArrayList<>();
        for (Topic topic : topicList){
            TopicModel model = copy(topic);
            model.setTopicTypeStr(displayTopicType(topic.getTopicType()));
            model.setSubjectStr(displayTopicSubject(subjectList,topic.getTopicSubject()));
            model.setCreateTime(new DateTime(topic.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));
            model.setLastUpdateTime(new DateTime(topic.getLastUpdateTime()).toString("yyyy-MM-dd HH:mm:ss"));
            topicModelList.add(model);
        }
        return topicModelList;
    }
    private String displayTopicSubject(List<Subject> subjectList, Long subject) {
        for (Subject subject1 : subjectList){
            if (subject1.getId().equals(subject)){
                return subject1.getSubjectName() +" "+ subject1.getSubjectGrade() +" "+ subject1.getSubjectTerm();
            }
        }
        return "";
    }
    private String displayTopicType(Integer topicType) {
        switch (TopicType.valueOfCode(topicType)){
            case FILL_BLANK:
                return "填空题";
            case RADIO:
                return "单选题";
            case QA:
                return "问答题";
            case MUL_CHOICE:
                return "多选题";
            case JUDGE:
                return "判断题";
            default:return "";
        }
    }

}
