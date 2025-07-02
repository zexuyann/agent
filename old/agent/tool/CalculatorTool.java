package com.example.springai.old.agent.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CalculatorTool implements Tool {

    @Override
    public String getName() {
        return "calculator";
    }

    @Override
    public String getDescription() {
        return "进行数学计算，支持基本的四则运算";
    }

    @Override
    public String execute(String expression) {
        log.info("计算表达式: {}", expression);
        
        try {
            // 简单的计算器实现（实际应用中应该使用更安全的表达式解析器）
            String cleanExpression = expression.replaceAll("[^0-9+\\-*/().\\s]", "");
            
            // 使用JavaScript引擎计算（仅示例，生产环境建议使用专门的数学库）
            javax.script.ScriptEngineManager manager = new javax.script.ScriptEngineManager();
            javax.script.ScriptEngine engine = manager.getEngineByName("JavaScript");
            Object result = engine.eval(cleanExpression);
            
            return "计算结果: " + cleanExpression + " = " + result;
        } catch (Exception e) {
            log.error("计算错误: ", e);
            return "计算错误: " + e.getMessage();
        }
    }

    @Override
    public String getParameterSchema() {
        return """
        {
            "type": "object",
            "properties": {
                "expression": {
                    "type": "string",
                    "description": "要计算的数学表达式，如 '2+3*4'"
                }
            },
            "required": ["expression"]
        }
        """;
    }
}