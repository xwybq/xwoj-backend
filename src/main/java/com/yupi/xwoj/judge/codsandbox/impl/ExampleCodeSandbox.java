package com.yupi.xwoj.judge.codsandbox.impl;

import com.yupi.xwoj.judge.codsandbox.CodeSandbox;
import com.yupi.xwoj.judge.codsandbox.model.ExecuteCodeRequest;
import com.yupi.xwoj.judge.codsandbox.model.ExecuteCodeResponse;
import com.yupi.xwoj.judge.codsandbox.model.JudgeInfo;
import com.yupi.xwoj.model.enums.JudgeInfoMessageEnum;
import com.yupi.xwoj.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 示例代码沙箱
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        // 模拟远程调用，直接返回成功
        JudgeInfo judgeInfo = JudgeInfo.builder()
                .message(JudgeInfoMessageEnum.ACCEPTED.getValue())
                .memory(1000L)
                .time(1000L)
                .build();


        ExecuteCodeResponse executeCodeResponse = ExecuteCodeResponse
                .builder()
                .outputList(inputList)
                .message("测试执行成功")
                .judgeInfo(judgeInfo)
                .status(QuestionSubmitStatusEnum.SUCCESS.getValue())
                .build();
        return executeCodeResponse;
    }
}
