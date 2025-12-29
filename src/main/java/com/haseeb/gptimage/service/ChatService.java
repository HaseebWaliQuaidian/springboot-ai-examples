package com.haseeb.gptimage.service;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.content.Media;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.io.IOException;
import java.util.List;

@Service
public class ChatService {

    private final OpenAiChatModel chatClient;


    public ChatService(OpenAiChatModel chatClient) {
        this.chatClient = chatClient;
    }

    public ChatResponse generateChatResponse(String category, String year) {
        PromptTemplate promptTemplate = new PromptTemplate(
                """
                Please provide me best book for the given {category} and the {year}.
                Please do provide a summary of the book as well, the information should be 
                limited and not much in depth. Please provide the details in the JSON format
                containing this information : category, book, year, review, author, summary
                """
        );
        promptTemplate.add("category", category);
        promptTemplate.add("year", year);
        Prompt prompt = promptTemplate.create();

        return chatClient.call(prompt);
    }

    public String getImageChatReader(String query) throws IOException {
        var imageResource = new ClassPathResource("/images/code.png");

        var userMessage = UserMessage.builder()
                .text(query)
                .text(query + "\n\nReturn the result as JSON only.")
                .media(new Media(MimeTypeUtils.IMAGE_PNG,imageResource))
                .build();
        ChatResponse response = chatClient.call(new Prompt(userMessage));
        return response.getResult().getOutput().getText();
    }
}
