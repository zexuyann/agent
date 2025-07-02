package com.example.springai.neww.tools;

import com.example.springai.neww.tools.builtin.CalculatorTool;
import com.example.springai.neww.tools.builtin.DateTimeTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.*;

@Component
@Slf4j
public class BuiltinToolRegistry {
    
    private final Map<String, Tool> tools = new HashMap<>();
    
    @PostConstruct
    public void initializeTools() {
        // 注册内置工具
        registerTool(new CalculatorTool());
        registerTool(new DateTimeTool());
        
        log.info("已注册 {} 个内置工具", tools.size());
    }
    
    /**
     * 注册工具
     */
    public void registerTool(Tool tool) {
        tools.put(tool.getName(), tool);
        log.debug("注册内置工具: {}", tool.getName());
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
} 