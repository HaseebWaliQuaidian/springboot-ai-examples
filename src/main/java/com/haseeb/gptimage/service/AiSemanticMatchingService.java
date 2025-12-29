package com.haseeb.gptimage.service;

import com.haseeb.gptimage.dto.SemanticOutputDto;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiSemanticMatchingService {

    private final OpenAiChatModel chatModel;

    public AiSemanticMatchingService(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public List<SemanticOutputDto> getSemanticMatch(List<String> sourceList, List<String> destinationList) {
        BeanOutputConverter<List<SemanticOutputDto>> beanOutputConverter = new BeanOutputConverter<>(
                new ParameterizedTypeReference<List<SemanticOutputDto>>() {
                });
        String format = beanOutputConverter.getFormat();
        var template = """
               
                "You will be provided a source and a destination list containing strings.
                "You need to take one by one string until the source list is empty
                "and try to match it with the destination list.

                
                "Please note that you ned match the strings on the basis of semantic mapping not just matched on string similarity.
                "If you are not able to find any match, please return 'No Match Found' in the destination list.
                "Please also mention the matching source based on your confidence on the mapping.
                "The match should be a number between 0 and 100. Where 0 means no match and 100 means perfect match.
                
                source List questions:
                {source_list}
                
                destination List questions:
                {destination_list}
                
                Your response:
                {format}
                """;

        PromptTemplate promptTemplate = new PromptTemplate(template);
        promptTemplate.add("source_list", sourceList);
        promptTemplate.add("destination_list", destinationList);
        promptTemplate.add("format", format);
        Prompt prompt = promptTemplate.create();
        return beanOutputConverter.convert(chatModel.call(prompt).getResult().getOutput().getText());
    }
}
