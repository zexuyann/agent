package com.example.springai.neww.agent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentResponse {
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private Object data;
    
    /**
     * 使用的工具名称
     */
    private String toolUsed;
    
    /**
     * 响应时间
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
} 