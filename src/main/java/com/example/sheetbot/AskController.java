package com.example.sheetbot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AskController {

    @Autowired
    private GPTService gptService;

    @PostMapping("/ask")
    public Map<String, String> ask(@RequestBody Map<String, String> payload) throws Exception {
        String question = payload.get("question");
        String reply = gptService.extractIntent(question);
        return Map.of("reply", reply);
    }
}
