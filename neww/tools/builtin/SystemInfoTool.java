package com.example.springai.neww.tools.builtin;

import com.example.springai.neww.tools.Tool;
import com.example.springai.neww.tools.ToolResult;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class SystemInfoTool implements Tool {
    
    @Override
    public String getName() {
        return "systeminfo";
    }
    
    @Override
    public String getDescription() {
        return "获取系统信息，包括Java版本、操作系统、内存使用情况等";
    }
    
    @Override
    public Map<String, String> getParameterDescriptions() {
        return Map.of("type", "信息类型 (可选): all, java, os, memory");
    }
    
    @Override
    public ToolResult execute(Map<String, Object> parameters) {
        try {
            String type = (String) parameters.getOrDefault("type", "all");
            
            Runtime runtime = Runtime.getRuntime();
            Map<String, Object> systemInfo = Map.of(
                "java_version", System.getProperty("java.version"),
                "java_vendor", System.getProperty("java.vendor"),
                "os_name", System.getProperty("os.name"),
                "os_version", System.getProperty("os.version"),
                "os_arch", System.getProperty("os.arch"),
                "available_processors", runtime.availableProcessors(),
                "total_memory_mb", runtime.totalMemory() / 1024 / 1024,
                "free_memory_mb", runtime.freeMemory() / 1024 / 1024,
                "max_memory_mb", runtime.maxMemory() / 1024 / 1024,
                "used_memory_mb", (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024
            );
            
            Object filteredInfo = switch (type.toLowerCase()) {
                case "java" -> Map.of(
                    "java_version", systemInfo.get("java_version"),
                    "java_vendor", systemInfo.get("java_vendor")
                );
                case "os" -> Map.of(
                    "os_name", systemInfo.get("os_name"),
                    "os_version", systemInfo.get("os_version"),
                    "os_arch", systemInfo.get("os_arch")
                );
                case "memory" -> Map.of(
                    "total_memory_mb", systemInfo.get("total_memory_mb"),
                    "free_memory_mb", systemInfo.get("free_memory_mb"),
                    "max_memory_mb", systemInfo.get("max_memory_mb"),
                    "used_memory_mb", systemInfo.get("used_memory_mb")
                );
                default -> systemInfo;
            };
            
            log.info("获取系统信息，类型: {}", type);
            return ToolResult.success("系统信息获取成功", filteredInfo);
            
        } catch (Exception e) {
            log.error("获取系统信息失败", e);
            return ToolResult.failure("获取系统信息失败: " + e.getMessage());
        }
    }
}