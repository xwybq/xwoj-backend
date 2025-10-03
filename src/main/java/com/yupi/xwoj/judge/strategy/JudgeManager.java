package com.yupi.xwoj.judge.strategy;

import com.yupi.xwoj.model.dto.questionsubmit.JudgeInfo;
import com.yupi.xwoj.model.entity.Question;
import com.yupi.xwoj.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理
 */
@Service
public class JudgeManager {
    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
            return judgeStrategy.doJudge(judgeContext);
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
