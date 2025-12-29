package com.haseeb.gptimage.controller;

import com.haseeb.gptimage.dto.BooksInfo;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai/structured")
public class StructuredResponseController {

    private final ChatClient chatClient;

    @Autowired
    public StructuredResponseController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @PostMapping("/v1/chat")
    public String getVersionOneResponse(@RequestParam String category, @RequestParam String year) {
        return chatClient.prompt()
                .user(
                        u -> u.text(
                                "Please provide me best book for the given " + category + " and the " + year +
                                " \n\nReturn the result as JSON only.")
                                .param("category", category)
                                .param("year", year)
                )
                .call()
                .content();
    }

    /**
     * Using Bean output converter
     * with this, we can remove the property as response is a class spring.ai.openai.chat.options.responseFormat.type=JSON_OBJECT
     */
    @PostMapping("/v2/chat")
    public BooksInfo getBooksInfo(@RequestParam String category, @RequestParam String year) {
        return chatClient.prompt()
                .user(
                        u -> u.text(
                                "Please provide me best book for the given " + category + " and the " + year +
                                " \n\nReturn the result as JSON only.")
                                .param("category", category)
                                .param("year", year)
                )
                .call()
                .entity(BooksInfo.class);

    }

    @PostMapping("/v3/chat")
    public List<BooksInfo> getListBeanResponse(@RequestParam String category, @RequestParam String year) {
        return chatClient.prompt()
                .user(
                        u -> u.text(
                                "Please provide me 2 bestbooks for the given " + category + " and the " + year +
                                " \n\nReturn the result as JSON array only.")
                                .param("category", category)
                                .param("year", year)
                )
                .call()
                .entity(new ParameterizedTypeReference<List<BooksInfo>>(){});
    }

    /**
     * using list output converter
     * Looking for not a specific class response but generic response
     * e.g
     * [
     *     "\"Clean Code: A Handbook of Agile Software Craftsmanship\"",
     *     "\"The Pragmatic Programmer: Your Journey to Mastery\""
     * ]
     */
    @PostMapping("/v4/chat")
    public List<String> getListResponse(@RequestParam String category, @RequestParam String year) {
        return chatClient.prompt()
                .user(
                        u -> u.text(
                                "Please provide me 2 bestbooks for the given " + category + " and the " + year +
                                " \n\nReturn the result as JSON array only.")
                                .param("category", category)
                                .param("year", year)
                )
                .call()
                .entity(new ListOutputConverter(new DefaultConversionService()));
    }

    /**
     * using mapOutputConverter
     *
     */
    @PostMapping("/v5/chat")
    public Map<String, Object> getMapResponse(@RequestParam String category, @RequestParam String year) {
        return chatClient.prompt()
                .user(
                        u -> u.text(
                                "Please provide me best book for the given " + category + " and the " + year +
                                        " Please do provide a summary of the book as well, the information should be limited and not much in depth." +
                                        " the response should be containing this information : category, book, year, review, author, summary." +
                                        " \n\nReturn the result as JSON object only." +
                                        " Please remove ''' response from the final output.")
                                .param("category", category)
                                .param("year", year)
                )
                .call()
                .entity(new MapOutputConverter());
    }
}
