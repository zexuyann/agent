package com.example.springai.old.agent;

import com.example.springai.old.agent.memory.ConversationMemory;
import com.example.springai.old.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class SimpleAgent implements Agent {

    private final ChatModel chatModel;
    private final List<Tool> tools;
    private final ConversationMemory memory;

    public SimpleAgent(ChatModel chatModel, List<Tool> tools, ConversationMemory memory) {
        this.chatModel = chatModel;
        this.tools = tools;
        this.memory = memory;
        log.info("Agent初始化完成，可用工具数量: {}", tools.size());
    }

    @Override
    public String execute(String input) {
        return executeWithContext(input, "default");
    }

    @Override
    public String executeWithContext(String input, String sessionId) {
        log.info("Agent执行任务，SessionId: {}, Input: {}", sessionId, input);
        
        // 添加用户消息到记忆
        memory.addMessage(sessionId, "user", input);
        
        // 构建系统提示词
        String systemPrompt = buildSystemPrompt();
        
        // 获取历史对话上下文
        String context = memory.getContext(sessionId);
        
        // 构建完整的提示词
        String fullPrompt = systemPrompt + "\n\n历史对话:\n" + context + "\n用户: " + input;
        
        try {
            // 第一次LLM调用：分析是否需要工具
            ChatResponse response = chatModel.call(new Prompt(fullPrompt));
            String aiResponse = response.getResult().getOutput().getText();
            
            log.info("AI初始响应: {}", aiResponse);
            
            // 检查是否需要使用工具
            String toolCall = extractToolCall(aiResponse);
            
            if (toolCall != null) {
                // 执行工具调用
                String toolResult = executeToolCall(toolCall);
                
                // 第二次LLM调用：基于工具结果生成最终回答
                String finalPrompt = fullPrompt + "\n助手: " + aiResponse + "\n工具执行结果: " + toolResult + "\n请基于工具结果给出最终回答:";
                ChatResponse finalResponse = chatModel.call(new Prompt(finalPrompt));
                String finalAnswer = finalResponse.getResult().getOutput().getText();
                
                // 添加助手消息到记忆
                memory.addMessage(sessionId, "assistant", finalAnswer);
                
                return finalAnswer;
            } else {
                // 不需要工具，直接返回回答
                memory.addMessage(sessionId, "assistant", aiResponse);
                return aiResponse;
            }
            
        } catch (Exception e) {
            log.error("Agent执行出错: ", e);
            String errorMsg = "抱歉，处理您的请求时出现了错误。";
            memory.addMessage(sessionId, "assistant", errorMsg);
            return errorMsg;
        }
    }

    private String buildSystemPrompt() {
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一个智能助手，可以使用以下工具来帮助用户:\n\n");
        
        for (Tool tool : tools) {
            prompt.append("工具名称: ").append(tool.getName()).append("\n");
            prompt.append("工具描述: ").append(tool.getDescription()).append("\n");
            prompt.append("参数格式: ").append(tool.getParameterSchema()).append("\n\n");
        }
        
        prompt.append("当你需要使用工具时，请按照以下格式回复:\n");
        prompt.append("TOOL_CALL: 工具名称|参数值\n");
        prompt.append("例如: TOOL_CALL: weather_query|北京\n");
        prompt.append("例如: TOOL_CALL: calculator|2+3*4\n\n");
        prompt.append("如果不需要使用工具，请直接回答用户的问题。\n");
        
        return prompt.toString();
    }

    private String extractToolCall(String response) {
        Pattern pattern = Pattern.compile("TOOL_CALL:\\s*(\\w+)\\|(.+)");
        Matcher matcher = pattern.matcher(response);
        
        if (matcher.find()) {
            return matcher.group(1) + "|" + matcher.group(2);
        }
        
        return null;
    }

    private String executeToolCall(String toolCall) {
        String[] parts = toolCall.split("\\|", 2);
        if (parts.length != 2) {
            return "工具调用格式错误";
        }
        
        String toolName = parts[0].trim();
        String parameter = parts[1].trim();
        
        log.info("执行工具调用: {} with parameter: {}", toolName, parameter);
        
        for (Tool tool : tools) {
            if (tool.getName().equals(toolName)) {
                return tool.execute(parameter);
            }
        }
        
        return "未找到工具: " + toolName;
    }
}