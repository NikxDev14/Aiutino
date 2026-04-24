package org.example.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.model.Recensione;
import org.example.service.FileRecensioni;
import org.example.service.Sessione;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFrame extends StackPane {

    BorderPane layoutPrincipale;

    public HomeFrame(){
        System.out.println("Debug --> Stato autenticazione: " + Sessione.isAutenticato() + " Utente: " + Sessione.getUtente());
        //Applicare una classe CSS al contenitore principale
        this.getStyleClass().add("contenitore-principale");

        //Creazione pannello principale
        layoutPrincipale = new BorderPane();

        //Aggiungere header e body creati
        layoutPrincipale.setTop(creaHeader());
        layoutPrincipale.setCenter(creaBody());

        this.getChildren().add(layoutPrincipale);
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
                AggiungiRecensioneOverlay overlay = new AggiungiRecensioneOverlay(() -> { //Codice eseguito dopo aver pubblicato recensione

                    layoutPrincipale.setCenter(creaBody()); //Ricarica il body con le recensioni
                });

                this.getChildren().add(overlay);
            });
        }

        Button btnProfilo = new Button(Sessione.isAutenticato() ? Sessione.getUtente().getUsername() : "Accedi");
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
        ArrayList<Recensione> recensioni = FileRecensioni.leggiRecensioni();
        Collections.reverse(recensioni);

        int riga = 0,colonna = 0;
        for (Recensione r : recensioni){
            VBox card = creaCardRecensione(r);
            griglia.add(card, colonna, riga);

            colonna++;
            if (colonna==2){
                colonna = 0;
                riga++;
            }
        }

        //Rendere griglia scorribile
        ScrollPane sp = new ScrollPane(griglia);
        sp.setFitToWidth(true);
        griglia.getStyleClass().add("body-scroll");
        griglia.minHeightProperty().bind(sp.heightProperty());
        return sp;
    }

    private VBox creaCardRecensione(Recensione r) {
        VBox card = new VBox(10);
        card.getStyleClass().add("login-card"); //Riuso del css della login card

        Label autore = new Label("@" + r.getUtente());
        Label stelle = new Label("★".repeat(r.getStelle())); //Scrive tante stelle quanto segnate nell'oggetto
        Label commento = new Label(r.getCommento());

        card.getChildren().addAll(autore, stelle, new Label(r.getCategoria()), commento);

        //Cliccando mostra tutti i dettagli della recensione
        card.setOnMouseClicked(e -> mostraDettaglioRecensione(r));

        return card;
    }

    private void mostraDettaglioRecensione(Recensione r) {
        //Oscura ciò che c'è sotto
        VBox overlay = new VBox();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.7);"); //Imposta l'opacità
        overlay.setAlignment(Pos.CENTER);

        //Card recensione completa
        VBox dettaglio = new VBox(20);
        dettaglio.getStyleClass().add("login-card"); //Riusa il formato login
        dettaglio.setMaxSize(400, 500);
        dettaglio.setPadding(new Insets(30));

        Label chiudi = new Label("X");
        chiudi.getStyleClass().add("btn-chiudi");
        chiudi.setOnMouseClicked(ev -> this.getChildren().remove(overlay)); //Chiude la scheda

        Label titolo = new Label("Dettagli Recensione");
        titolo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Sezione Frasi Ironiche
        VBox boxFrasi = new VBox(5);
        Label lblFrasi = new Label("Frasi selezionate:");
        lblFrasi.setStyle("-fx-font-weight: bold;");
        boxFrasi.getChildren().add(lblFrasi);

        for (String frase : r.getFrasiIroniche()) {
            Label f = new Label("- " + frase);
            f.setStyle("-fx-font-style: italic; -fx-text-fill: #e67e22;");
            boxFrasi.getChildren().add(f);
        }

        dettaglio.getChildren().addAll(chiudi, titolo, new Label("Autore: " + r.getUtente()), boxFrasi);
        overlay.getChildren().add(dettaglio);

        // Aggiungiamo l'overlay sopra a tutto
        this.getChildren().add(overlay);
    }
}
