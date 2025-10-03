package com.yupi.xwoj.judge.codsandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteCodeRequest {
    //输入用例列表
    private List<String> inputList;
    //提交的代码
    private String code;
    //题目用的编程语言
    private String language;
}
