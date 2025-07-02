package com.example.springai.neww.agent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentRequest {
    /**
     * 用户输入的消息
     */
    private String message;
    
    /**
     * 额外参数
     */
    private Map<String, Object> parameters;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 会话ID
     */
    private String sessionId;
} 