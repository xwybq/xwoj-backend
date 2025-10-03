package com.yupi.xwoj.judge.codsandbox;

import com.yupi.xwoj.judge.codsandbox.impl.ExampleCodeSandbox;
import com.yupi.xwoj.judge.codsandbox.model.ExecuteCodeRequest;
import com.yupi.xwoj.judge.codsandbox.model.ExecuteCodeResponse;
import com.yupi.xwoj.model.entity.Question;
import com.yupi.xwoj.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CodeSandboxTest {


    @Value("${codesandbox.type:example}")
    private String type;

    @Test
    void excuteCode() {

        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code("int main() { return 0; }")
                .language(QuestionSubmitLanguageEnum.CPLUSPLUS.getValue())
                .inputList(List.of("1 2", "3 4"))
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
//            Assertions.assertNotNull(executeCodeResponse);
    }


    @Test
    void excuteCodeByValue() {
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code("int main() { return 0; }")
                .language(QuestionSubmitLanguageEnum.CPLUSPLUS.getValue())
                .inputList(List.of("1 2", "3 4"))
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
//            Assertions.assertNotNull(executeCodeResponse);
    }


    @Test
    void excuteCodeByProxy() {
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        codeSandbox = new CodeSandboxPoxy(codeSandbox);
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code("int main() { return 0; }")
                .language(QuestionSubmitLanguageEnum.CPLUSPLUS.getValue())
                .inputList(List.of("1 2", "3 4"))
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
//            Assertions.assertNotNull(executeCodeResponse);
    }

}