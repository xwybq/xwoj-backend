package com.yupi.xwoj.model.dto.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 判题配置
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JudgeConfig {




    /**
     * 时间限制，例如 1000ms
     */
    private Long timeLimit;

    /**
     * 内存限制，例如 1024KB
     */
    private Long memoryLimit;

    /**
     * 栈限制，例如 1024KB
     */
    private Long stackLimit;
}
