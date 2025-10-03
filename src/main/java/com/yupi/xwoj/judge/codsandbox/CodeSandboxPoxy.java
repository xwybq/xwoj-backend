package com.yupi.xwoj.judge.codsandbox;

import com.yupi.xwoj.judge.codsandbox.model.ExecuteCodeRequest;
import com.yupi.xwoj.judge.codsandbox.model.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CodeSandboxPoxy implements CodeSandbox {

    private final CodeSandbox codeSandbox;

    public CodeSandboxPoxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息：{}", executeCodeRequest);
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应信息：{}", executeCodeResponse);
        return executeCodeResponse;
    }
}
