package com.example.springai.old.chat;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/ask")
    public String ask(@RequestBody String question) {
        return chatService.chat(question);
    }

    @GetMapping("/test")
    public String test() {
        return chatService.chat("你好，请简单介绍一下自己");
    }
}