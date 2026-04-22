package org.example.model;

public class Utente {
    private String username, email, password;

    public Utente(String username, String email, String password) {
        this.username = username.trim();
        this.email = email.toLowerCase().trim();
        this.password = password.trim();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String toCsv(){
        return username+";"+email+";"+password;
    }
}
