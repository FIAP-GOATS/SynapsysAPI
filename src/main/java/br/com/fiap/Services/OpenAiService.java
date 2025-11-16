package br.com.fiap.Services;

import br.com.fiap.Global;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OpenAiService {

    public String Chat(String systemPrompt, String userPrompt) throws IOException, InterruptedException {
        /// Create the JSON payload;
        JsonObject payload = new JsonObject();
        payload.addProperty("model", "gpt-4.1-mini");

        /// Create messages array;
        JsonArray messages = new JsonArray();

        /// Add system and user prompts;
        JsonObject systemMsg = new JsonObject();
        systemMsg.addProperty("role", "system");
        systemMsg.addProperty("content", systemPrompt);

        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role", "user");
        userMsg.addProperty("content", userPrompt);

        messages.add(systemMsg);
        messages.add(userMsg);

        /// Add messages to payload;
        payload.add("messages", messages);

        /// Build HTTP request;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Global.OpenAiApiKey)
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .build();

        /// Send HTTP request;
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        /// Parse response;
        String responseBody = response.body();
        JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
        JsonArray choices = json.getAsJsonArray("choices");
        JsonObject message = choices.get(0).getAsJsonObject().getAsJsonObject("message");
        return message.get("content").getAsString();
    }


}
