package com.example.springai.neww.tools.builtin;

import com.example.springai.neww.tools.Tool;
import com.example.springai.neww.tools.ToolResult;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
public class DateTimeTool implements Tool {
    
    @Override
    public String getName() {
        return "datetime";
    }
    
    @Override
    public String getDescription() {
        return "获取当前日期时间信息";
    }
    
    @Override
    public Map<String, String> getParameterDescriptions() {
        return Map.of(
            "format", "日期时间格式 (可选)，默认为 yyyy-MM-dd HH:mm:ss",
            "timezone", "时区 (可选)，默认为系统时区"
        );
    }
    
    @Override
    public ToolResult execute(Map<String, Object> parameters) {
        try {
            String format = (String) parameters.getOrDefault("format", "yyyy-MM-dd HH:mm:ss");
            String timezone = (String) parameters.getOrDefault("timezone", ZoneId.systemDefault().getId());
            
            ZoneId zoneId = ZoneId.of(timezone);
            LocalDateTime now = LocalDateTime.now(zoneId);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            String formattedTime = now.format(formatter);
            
            Map<String, Object> result = Map.of(
                "current_time", formattedTime,
                "timestamp", System.currentTimeMillis(),
                "timezone", timezone,
                "format", format
            );
            
            log.info("获取当前时间: {}", formattedTime);
            return ToolResult.success("当前时间: " + formattedTime, result);
            
        } catch (Exception e) {
            log.error("获取时间失败", e);
            return ToolResult.failure("获取时间失败: " + e.getMessage());
        }
    }
} 