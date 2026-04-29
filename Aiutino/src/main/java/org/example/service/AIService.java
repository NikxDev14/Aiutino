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

    private static final String APY_KEY = EnvLoader.getEnv("APY_KEY_AI");
    private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3-flash-preview:generateContent?key=" + APY_KEY.trim();

    public static String generaRiassunto(ArrayList<Recensione> recensioni) {
        System.out.println(URL);
        StringBuilder sb = new StringBuilder();
        for (Recensione r : recensioni) {
            sb.append("Categoria: " + r.getCategoria()).append(": ").append("Commento: " + r.getCommento()).append("\n");
        }

        String prompt;
        if (Sessione.isAutenticato()){
            prompt = "Riassumi in modo ironico queste recensioni, dividendole per categorie. Saluta l'utente " +Sessione.getUtente().getUsername()+
                    " in modo simpatico prima di fare il riassunto. Ecco le recensioni: ";
        }
        else {
            prompt = "Riassumi in modo ironico queste recensioni, dividendole per categorie." +
                    " Saluta l'utente non autenticato in modo generico ma simpatico. Ecco le recensioni: ";
        }
        prompt = prompt + sb.toString();

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

            System.out.println("Debug --> Codice Risposta: " + response.statusCode());
            System.out.println("Debug --> Corpo Risposta: " + response.body());

            if (response.statusCode() != 200) {
                return "Errore API (" + response.statusCode() + "): Controlla i log in console.";
            }

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