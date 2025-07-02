package com.example.springai.neww.tools.builtin;

import com.example.springai.neww.tools.Tool;
import com.example.springai.neww.tools.ToolResult;
import lombok.extern.slf4j.Slf4j;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Map;

@Slf4j
public class CalculatorTool implements Tool {
    
    private final ScriptEngine scriptEngine;
    
    public CalculatorTool() {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        if (engine == null) {
            // 如果nashorn不可用，尝试使用JavaScript引擎
            engine = new ScriptEngineManager().getEngineByName("JavaScript");
        }
        this.scriptEngine = engine;
    }
    
    @Override
    public String getName() {
        return "calculator";
    }
    
    @Override
    public String getDescription() {
        return "执行数学计算，支持基本的算术运算";
    }
    
    @Override
    public Map<String, String> getParameterDescriptions() {
        return Map.of("expression", "要计算的数学表达式，如: 2+3*4");
    }
    
    @Override
    public ToolResult execute(Map<String, Object> parameters) {
        try {
            String expression = (String) parameters.get("expression");
            if (expression == null || expression.trim().isEmpty()) {
                return ToolResult.failure("缺少计算表达式");
            }
            
            // 安全检查：只允许数字、基本运算符和括号
            if (!expression.matches("[0-9+\\-*/().\\s]+")) {
                return ToolResult.failure("表达式包含不支持的字符");
            }
            
            Object result;
            if (scriptEngine != null) {
                result = scriptEngine.eval(expression);
            } else {
                // 简单的计算逻辑作为备选方案
                result = simpleCalculate(expression);
            }
            
            log.info("计算表达式: {} = {}", expression, result);
            return ToolResult.success("计算结果: " + result, result);
            
        } catch (Exception e) {
            log.error("计算失败", e);
            return ToolResult.failure("计算失败: " + e.getMessage());
        }
    }
    
    /**
     * 简单的计算实现（备选方案）
     */
    private double simpleCalculate(String expression) {
        // 这里实现一个简单的计算器逻辑
        // 为了简化，这里只处理基本的加减乘除
        expression = expression.replaceAll("\\s", "");
        
        // 简单处理：只支持两个数字的运算
        if (expression.contains("+")) {
            String[] parts = expression.split("\\+");
            return Double.parseDouble(parts[0]) + Double.parseDouble(parts[1]);
        } else if (expression.contains("-")) {
            String[] parts = expression.split("-");
            return Double.parseDouble(parts[0]) - Double.parseDouble(parts[1]);
        } else if (expression.contains("*")) {
            String[] parts = expression.split("\\*");
            return Double.parseDouble(parts[0]) * Double.parseDouble(parts[1]);
        } else if (expression.contains("/")) {
            String[] parts = expression.split("/");
            return Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]);
        } else {
            return Double.parseDouble(expression);
        }
    }
} 