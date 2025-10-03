package com.yupi.xwoj.model.dto.question;

import lombok.Data;

/**
 * 判题用例
 */
@Data
public class JudgeCase {

    /**
     * 输入用例，例如 "1 2"
     */
    private  String input;

     /**
     * 输出用例，例如 "3"
     */
    private String output;
}
