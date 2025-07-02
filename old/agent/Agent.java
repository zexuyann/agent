package com.example.springai.old.agent;

public interface Agent {
    String execute(String input);
    String executeWithContext(String input, String sessionId);
}