package com.example.springai.old.agent.memory;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConversationMemory {

    private final Map<String, List<Message>> conversations = new ConcurrentHashMap<>();
    private final int maxMessages = 10; // 每个会话最多保存10条消息

    @Data
    public static class Message {
        private String role; // "user", "assistant", "system"
        private String content;
        private long timestamp;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
            this.timestamp = System.currentTimeMillis();
        }
    }

    public void addMessage(String sessionId, String role, String content) {
        List<Message> messages = conversations.computeIfAbsent(sessionId, k -> new ArrayList<>());
        messages.add(new Message(role, content));
        
        // 保持消息数量在限制内
        if (messages.size() > maxMessages) {
            messages.remove(0);
        }
    }

    public List<Message> getMessages(String sessionId) {
        return conversations.getOrDefault(sessionId, new ArrayList<>());
    }

    public String getContext(String sessionId) {
        List<Message> messages = getMessages(sessionId);
        StringBuilder context = new StringBuilder();
        
        for (Message message : messages) {
            context.append(message.getRole()).append(": ").append(message.getContent()).append("\n");
        }
        
        return context.toString();
    }

    public void clearSession(String sessionId) {
        conversations.remove(sessionId);
    }
}