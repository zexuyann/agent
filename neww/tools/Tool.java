package com.example.springai.neww.tools;

import java.util.Map;

/**
 * 工具接口，所有工具都需要实现此接口
 */
public interface Tool {
    
    /**
     * 获取工具名称
     */
    String getName();
    
    /**
     * 获取工具描述
     */
    String getDescription();
    
    /**
     * 获取工具参数说明
     */
    Map<String, String> getParameterDescriptions();
    
    /**
     * 执行工具
     */
    ToolResult execute(Map<String, Object> parameters);
    
    /**
     * 检查参数是否有效
     */
    default boolean validateParameters(Map<String, Object> parameters) {
        return true;
    }
} 