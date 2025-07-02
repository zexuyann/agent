package com.example.springai.neww.tools.mcp;

import com.example.springai.neww.tools.Tool;
import com.example.springai.neww.tools.ToolResult;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Random;

@Slf4j
public class WeatherTool implements Tool {
    
    private final Random random = new Random();
    
    @Override
    public String getName() {
        return "weather";
    }
    
    @Override
    public String getDescription() {
        return "获取指定地点的天气信息（模拟MCP工具）";
    }
    
    @Override
    public Map<String, String> getParameterDescriptions() {
        return Map.of("location", "地点名称，如：北京、上海、纽约等");
    }
    
    @Override
    public ToolResult execute(Map<String, Object> parameters) {
        try {
            String location = (String) parameters.get("location");
            if (location == null || location.trim().isEmpty()) {
                return ToolResult.failure("缺少地点参数");
            }
            
            // 模拟天气数据（实际应用中这里会调用真实的天气API）
            Map<String, Object> weatherData = generateMockWeatherData(location);
            
            String weatherInfo = String.format("%s 当前天气：%s，温度 %d°C，湿度 %d%%",
                location,
                weatherData.get("condition"),
                weatherData.get("temperature"),
                weatherData.get("humidity")
            );
            
            log.info("获取天气信息: {}", location);
            return ToolResult.success(weatherInfo, weatherData);
            
        } catch (Exception e) {
            log.error("获取天气信息失败", e);
            return ToolResult.failure("获取天气信息失败: " + e.getMessage());
        }
    }
    
    /**
     * 生成模拟天气数据
     */
    private Map<String, Object> generateMockWeatherData(String location) {
        String[] conditions = {"晴朗", "多云", "阴天", "小雨", "大雨", "雷阵雨"};
        int temperature = random.nextInt(40) - 10; // -10 到 30 度
        int humidity = random.nextInt(100); // 0 到 100%
        String condition = conditions[random.nextInt(conditions.length)];
        
        return Map.of(
            "location", location,
            "condition", condition,
            "temperature", temperature,
            "humidity", humidity,
            "wind_speed", random.nextInt(20) + "km/h",
            "pressure", random.nextInt(100) + 1000 + "hPa",
            "source", "模拟MCP天气服务"
        );
    }
} 