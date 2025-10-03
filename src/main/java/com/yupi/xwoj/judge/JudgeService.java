package com.yupi.xwoj.judge;

import com.yupi.xwoj.exception.BusinessException;
import com.yupi.xwoj.judge.codsandbox.model.ExecuteCodeRequest;
import com.yupi.xwoj.judge.codsandbox.model.ExecuteCodeResponse;
import com.yupi.xwoj.model.entity.QuestionSubmit;
import com.yupi.xwoj.model.vo.QuestionSubmitVO;

/**
 * 判题服务
 */
public interface JudgeService {
    /**
     * 判题
     *
     * @param questionSubmitId 题目提交id
     * @return 判题信息
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
