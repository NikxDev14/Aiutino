package org.example.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.example.model.Recensione;
import java.util.ArrayList;

public class AIService {

    private static final String API_KEY = EnvLoader.getEnv("APY_KEY_AI");
    private static final String URL = EnvLoader.getEnv("URL_AI") + API_KEY;

    public static String generaRiassunto(ArrayList<Recensione> recensioni) {
        StringBuilder sb = new StringBuilder();
        for (Recensione r : recensioni) {
            sb.append(r.getCategoria()).append(": ").append(r.getCommento()).append("\n");
        }

        String prompt = "Riassumi in modo ironico queste recensioni, dividendole per categorie: " + sb.toString();

        //Json con libreria GSON
        //La struttura di Gemini è: { "contents": [ { "parts": [ { "text": "..." } ] } ] }
        JsonObject payload = new JsonObject();
        JsonArray contents = new JsonArray();
        JsonObject contentObj = new JsonObject();
        JsonArray parts = new JsonArray();
        JsonObject textObj = new JsonObject();

        textObj.addProperty("text", prompt);
        parts.add(textObj);
        contentObj.add("parts", parts);
        contents.add(contentObj);
        payload.add("contents", contents);

        Gson gson = new Gson();
        String json = gson.toJson(payload);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //Risposta ai
            JsonObject fullResp = gson.fromJson(response.body(), JsonObject.class);
            return fullResp.getAsJsonArray("candidates")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("content")
                    .getAsJsonArray("parts")
                    .get(0).getAsJsonObject()
                    .get("text").getAsString();

        } catch (Exception e) {
            return "Errore: l'AI ha deciso di prendersi qualche giorno di ferie.";
        }
    }
}