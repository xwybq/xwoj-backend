package com.yupi.xwoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.xwoj.model.dto.question.QuestionQueryRequest;
import com.yupi.xwoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yupi.xwoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yupi.xwoj.model.entity.Question;
import com.yupi.xwoj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.xwoj.model.entity.User;
import com.yupi.xwoj.model.vo.QuestionSubmitVO;
import com.yupi.xwoj.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangjialeNB
 * @description 针对表【question_submit(题目提交)】的数据库操作Service
 * @createDate 2025-10-01 15:44:11
 */
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 点赞
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 题目提交（内部服务）
     *
     * @param userId
     * @param questionId
     * @return
     */
//    int doQuestionSubmitInner(long userId, long questionId);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /*    *//**
     * 从 ES 查询
     *
     * @param questionQueryRequest
     * @return
     *//*
    Page<Question> searchFromEs(QuestionQueryRequest questionQueryRequest);*/

    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}
