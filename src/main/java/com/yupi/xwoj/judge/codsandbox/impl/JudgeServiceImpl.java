package com.yupi.xwoj.judge.codsandbox.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.yupi.xwoj.common.ErrorCode;
import com.yupi.xwoj.exception.BusinessException;
import com.yupi.xwoj.judge.JudgeService;
import com.yupi.xwoj.judge.codsandbox.CodeSandbox;
import com.yupi.xwoj.judge.codsandbox.CodeSandboxFactory;
import com.yupi.xwoj.judge.codsandbox.CodeSandboxPoxy;
import com.yupi.xwoj.judge.codsandbox.model.ExecuteCodeRequest;
import com.yupi.xwoj.judge.codsandbox.model.ExecuteCodeResponse;
import com.yupi.xwoj.judge.strategy.DefaultJudgeStrategy;
import com.yupi.xwoj.judge.strategy.JudgeContext;
import com.yupi.xwoj.judge.strategy.JudgeManager;
import com.yupi.xwoj.judge.strategy.JudgeStrategy;
import com.yupi.xwoj.model.dto.question.JudgeCase;
import com.yupi.xwoj.judge.codsandbox.model.JudgeInfo;
import com.yupi.xwoj.model.entity.Question;
import com.yupi.xwoj.model.entity.QuestionSubmit;
import com.yupi.xwoj.model.enums.QuestionSubmitStatusEnum;
import com.yupi.xwoj.service.QuestionService;
import com.yupi.xwoj.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private JudgeManager judgeManager;

    @Value("${codesandbox.type:example}")
    private String type;


    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        //1.传入题目提交的ID，获取到对应的题目、提交信息
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目提交不存在");
        }
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        Integer status = questionSubmit.getStatus();
        Long questionId = questionSubmit.getQuestionId();


        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        String judgeCaseStr = question.getJudgeCase();
        if (StrUtil.isBlank(judgeCaseStr)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目未配置判题用例");
        }
        //1.1 如果题目的提交状态不是待判题，那么就不进行判题
        if (!status.equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中，不能重复判题");
        }
        //1.2否则更改题目提交的状态为判题中
        QuestionSubmit questionSubmitUpdate = QuestionSubmit.builder()
                .id(questionSubmitId)
                .status(QuestionSubmitStatusEnum.JUDGING.getValue())
                .build();
        boolean updateSuccess = questionSubmitService.updateById(questionSubmitUpdate);
        if (!updateSuccess) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新题目提交状态失败");
        }

        //1.3 解析出判题用例
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());

        //2.根据题目信息，构建代码沙箱的执行请求
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .inputList(inputList)
                .code(code)
                .language(language)
                .build();
        //3.调用代码沙箱执行代码
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxPoxy(codeSandbox);
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        //4.根据代码沙箱的执行结果，构建判题信息
        List<String> outputList = executeCodeResponse.getOutputList();
//        if (outputList.size() != judgeCaseList.size()) {
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "代码执行结果与用例数量不匹配");
//        }
        JudgeInfo judgeInfo = executeCodeResponse.getJudgeInfo();

        //4.1 构建判题上下文
        JudgeContext judgeContext = JudgeContext.builder()
                .judgeInfo(judgeInfo)
                .outputList(outputList)
                .inputList(inputList)
                .judgeCaseList(judgeCaseList)
                .question(question)
                .questionSubmit(questionSubmit)
                .build();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        //4.2 执行判题，根据判题上下文，返回判题信息
        JudgeInfo judgeInfoResult = judgeManager.doJudge(judgeContext);
        //4.3 更新题目提交的状态为判题完成
        questionSubmitUpdate = QuestionSubmit.builder()
                .id(questionSubmitId)
                .status(QuestionSubmitStatusEnum.SUCCESS.getValue())
                .judgeInfo(JSONUtil.toJsonStr(judgeInfoResult))
                .build();
        updateSuccess = questionSubmitService.updateById(questionSubmitUpdate);
        if (!updateSuccess) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新题目提交状态失败");
        }
        questionSubmit = questionSubmitService.getById(questionSubmitId);
        return questionSubmit;
    }
}
