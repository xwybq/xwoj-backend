package com.yupi.xwoj.judge.codsandbox.model;

import com.yupi.xwoj.model.dto.questionsubmit.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteCodeResponse {

    //输出用例列表
    private List<String> outputList;
    //接口信息
    private String message;
    //判题信息
    private JudgeInfo judgeInfo;
    //判题状态
    private Integer status;
}
