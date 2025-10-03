package com.yupi.xwoj.judge.codsandbox;

import com.yupi.xwoj.judge.codsandbox.impl.ExampleCodeSandbox;
import com.yupi.xwoj.judge.codsandbox.impl.RemoteCodeSandbox;
import com.yupi.xwoj.judge.codsandbox.impl.ThirdPartyCodeSandbox;

/**
 * 代码沙箱工厂
 */
public class CodeSandboxFactory {
    /**
     * 根据类型创建代码沙箱实例
     *
     * @param type 代码沙箱类型
     * @return 代码沙箱实例
     */
    public static CodeSandbox newInstance(String type) {
        if (type == null || type.isEmpty()) {
            return new ExampleCodeSandbox();
        }
        return switch (type) {
            case "example" -> new ExampleCodeSandbox();
            case "thirdParty" -> new ThirdPartyCodeSandbox();
            case "remote" -> new RemoteCodeSandbox();
            default -> new ExampleCodeSandbox();
        };
    }
}
