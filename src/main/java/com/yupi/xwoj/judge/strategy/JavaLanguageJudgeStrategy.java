package com.yupi.xwoj.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.yupi.xwoj.judge.JudgeContext;
import com.yupi.xwoj.model.dto.question.JudgeCase;
import com.yupi.xwoj.model.dto.question.JudgeConfig;
import com.yupi.xwoj.judge.codsandbox.model.JudgeInfo;
import com.yupi.xwoj.model.entity.Question;
import com.yupi.xwoj.model.enums.JudgeInfoMessageEnum;

import java.util.List;
import java.util.Optional;

public class JavaLanguageJudgeStrategy implements JudgeStrategy {

    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memory = Optional.ofNullable(judgeInfo.getMemory()).orElse(0L);
        Long time = Optional.ofNullable(judgeInfo.getTime()).orElse(0L);
        List<String> outputList = judgeContext.getOutputList();
        List<String> inputList = judgeContext.getInputList();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        Question question = judgeContext.getQuestion();


        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPTED;
        //5.根据判题信息，更新题目提交的判题状态、判题信息
        JudgeInfo judgeInfoResponse = JudgeInfo.builder()
                .memory(memory)
                .time(time)
                .build();
        //4.1 如果输出的数量和判题用例的数量不一致，那么判题信息为错误答案
        if (outputList.size() != inputList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        //4.2 如果输出的结果和判题用例的结果不一致，那么判题信息为错误答案
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            String output = outputList.get(i);
            if (!output.equals(judgeCase.getOutput())) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }
        //4.3题目限制判断
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        //Java程序本身需要额外执行10秒钟
        Long timeLimit = judgeConfig.getTimeLimit();
        Long memoryLimit = judgeConfig.getMemoryLimit();
        if (memory > memoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        long JAVA_PROGRAM_TIME_COST = 10000L;
        if (time - JAVA_PROGRAM_TIME_COST > timeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        //4.4 如果没有错误，那么判题信息为通过
        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        return judgeInfoResponse;
    }
}
