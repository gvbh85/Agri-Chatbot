package agri_chatbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String askGemini(String prompt) {
        try {
            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

            HttpHeaders headers = new HttpHeaders();
            headers.set("x-goog-api-key", geminiApiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONObject body = new JSONObject();
            body.put("prompt", prompt);
            body.put("temperature", 0.7);

            HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            JSONObject json = new JSONObject(response.getBody());

            // Gemini response structure may vary
            if (json.has("candidates")) {
                JSONArray candidates = json.getJSONArray("candidates");
                if (candidates.length() > 0) {
                    JSONObject first = candidates.getJSONObject(0);
                    if (first.has("content")) {
                        JSONObject content = first.getJSONObject("content");
                        if (content.has("text")) {
                            return content.getString("text");
                        }
                    }
                }
            }

            return "Gemini returned unknown response structure.";

        } catch (Exception e) {
            e.printStackTrace();
            return "Gemini API error: " + e.getMessage();
        }
    }
}
