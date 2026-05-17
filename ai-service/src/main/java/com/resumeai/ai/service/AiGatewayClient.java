package com.resumeai.ai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AiGatewayClient {

    @Value("${app.ai.gateway-url:https://ai.gateway.lovable.dev/v1/chat/completions}")
    private String gatewayUrl;
    @Value("${app.ai.api-key:}")
    private String apiKey;
    @Value("${app.ai.model:google/gemini-3-flash-preview}")
    private String model;

    public String complete(String system, String user) {
        if (apiKey == null || apiKey.isBlank()) {
            return "[AI disabled — set LOVABLE_API_KEY or OPENAI_API_KEY]";
        }
        WebClient client = WebClient.builder().baseUrl(gatewayUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role","system","content", system),
                        Map.of("role","user","content", user)));
        try {
            Map<?,?> resp = client.post().bodyValue(body).retrieve().bodyToMono(Map.class).block();
            if (resp == null) return "";
            List<?> choices = (List<?>) resp.get("choices");
            if (choices == null || choices.isEmpty()) return "";
            Map<?,?> first = (Map<?,?>) choices.get(0);
            Map<?,?> message = (Map<?,?>) first.get("message");
            return String.valueOf(message.get("content"));
        } catch (Exception e) {
            return "[AI error: " + e.getMessage() + "]";
        }
    }
}
