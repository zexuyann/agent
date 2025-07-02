package com.example.springai.neww.agent;

import com.example.springai.neww.tools.BuiltinToolRegistry;
import com.example.springai.neww.tools.McpToolRegistry;
import com.example.springai.neww.tools.Tool;
import com.example.springai.neww.tools.ToolResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgentService {
    
    private final BuiltinToolRegistry builtinToolRegistry;
    private final McpToolRegistry mcpToolRegistry;
    
    /**
     * 处理用户输入并执行相应的工具
     */
    public AgentResponse processInput(AgentRequest request) {
        log.info("处理用户请求: {}", request.getMessage());
        
        try {
            // 1. 解析用户意图和参数
            ToolInvocation toolInvocation = parseUserIntent(request);
            
            if (toolInvocation == null) {
                return AgentResponse.builder()
                    .success(false)
                    .message("无法理解您的请求，请重新描述")
                    .build();
            }
            
            // 2. 查找并执行工具
            ToolResult result = executeTool(toolInvocation);
            
            // 3. 构建响应
            return AgentResponse.builder()
                .success(result.isSuccess())
                .message(result.getMessage())
                .data(result.getData())
                .toolUsed(toolInvocation.getToolName())
                .build();
                
        } catch (Exception e) {
            log.error("处理请求时发生错误", e);
            return AgentResponse.builder()
                .success(false)
                .message("处理请求时发生错误: " + e.getMessage())
                .build();
        }
    }
    
    /**
     * 获取所有可用工具列表
     */
    public List<Tool> getAvailableTools() {
        List<Tool> tools = builtinToolRegistry.getAllTools();
        tools.addAll(mcpToolRegistry.getAllTools());
        return tools;
    }
    
    /**
     * 解析用户意图，提取工具名称和参数
     */
    private ToolInvocation parseUserIntent(AgentRequest request) {
        String message = request.getMessage().toLowerCase().trim();
        
        // 简单的意图识别逻辑，可以后续使用AI模型进行改进
        if (message.contains("天气") || message.contains("weather")) {
            return ToolInvocation.builder()
                .toolName("weather")
                .parameters(Map.of("location", extractLocation(message)))
                .build();
        }
        
        if (message.contains("计算") || message.contains("calculate")) {
            return ToolInvocation.builder()
                .toolName("calculator")
                .parameters(extractCalculationParams(message))
                .build();
        }
        
        if (message.contains("时间") || message.contains("time")) {
            return ToolInvocation.builder()
                .toolName("datetime")
                .parameters(Map.of())
                .build();
        }
        
        // 检查是否直接指定了工具名称
        for (Tool tool : getAvailableTools()) {
            if (message.contains(tool.getName().toLowerCase())) {
                return ToolInvocation.builder()
                    .toolName(tool.getName())
                    .parameters(request.getParameters() != null ? request.getParameters() : Map.of())
                    .build();
            }
        }
        
        return null;
    }
    
    /**
     * 执行工具调用
     */
    private ToolResult executeTool(ToolInvocation invocation) {
        // 首先尝试内置工具
        Optional<Tool> builtinTool = builtinToolRegistry.getTool(invocation.getToolName());
        if (builtinTool.isPresent()) {
            log.info("使用内置工具: {}", invocation.getToolName());
            return builtinTool.get().execute(invocation.getParameters());
        }
        
        // 然后尝试MCP工具
        Optional<Tool> mcpTool = mcpToolRegistry.getTool(invocation.getToolName());
        if (mcpTool.isPresent()) {
            log.info("使用MCP工具: {}", invocation.getToolName());
            return mcpTool.get().execute(invocation.getParameters());
        }
        
        return ToolResult.failure("未找到工具: " + invocation.getToolName());
    }
    
    /**
     * 从消息中提取地理位置
     */
    private String extractLocation(String message) {
        // 简单的位置提取逻辑
        String[] words = message.split("\\s+");
        for (String word : words) {
            if (word.length() > 1 && !word.equals("天气") && !word.equals("weather")) {
                return word;
            }
        }
        return "北京"; // 默认位置
    }
    
    /**
     * 从消息中提取计算参数
     */
    private Map<String, Object> extractCalculationParams(String message) {
        // 简单的计算参数提取逻辑
        return Map.of("expression", message.replaceAll("计算|calculate", "").trim());
    }
} 