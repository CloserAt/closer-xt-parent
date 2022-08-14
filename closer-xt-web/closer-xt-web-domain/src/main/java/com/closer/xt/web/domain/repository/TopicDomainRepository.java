package com.closer.xt.web.domain.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.closer.xt.pojo.Topic;
import com.closer.xt.pojo.UserPractice;
import com.closer.xt.web.dao.TopicMapper;
import com.closer.xt.web.dao.data.TopicDTO;
import com.closer.xt.web.domain.*;
import com.closer.xt.web.model.params.*;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TopicDomainRepository {
    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private CourseDomainRepository courseDomainRepository;

    @Autowired
    private UserCourseDomainRepository userCourseDomainRepository;

    @Autowired
    private UserHistoryDomainRepository userHistoryDomainRepository;

    @Autowired
    private SubjectDomainRepository subjectDomainRepository;

    @Autowired
    private UserPracticeDomainRepository userPracticeDomainRepository;

    @Autowired
    private UserProblemDomainRepository userProblemDomainRepository;

    public TopicDomain createDomain(TopicParams topicParams) {
        return new TopicDomain(this,topicParams);
    }

    public CourseDomain createCourseDomain(CourseParams courseParams) {
        return courseDomainRepository.createDomain(courseParams);
    }

    public UserCourseDomain createUserCourseDomain(UserCourseParams userCourseParams) {
        return userCourseDomainRepository.createDomain(userCourseParams);
    }

    public UserHistoryDomain createUserHistoryDomain(UserHistoryParams userHistoryParams) {
        return userHistoryDomainRepository.createDomain(userHistoryParams);
    }

    public UserPracticeDomain createUserPracticeDomain(UserPracticeParams userPracticeParams) {
        return userPracticeDomainRepository.createDomain(userPracticeParams);
    }

    public List<Long> findTopicRandom(Long subjectId, List<Integer> subjectUnitList) {
        LambdaQueryWrapper<Topic> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Topic::getTopicSubject,subjectId);

        if (CollectionUtils.isNotEmpty(subjectUnitList)) {
            queryWrapper.in(Topic::getSubjectUnit, subjectUnitList);
        }
        queryWrapper.last("order by RAND() limit 50");//RAND()函数，随即查询50条
        queryWrapper.select(Topic::getId);
        List<Topic> topicList = this.topicMapper.selectList(queryWrapper);

        return topicList.stream().map(Topic::getId).collect(Collectors.toList());
    }

    public SubjectDomain createSubjectDomain(SubjectParams subjectParams) {
        return subjectDomainRepository.createDomain(subjectParams);
    }

    public TopicDTO findTopicAnswer(Long topicId, Long userId, Long userHistoryId) {
        TopicDTO topicDTO = new TopicDTO();
        Topic topic = this.topicMapper.selectById(topicId);
        UserPractice userPractice = this.userPracticeDomainRepository.createDomain(null).findUserPracticeByTopicId(userId,topicId,userHistoryId);
        BeanUtils.copyProperties(topic,topicDTO);
        topicDTO.setTopicAnswer(userPractice.getUserAnswer());
        topicDTO.setPStatus(userPractice.getPStatus());
        return topicDTO;
        //return this.topicMapper.findTopicAnswer(topicId,userId,userHistoryId);
    }

    public UserProblemDomain createUserProblem(UserProblemParams userProblemParams) {
        return userProblemDomainRepository.createDomain(userProblemParams);
    }

    public Topic findTopicById(Long topicId) {
        return this.topicMapper.selectById(topicId);
    }
}
