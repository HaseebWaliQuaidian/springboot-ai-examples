package com.haseeb.gptimage.controller;

import com.haseeb.gptimage.service.ChatService;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class ChatController {
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }
    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @PostMapping("/chat")
    public String generateChatResponse(@RequestParam String category, @RequestParam String year) {
        System.out.println(apiKey);
        return chatService.generateChatResponse(category,year).getResult().getOutput().getText();
    }
    @PostMapping("/image")
    public String getImageChatReader(@RequestParam String query) throws IOException {
        return chatService.getImageChatReader(query);
    }

}
