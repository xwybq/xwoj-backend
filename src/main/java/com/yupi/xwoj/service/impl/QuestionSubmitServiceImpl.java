package com.yupi.xwoj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.xwoj.common.ErrorCode;
import com.yupi.xwoj.exception.BusinessException;
import com.yupi.xwoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yupi.xwoj.model.entity.*;
import com.yupi.xwoj.model.enums.JudgeInfoMessageEnum;
import com.yupi.xwoj.model.enums.QuestionSubmitLanguageEnum;
import com.yupi.xwoj.model.enums.QuestionSubmitStatusEnum;
import com.yupi.xwoj.service.PostService;
import com.yupi.xwoj.service.PostThumbService;
import com.yupi.xwoj.service.QuestionService;
import com.yupi.xwoj.service.QuestionSubmitService;
import com.yupi.xwoj.mapper.QuestionSubmitMapper;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author wangjialeNB
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2025-10-01 15:44:11
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {
    @Resource
    private QuestionService questionService;

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        String language = questionSubmitAddRequest.getLanguage();
        //         判断编程语言是否合法
        QuestionSubmitLanguageEnum questionSubmitLanguageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (questionSubmitLanguageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "语言不存在");
        }
        long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已点赞
        long userId = loginUser.getId();
        /*// 每个用户串行点赞
        // 锁必须要包裹住事务方法，否则会失效
        QuestionSubmitService questionSubmitService = (QuestionSubmitService) AopContext.currentProxy();
        // 锁的粒度是 userId，防止并发问题
        synchronized (String.valueOf(userId).intern()) {
            return questionSubmitService.doQuestionSubmitInner(userId, questionId);
        }*/
        QuestionSubmit questionSubmit = QuestionSubmit.builder()
                .userId(userId)
                .questionId(questionId)
                .language(language)
                .code(questionSubmitAddRequest.getCode())
                //初始状态为待判题
                .status(QuestionSubmitStatusEnum.WAITING.getValue())
                .judgeInfo("{}")
                .build();
        boolean success = this.save(questionSubmit);
        if (!success) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        return questionSubmit.getId();
    }

    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param questionId
     * @return
     */
    /*@Override
    @Transactional(rollbackFor = Exception.class)
    public int doQuestionSubmitInner(long userId, long questionId) {
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        QueryWrapper<QuestionSubmit> thumbQueryWrapper = new QueryWrapper<>(questionSubmit);
        QuestionSubmit oldQuestionSubmit = this.getOne(thumbQueryWrapper);
        boolean result;
        // 已点赞
        if (oldQuestionSubmit != null) {
            result = this.remove(thumbQueryWrapper);
            if (result) {
                // 状态数 - 1
                result = this.update()
                        .eq("id", questionId)
                        .gt("status", 0)
                        .setSql("status = status - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未点赞
            questionSubmit.setStatus(0);
            result = this.save(questionSubmit);
            if (result) {
                // 点赞数 + 1
                result = this.update()
                        .eq("id", questionId)
                        .setSql("status = status + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }*/
}




