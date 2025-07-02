package com.example.springai.neww.tools;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolResult {
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 结果消息
     */
    private String message;
    
    /**
     * 结果数据
     */
    private Object data;
    
    /**
     * 错误信息
     */
    private String error;
    
    /**
     * 创建成功结果
     */
    public static ToolResult success(String message, Object data) {
        return ToolResult.builder()
            .success(true)
            .message(message)
            .data(data)
            .build();
    }
    
    /**
     * 创建成功结果（仅消息）
     */
    public static ToolResult success(String message) {
        return success(message, null);
    }
    
    /**
     * 创建失败结果
     */
    public static ToolResult failure(String error) {
        return ToolResult.builder()
            .success(false)
            .error(error)
            .message("执行失败: " + error)
            .build();
    }
} 