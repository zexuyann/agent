package com.example.springai.neww.tools.mcp;

import com.example.springai.neww.tools.Tool;
import com.example.springai.neww.tools.ToolResult;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Slf4j
public class FileTool implements Tool {
    
    @Override
    public String getName() {
        return "file";
    }
    
    @Override
    public String getDescription() {
        return "文件操作工具，支持读取、写入、列出目录等操作（模拟MCP工具）";
    }
    
    @Override
    public Map<String, String> getParameterDescriptions() {
        return Map.of(
            "operation", "操作类型: read, write, list, exists",
            "path", "文件或目录路径",
            "content", "写入内容（仅当operation为write时需要）"
        );
    }
    
    @Override
    public ToolResult execute(Map<String, Object> parameters) {
        try {
            String operation = (String) parameters.get("operation");
            String pathStr = (String) parameters.get("path");
            
            if (operation == null || pathStr == null) {
                return ToolResult.failure("缺少必要参数：operation 和 path");
            }
            
            Path path = Paths.get(pathStr);
            
            return switch (operation.toLowerCase()) {
                case "read" -> readFile(path);
                case "write" -> writeFile(path, (String) parameters.get("content"));
                case "list" -> listDirectory(path);
                case "exists" -> checkExists(path);
                default -> ToolResult.failure("不支持的操作: " + operation);
            };
            
        } catch (Exception e) {
            log.error("文件操作失败", e);
            return ToolResult.failure("文件操作失败: " + e.getMessage());
        }
    }
    
    private ToolResult readFile(Path path) {
        try {
            if (!Files.exists(path)) {
                return ToolResult.failure("文件不存在: " + path);
            }
            
            String content = Files.readString(path);
            Map<String, Object> result = Map.of(
                "path", path.toString(),
                "content", content,
                "size", Files.size(path),
                "operation", "read"
            );
            
            log.info("读取文件: {}", path);
            return ToolResult.success("文件读取成功", result);
            
        } catch (IOException e) {
            return ToolResult.failure("读取文件失败: " + e.getMessage());
        }
    }
    
    private ToolResult writeFile(Path path, String content) {
        try {
            if (content == null) {
                return ToolResult.failure("缺少写入内容");
            }
            
            // 为了安全，限制写入到临时目录
            if (!path.toString().startsWith(System.getProperty("java.io.tmpdir"))) {
                return ToolResult.failure("出于安全考虑，只能写入临时目录");
            }
            
            Files.writeString(path, content);
            Map<String, Object> result = Map.of(
                "path", path.toString(),
                "size", Files.size(path),
                "operation", "write"
            );
            
            log.info("写入文件: {}", path);
            return ToolResult.success("文件写入成功", result);
            
        } catch (IOException e) {
            return ToolResult.failure("写入文件失败: " + e.getMessage());
        }
    }
    
    private ToolResult listDirectory(Path path) {
        try {
            if (!Files.exists(path)) {
                return ToolResult.failure("目录不存在: " + path);
            }
            
            if (!Files.isDirectory(path)) {
                return ToolResult.failure("路径不是目录: " + path);
            }
            
            var files = Files.list(path)
                .map(p -> Map.of(
                    "name", p.getFileName().toString(),
                    "path", p.toString(),
                    "isDirectory", Files.isDirectory(p)
                ))
                .toList();
            
            Map<String, Object> result = Map.of(
                "path", path.toString(),
                "files", files,
                "count", files.size(),
                "operation", "list"
            );
            
            log.info("列出目录: {}", path);
            return ToolResult.success("目录列表获取成功", result);
            
        } catch (IOException e) {
            return ToolResult.failure("列出目录失败: " + e.getMessage());
        }
    }
    
    private ToolResult checkExists(Path path) {
        boolean exists = Files.exists(path);
        Map<String, Object> result = Map.of(
            "path", path.toString(),
            "exists", exists,
            "isDirectory", exists && Files.isDirectory(path),
            "isFile", exists && Files.isRegularFile(path),
            "operation", "exists"
        );
        
        log.info("检查文件存在: {} = {}", path, exists);
        return ToolResult.success("文件存在性检查完成", result);
    }
}