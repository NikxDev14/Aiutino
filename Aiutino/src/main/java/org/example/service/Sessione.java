package org.example.service;

import org.example.model.Utente;

public class Sessione {
    private static Utente utenteLoggato = null;

    public static void login(Utente u){
        utenteLoggato = u;
    }
    public static void logout(){
        utenteLoggato = null;
    }
    public static boolean isAutenticato(){
        return utenteLoggato != null;
    }
    public static Utente getUtente(){
        return utenteLoggato;
    }
}
