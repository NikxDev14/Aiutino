package org.example.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfo;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class GoogleAuthService {
    //Permessi richiesti
    private static final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/userinfo.profile",
            "https://www.googleapis.com/auth/userinfo.email"
    );

    public static Userinfo login() throws Exception {
        //Carica le credenziali dal file JSON
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                GsonFactory.getDefaultInstance(),
                new InputStreamReader(GoogleAuthService.class.getResourceAsStream("/credentials.json"))
        );

        //Configura il flusso di autorizzazione
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                clientSecrets,
                SCOPES
        ).setAccessType("offline").build();

        //Apre il browser e aspetta il codice di ritorno su una porta locale
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        var credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        //Recupera le info del profilo usando il token ottenuto
        Oauth2 oauth2 = new Oauth2.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credential
        ).setApplicationName("IlMioAiutino").build();

        return oauth2.userinfo().get().execute();
    }
}