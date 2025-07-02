package com.example.springai.old.controller;

import com.example.springai.old.agent.Agent;
import com.example.springai.old.agent.memory.ConversationMemory;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/agent")
public class AgentController {

    private final Agent agent;
    private final ConversationMemory memory;

    public AgentController(Agent agent, ConversationMemory memory) {
        this.agent = agent;
        this.memory = memory;
    }

    @Data
    public static class AgentRequest {
        private String message;
        private String sessionId = "default";
    }

    @Data
    public static class AgentResponse {
        private boolean success;
        private String response;
        private String sessionId;
        private long timestamp;

        public static AgentResponse success(String response, String sessionId) {
            AgentResponse agentResponse = new AgentResponse();
            agentResponse.success = true;
            agentResponse.response = response;
            agentResponse.sessionId = sessionId;
            agentResponse.timestamp = System.currentTimeMillis();
            return agentResponse;
        }

        public static AgentResponse error(String message) {
            AgentResponse agentResponse = new AgentResponse();
            agentResponse.success = false;
            agentResponse.response = message;
            agentResponse.timestamp = System.currentTimeMillis();
            return agentResponse;
        }
    }

    @PostMapping("/chat")
    public AgentResponse chat(@RequestBody AgentRequest request) {
        try {
            log.info("Agent收到请求: sessionId={}, message={}", request.getSessionId(), request.getMessage());
            
            String response = agent.executeWithContext(request.getMessage(), request.getSessionId());
            
            return AgentResponse.success(response, request.getSessionId());
        } catch (Exception e) {
            log.error("Agent处理请求失败: ", e);
            return AgentResponse.error("处理请求时发生错误: " + e.getMessage());
        }
    }

    @PostMapping("/clear/{sessionId}")
    public AgentResponse clearSession(@PathVariable String sessionId) {
        memory.clearSession(sessionId);
        return AgentResponse.success("会话已清空", sessionId);
    }

    @GetMapping("/history/{sessionId}")
    public String getHistory(@PathVariable String sessionId) {
        return memory.getContext(sessionId);
    }
}