package com.yupi.xwoj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.xwoj.annotation.AuthCheck;
import com.yupi.xwoj.common.BaseResponse;
import com.yupi.xwoj.common.ErrorCode;
import com.yupi.xwoj.common.ResultUtils;
import com.yupi.xwoj.constant.UserConstant;
import com.yupi.xwoj.exception.BusinessException;

import com.yupi.xwoj.model.dto.question.QuestionQueryRequest;
import com.yupi.xwoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.yupi.xwoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.yupi.xwoj.model.entity.Question;
import com.yupi.xwoj.model.entity.QuestionSubmit;
import com.yupi.xwoj.model.entity.User;
import com.yupi.xwoj.model.vo.QuestionSubmitVO;
import com.yupi.xwoj.service.QuestionSubmitService;
import com.yupi.xwoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 帖子点赞接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
//@RequestMapping("/question_submit")
@Slf4j
@Deprecated
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     *
     * 点赞 / 取消点赞
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     *//*
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                               HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(questionSubmitId);
    }


    *//**
     * 分页获取题目提交列表（除了管理员外，普通用户只能看到非管理员的题目提交）
     *
     * @param questionSubmitQueryRequest
     * @return
     *//*
    @PostMapping("/list/page")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)

    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest
            , HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        //得到了原始数据
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        final User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
    }
*/
}
