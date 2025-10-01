package com.yupi.xwoj.service;

import com.yupi.xwoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yupi.xwoj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.xwoj.model.entity.User;

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
}
