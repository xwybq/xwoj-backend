package com.yupi.xwoj.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.xwoj.common.ErrorCode;
import com.yupi.xwoj.constant.CommonConstant;
import com.yupi.xwoj.exception.BusinessException;
import com.yupi.xwoj.judge.JudgeService;
import com.yupi.xwoj.model.dto.question.QuestionQueryRequest;
import com.yupi.xwoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yupi.xwoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yupi.xwoj.model.entity.*;
import com.yupi.xwoj.model.enums.JudgeInfoMessageEnum;
import com.yupi.xwoj.model.enums.QuestionSubmitLanguageEnum;
import com.yupi.xwoj.model.enums.QuestionSubmitStatusEnum;
import com.yupi.xwoj.model.vo.QuestionSubmitVO;
import com.yupi.xwoj.model.vo.QuestionVO;
import com.yupi.xwoj.model.vo.UserVO;
import com.yupi.xwoj.service.*;
import com.yupi.xwoj.mapper.QuestionSubmitMapper;
import com.yupi.xwoj.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * @author wangjialeNB
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2025-10-01 15:44:11
 */
@Service
@Slf4j
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit> implements QuestionSubmitService {
    @Resource
    private QuestionService questionService;
    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private JudgeService judgeService;

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

        long userId = loginUser.getId();
        QuestionSubmit questionSubmit = QuestionSubmit.builder().userId(userId).questionId(questionId).language(language).code(questionSubmitAddRequest.getCode())
                //初始状态为待判题
                .status(QuestionSubmitStatusEnum.WAITING.getValue()).judgeInfo("{}").build();
        boolean success = this.save(questionSubmit);
        if (!success) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }
        Long questionSubmitId = questionSubmit.getId();

        log.info("questionSubmit保存成功，准备提交异步判题任务，questionSubmitId: {}", questionSubmitId);

// 2. 异步任务用自定义线程池执行
        CompletableFuture.runAsync(() -> {
            try {
                judgeService.doJudge(questionSubmitId);
            } catch (Exception e) {
                log.error("异步判题任务执行失败，questionSubmitId: {}", questionSubmitId, e);
            }
        }); // 传入自定义线程池
        // 提交成功后，执行判题服务（只打印日志，不调用judgeService）
        // 替换 CompletableFuture，用原生Thread测试
    /*    new Thread(() -> {
            log.error("=== 原生Thread启动！questionSubmitId: {} ===", questionSubmitId);
        }, "TestJudgeThread-" + questionSubmitId).start();*/
//        judgeService.doJudge(questionSubmitId);
        log.info("异步判题任务已提交（不管是否执行），questionSubmitId: {}", questionSubmitId);

        return questionSubmitId;
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


    /**
     * 获取查询包装类
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }

        String language = questionSubmitQueryRequest.getLanguage();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        Integer status = questionSubmitQueryRequest.getStatus();

        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();


        //拼接查询条件
        queryWrapper.like(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.like(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.like(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.like(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        long userId = loginUser.getId();
        // 非管理员和非题目提交用户，隐藏代码
        if (userId != questionSubmit.getUserId() && !userService.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollUtil.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }
}




