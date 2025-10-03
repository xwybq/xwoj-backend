package com.yupi.xwoj.judge.codsandbox;

import com.yupi.xwoj.judge.codsandbox.model.ExecuteCodeRequest;
import com.yupi.xwoj.judge.codsandbox.model.ExecuteCodeResponse;

public interface CodeSandbox {

    /**
     * 执行代码
     *
     * @param executeCodeRequest 执行代码请求
     * @return 执行代码响应
     */
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
