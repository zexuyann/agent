package com.example.springai.old.agent.tool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WeatherTool implements Tool {

    @Override
    public String getName() {
        return "weather_query";
    }

    @Override
    public String getDescription() {
        return "查询指定城市的天气信息";
    }

    @Override
    public String execute(String city) {
        log.info("查询城市天气: {}", city);
        
        // 模拟天气查询（实际应用中应该调用真实的天气API）
        String[] weathers = {"晴天", "多云", "小雨", "大雨", "雪"};
        String[] temps = {"20°C", "15°C", "10°C", "25°C", "5°C"};
        
        int index = Math.abs(city.hashCode()) % weathers.length;
        String weather = weathers[index];
        String temp = temps[index];
        
        return String.format("城市：%s，天气：%s，温度：%s", city, weather, temp);
    }

    @Override
    public String getParameterSchema() {
        return """
        {
            "type": "object",
            "properties": {
                "city": {
                    "type": "string",
                    "description": "要查询天气的城市名称"
                }
            },
            "required": ["city"]
        }
        """;
    }
}