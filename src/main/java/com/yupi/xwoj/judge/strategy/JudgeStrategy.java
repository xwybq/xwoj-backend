package com.yupi.xwoj.judge.strategy;

import com.yupi.xwoj.judge.codsandbox.model.JudgeInfo;

public interface JudgeStrategy {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}
