package br.com.fiap.services;

import br.com.fiap.Global;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class OpenAiService {

    public String Chat(String systemPrompt, String userPrompt) throws IOException, InterruptedException {
        if (Global.OpenAiApiKey == null || Global.OpenAiApiKey.isBlank()) {
            throw new IllegalStateException("OpenAI API key is null or empty (Global.OpenAiApiKey).");
        }

        // Build payload
        JsonObject payload = new JsonObject();
        payload.addProperty("model", "gpt-4.1-mini"); // keep your model here, but verify it's available to your key

        JsonArray messages = new JsonArray();

        JsonObject systemMsg = new JsonObject();
        systemMsg.addProperty("role", "system");
        systemMsg.addProperty("content", systemPrompt);
        messages.add(systemMsg);

        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role", "user");
        userMsg.addProperty("content", userPrompt);
        messages.add(userMsg);

        payload.add("messages", messages);

        // Build request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Global.OpenAiApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int status = response.statusCode();
        String responseBody = Optional.ofNullable(response.body()).orElse("");

        // If non-2xx, include the body in the exception so you can inspect it
        if (status < 200 || status >= 300) {
            String message = String.format("OpenAI API returned status %d. Body: %s", status, responseBody);
            throw new IOException(message);
        }

        // Parse JSON safely
        JsonObject json;
        try {
            json = JsonParser.parseString(responseBody).getAsJsonObject();
        } catch (Exception e) {
            throw new IOException("Failed to parse OpenAI response as JSON. Body: " + responseBody, e);
        }

        // If API returned an error object, surface it
        if (json.has("error")) {
            JsonObject error = json.getAsJsonObject("error");
            throw new IOException("OpenAI error: " + error.toString());
        }

        // Extract choices safely
        JsonArray choices = json.has("choices") ? json.getAsJsonArray("choices") : null;
        if (choices == null || choices.size() == 0) {
            throw new IOException("No choices returned by OpenAI. Full response: " + responseBody);
        }

        JsonObject firstChoice = choices.get(0).getAsJsonObject();

        // Prefer the chat-completion structure: choices[0].message.content
        String content = null;
        if (firstChoice.has("message")) {
            JsonObject messageObj = firstChoice.getAsJsonObject("message");
            if (messageObj.has("content")) {
                try {
                    content = messageObj.get("content").getAsString();
                } catch (Exception ignored) {}
            }
        }

        // Fallback to older text field if present
        if ((content == null || content.isEmpty()) && firstChoice.has("text")) {
            try {
                content = firstChoice.get("text").getAsString();
            } catch (Exception ignored) {}
        }

        if (content == null) {
            throw new IOException("No usable content found in first choice. Full response: " + responseBody);
        }

        return content.trim();
    }
}