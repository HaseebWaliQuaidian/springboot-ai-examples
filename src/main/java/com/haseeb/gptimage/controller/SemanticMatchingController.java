package com.haseeb.gptimage.controller;

import com.haseeb.gptimage.dto.SemanticOutputDto;
import com.haseeb.gptimage.dto.SemanticRequestDto;
import com.haseeb.gptimage.service.AiSemanticMatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ai/semantic")
public class SemanticMatchingController {
    private final AiSemanticMatchingService aiSemanticMatchingService;
    @Autowired
    public SemanticMatchingController(AiSemanticMatchingService aiSemanticMatchingService) {
        this.aiSemanticMatchingService = aiSemanticMatchingService;
    }

    @PostMapping("/match")
    public List<SemanticOutputDto> getSemanticMatch(@RequestBody SemanticRequestDto request) {

        return aiSemanticMatchingService.getSemanticMatch(request.sourceList(), request.destinationList());
    }
}
