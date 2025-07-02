# 智能Agent系统

这是一个基于Spring Boot的智能Agent系统，能够调用内置工具和MCP工具来执行各种任务。

## 🏗️ 架构设计

```
src/main/java/com/example/springai/neww/
├── agent/                  # Agent核心
│   ├── AgentService.java      # 主服务类
│   ├── AgentRequest.java      # 请求模型
│   ├── AgentResponse.java     # 响应模型
│   └── ToolInvocation.java    # 工具调用模型
├── tools/                  # 工具系统
│   ├── Tool.java              # 工具接口
│   ├── ToolResult.java        # 工具执行结果
│   ├── BuiltinToolRegistry.java # 内置工具注册表
│   ├── McpToolRegistry.java   # MCP工具注册表
│   ├── builtin/               # 内置工具
│   │   ├── CalculatorTool.java    # 计算器工具
│   │   ├── DateTimeTool.java      # 日期时间工具
│   │   └── SystemInfoTool.java    # 系统信息工具
│   └── mcp/                   # MCP工具
│       ├── WeatherTool.java       # 天气工具
│       └── FileTool.java          # 文件操作工具
├── controller/             # REST控制器
│   └── AgentController.java   # Agent API控制器
└── config/                 # 配置
    └── AgentConfig.java       # Agent配置
```

## 🚀 快速开始

### 1. 启动应用

```bash
mvn spring-boot:run
```

### 2. 访问演示页面

打开浏览器访问：`http://localhost:8080`

### 3. 使用API

#### 发送消息给Agent
```bash
curl -X POST http://localhost:8080/api/agent/message \
  -H "Content-Type: application/json" \
  -d '{"message": "计算 2+3*4"}'
```

#### 获取可用工具列表
```bash
curl http://localhost:8080/api/agent/tools
```

#### 健康检查
```bash
curl http://localhost:8080/api/agent/health
```

## 🔧 可用工具

### 内置工具

1. **计算器工具 (calculator)**
   - 功能：执行数学计算
   - 参数：expression（数学表达式）
   - 示例：`"计算 2+3*4"` 或 `"calculate 10/2"`

2. **日期时间工具 (datetime)**
   - 功能：获取当前日期时间
   - 参数：format（可选）、timezone（可选）
   - 示例：`"现在几点了"` 或 `"当前时间"`

3. **系统信息工具 (systeminfo)**
   - 功能：获取系统信息
   - 参数：type（可选：all、java、os、memory）
   - 示例：`"获取系统信息"` 或 `"查看内存使用情况"`

### MCP工具

1. **天气工具 (weather)**
   - 功能：获取天气信息（模拟）
   - 参数：location（地点名称）
   - 示例：`"北京天气怎么样"` 或 `"上海的天气"`

2. **文件工具 (file)**
   - 功能：文件操作（读取、写入、列表、检查存在）
   - 参数：operation、path、content（可选）
   - 示例：`"检查文件是否存在 /tmp"` 或 `"列出目录 /home"`

## 💡 使用示例

### 1. 计算数学表达式
```
用户：计算 (10+5)*2-3
Agent：计算结果: 27.0
```

### 2. 获取当前时间
```
用户：现在几点了
Agent：当前时间: 2024-01-15 14:30:25
```

### 3. 查询天气
```
用户：北京天气怎么样
Agent：北京 当前天气：晴朗，温度 15°C，湿度 45%
```

## 🎯 特性

- ✅ 模块化架构，易于扩展
- ✅ 支持内置工具和MCP工具
- ✅ 智能意图识别
- ✅ RESTful API接口
- ✅ Web演示界面
- ✅ 完整的错误处理
- ✅ 日志记录
- ✅ CORS支持 