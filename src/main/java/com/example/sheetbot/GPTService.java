package com.example.sheetbot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GPTService {

    @Value("${openai.api.key}")
    private String apiKey;

    public String extractIntent(String question) throws Exception {
        String prompt = """
            You are CourseBot, a GPT assistant that helps users find online courses from a catalog.
            Understand fuzzy spelling and help users with course platform tips (like LinkedIn, LMS).
            If the question is off-topic, say: "I'm here to help with course-related questions only 😊"
        """;

        JSONArray messages = new JSONArray();
        messages.put(new JSONObject().put("role", "system").put("content", prompt));
        messages.put(new JSONObject().put("role", "user").put("content", question));

        JSONObject payload = new JSONObject();
        payload.put("model", "gpt-3.5-turbo");
        payload.put("messages", messages);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://openrouter.ai/api/v1/chat/completions"))
                .header("Authorization", "Bearer " + apiKey.trim())
                .header("Content-Type", "application/json")
                .header("HTTP-Referer", "https://yourdomain.com")
                .header("X-Title", "coursebot")
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());
        return json.getJSONArray("choices")
                   .getJSONObject(0)
                   .getJSONObject("message")
                   .getString("content")
                   .trim();
    }
}
