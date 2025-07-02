package com.example.springai.neww.controller;

import com.example.springai.neww.agent.AgentRequest;
import com.example.springai.neww.agent.AgentResponse;
import com.example.springai.neww.agent.AgentService;
import com.example.springai.neww.tools.Tool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/agent")
@RequiredArgsConstructor
@Slf4j
public class AgentController {
    
    private final AgentService agentService;
    
    /**
     * 处理用户消息
     */
    @PostMapping("/chat")
    public ResponseEntity<AgentResponse> chat(@RequestBody AgentRequest request) {
        log.info("收到用户消息: {}", request.getMessage());
        AgentResponse response = agentService.processInput(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 便捷的消息接口（仅接受文本消息）
     */
    @PostMapping("/message")
    public ResponseEntity<AgentResponse> message(@RequestBody Map<String, String> payload) {
        String message = payload.get("message");
        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(AgentResponse.builder()
                    .success(false)
                    .message("消息不能为空")
                    .build());
        }
        
        AgentRequest request = AgentRequest.builder()
            .message(message)
            .build();
            
        AgentResponse response = agentService.processInput(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取所有可用工具
     */
    @GetMapping("/tools")
    public ResponseEntity<List<Tool>> getTools() {
        List<Tool> tools = agentService.getAvailableTools();
        return ResponseEntity.ok(tools);
    }
    
    /**
     * 获取工具信息
     */
    @GetMapping("/tools/{toolName}")
    public ResponseEntity<Map<String, Object>> getToolInfo(@PathVariable String toolName) {
        List<Tool> tools = agentService.getAvailableTools();
        
        Tool tool = tools.stream()
            .filter(t -> t.getName().equals(toolName))
            .findFirst()
            .orElse(null);
            
        if (tool == null) {
            return ResponseEntity.notFound().build();
        }
        
        Map<String, Object> toolInfo = Map.of(
            "name", tool.getName(),
            "description", tool.getDescription(),
            "parameters", tool.getParameterDescriptions()
        );
        
        return ResponseEntity.ok(toolInfo);
    }
    
    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = Map.of(
            "status", "UP",
            "timestamp", System.currentTimeMillis(),
            "availableTools", agentService.getAvailableTools().size()
        );
        return ResponseEntity.ok(health);
    }
} 