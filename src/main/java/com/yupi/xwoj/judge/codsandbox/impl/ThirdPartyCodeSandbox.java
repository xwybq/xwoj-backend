package com.yupi.xwoj.judge.codsandbox.impl;

import com.yupi.xwoj.judge.codsandbox.CodeSandbox;
import com.yupi.xwoj.judge.codsandbox.model.ExecuteCodeRequest;
import com.yupi.xwoj.judge.codsandbox.model.ExecuteCodeResponse;

/**
 * 第三方代码沙箱(网上现成的)
 */
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
