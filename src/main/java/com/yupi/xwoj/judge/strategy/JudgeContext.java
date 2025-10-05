package com.yupi.xwoj.judge.strategy;

import com.yupi.xwoj.model.dto.question.JudgeCase;
import com.yupi.xwoj.judge.codsandbox.model.JudgeInfo;
import com.yupi.xwoj.model.entity.Question;
import com.yupi.xwoj.model.entity.QuestionSubmit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 判题上下文（用于定义在判题中会用到的变量）策略模式
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JudgeContext {
    private JudgeInfo judgeInfo;

    private List<String> outputList;
    private List<String> inputList;

    private Question question;

    private List<JudgeCase> judgeCaseList;

    private QuestionSubmit  questionSubmit;
}
