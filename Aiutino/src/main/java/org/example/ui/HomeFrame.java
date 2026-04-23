package org.example.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.service.Sessione;

public class HomeFrame extends BorderPane {

    public HomeFrame(){
        System.out.println("Debug --> Stato autenticazione: " + Sessione.isAutenticato() + " Utente: " + Sessione.getUtente());
        //Applicare una classe CSS al contenitore principale
        this.getStyleClass().add("contenitore-principale");

        //Aggiungere header e body creati
        setTop(creaHeader());
        setCenter(creaBody());
    }

    //Crea la barra in cima al programma
    private HBox creaHeader(){
        HBox header = new HBox(20);
        header.getStyleClass().add("header"); //Assegna una classe

        Label titolo = new Label("Aiutino?");
        titolo.getStyleClass().add("titolo");

        Button btnFiltro = new Button("Filtra categorie");
        btnFiltro.getStyleClass().add("btnFiltro");

        Region spazio = new Region(); //Componente grafico vuoto che occupa spazio
        HBox.setHgrow(spazio, Priority.ALWAYS); //La sua dimensione cresce schiacciando ciò che c'è dopo

        header.getChildren().addAll(titolo,btnFiltro,spazio);

        if (Sessione.isAutenticato()){
            Button btnRecensioni = new Button("+ Aggiungi recensione");
            btnRecensioni.getStyleClass().add("btnProfilo");
            header.getChildren().add(btnRecensioni);
            btnRecensioni.setOnAction(e -> {

            });
        }

        Button btnProfilo = new Button(Sessione.isAutenticato() ? "Profilo" : "Accedi");
        btnProfilo.getStyleClass().add("btnProfilo");
        btnProfilo.setOnAction(e -> {
            if (!Sessione.isAutenticato()){ //Se autenticato entra nel profilo, altrimenti bisogna autenticarsi
                LoginFrame loginFrame = new LoginFrame(); //Creazione oggetto loginFrame
                this.getScene().setRoot(loginFrame); //Cambio scena
            }
        });
        header.getChildren().add(btnProfilo);

        return header;
    }

    private ScrollPane creaBody(){
        GridPane griglia = new GridPane(); //Griglia flessibile e variabile quando si aggiungono nodi
        griglia.setHgap(20); //Distanza orizzontale tra colonne
        griglia.setVgap(20); //Distanza verticale tra colonne
        griglia.setPadding(new Insets(30)); //Margine tra contenuto e quadrati della griglia

        //Assegnazione di una proporzione alle due colonne
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(50);
        griglia.getColumnConstraints().setAll(col,col);

        //Popolazione body con recensioni

        //Rendere griglia scorribile
        ScrollPane sp = new ScrollPane(griglia);
        sp.setFitToWidth(true);
        sp.getStyleClass().add("body-scroll");
        return sp;
    }
}
