package com.example.springai.neww.tools;

import com.example.springai.neww.tools.mcp.WeatherTool;
import com.example.springai.neww.tools.mcp.FileTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.*;

@Component
@Slf4j
public class McpToolRegistry {
    
    private final Map<String, Tool> tools = new HashMap<>();
    
    @PostConstruct
    public void initializeTools() {
        // 注册MCP工具
        registerTool(new WeatherTool());
        registerTool(new FileTool());
        
        log.info("已注册 {} 个MCP工具", tools.size());
    }
    
    /**
     * 注册工具
     */
    public void registerTool(Tool tool) {
        tools.put(tool.getName(), tool);
        log.debug("注册MCP工具: {}", tool.getName());
    }
    
    /**
     * 获取工具
     */
    public Optional<Tool> getTool(String name) {
        return Optional.ofNullable(tools.get(name));
    }
    
    /**
     * 获取所有工具
     */
    public List<Tool> getAllTools() {
        return new ArrayList<>(tools.values());
    }
    
    /**
     * 工具是否存在
     */
    public boolean hasTool(String name) {
        return tools.containsKey(name);
    }
    
    /**
     * 从外部MCP服务器加载工具
     */
    public void loadFromMcpServer(String serverUrl) {
        // TODO: 实现从MCP服务器加载工具的逻辑
        log.info("TODO: 从MCP服务器加载工具: {}", serverUrl);
    }
} 