package com.example.springai.old.agent.tool;

public interface Tool {
    String getName();
    String getDescription();
    String execute(String input);
    String getParameterSchema(); // JSON Schema for parameters
}