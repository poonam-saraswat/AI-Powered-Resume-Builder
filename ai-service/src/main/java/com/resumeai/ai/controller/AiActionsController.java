package com.resumeai.ai.controller;

import com.resumeai.ai.service.AiGatewayClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiActionsController {
    private final AiGatewayClient ai;

    @PostMapping("/improve")
    public Map<String,String> improve(@RequestBody Map<String,String> body){
        String text = body.getOrDefault("text","");
        String out = ai.complete(
            "You are an expert resume editor. Improve clarity, impact and concision. Return ONLY the rewritten text.",
            text);
        return Map.of("result", out);
    }

    @PostMapping("/ats-score")
    public Map<String,String> ats(@RequestBody Map<String,String> body){
        String resume = body.getOrDefault("resume","");
        String jd = body.getOrDefault("jobDescription","");
        String out = ai.complete(
            "You are an ATS expert. Given a resume and a job description, return a JSON object with: score (0-100), missingKeywords (array), suggestions (array). Return ONLY JSON.",
            "RESUME:\n"+resume+"\n\nJOB DESCRIPTION:\n"+jd);
        return Map.of("result", out);
    }
}
