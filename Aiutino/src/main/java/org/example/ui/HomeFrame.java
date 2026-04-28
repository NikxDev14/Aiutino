package org.example.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.model.Recensione;
import org.example.service.AIService;
import org.example.service.FileFrasi;
import org.example.service.FileRecensioni;
import org.example.service.Sessione;
import javafx.scene.text.Text;
//import org.w3c.dom.Text;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HomeFrame extends StackPane {

    private String filtroAttivo;
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

        Button btnAI = new Button("✨ Chiedi all'AI");
        btnAI.getStyleClass().add("btnFiltro"); // Usa lo stile trasparente che abbiamo creato

        btnAI.setOnAction(e -> {
            ArrayList<Recensione> lista = FileRecensioni.leggiRecensioni();

            //Genera riassunto recensioni e le inoltra alla ai
            String riassunto = AIService.generaRiassunto(lista);
            mostraOverlayAI(riassunto);
        });

        MenuButton btnFiltro = new MenuButton("Filtra categorie");
        btnFiltro.getStyleClass().add("btnFiltro");

        MenuItem nessuno = new MenuItem("Nessun filtro");
        nessuno.setOnAction(e -> {
            filtroAttivo = null;
            layoutPrincipale.setCenter(creaBody());
        });
        btnFiltro.getItems().add(nessuno);

        Map<String, List<String>> categorie = FileFrasi.caricaFrasiPerCategoria();

        for (String cat : categorie.keySet()) {
            MenuItem item = new MenuItem(cat);
            item.setOnAction(e -> {
                filtroAttivo = cat; //Cliccando su un determinato filtro, lo attiva
                layoutPrincipale.setCenter(creaBody()); //Ricarica body
            });
            btnFiltro.getItems().add(item);
        }

        Region spazio = new Region(); //Componente grafico vuoto che occupa spazio
        HBox.setHgrow(spazio, Priority.ALWAYS); //La sua dimensione cresce schiacciando ciò che c'è dopo

        header.getChildren().addAll(titolo,btnAI,spazio, btnFiltro);

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
            else {
                this.getScene().setRoot(new ProfiloFrame());
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
            if (filtroAttivo != null && !r.getCategoria().equalsIgnoreCase(filtroAttivo)) {
                continue;
            }
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
        sp.getStyleClass().add("body-scroll");
        griglia.minHeightProperty().bind(sp.heightProperty());
        return sp;
    }

    private VBox creaCardRecensione(Recensione r) {
        VBox card = new VBox(10);
        card.getStyleClass().add("recensioni-card"); //css

        Label autore = new Label("@" + r.getUtente());
        autore.getStyleClass().add("autore"); //css

        Label stelle = new Label("★".repeat(r.getStelle())); //Scrive tante stelle quanto segnate nell'oggetto
        stelle.getStyleClass().add("stelle");

        Label categoria = new Label(r.getCategoria());
        categoria.getStyleClass().add("categoria");

        Text commento = new Text(r.getCommento());
        commento.getStyleClass().add("commento");
        commento.setWrappingWidth(320);

        card.getChildren().addAll(autore, stelle, categoria, commento);

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
        dettaglio.getStyleClass().add("recensioni-card"); //Riusa il formato login
        dettaglio.setMaxSize(400, 500);
        dettaglio.setPadding(new Insets(30));

        Label chiudi = new Label("X");
        chiudi.getStyleClass().add("btn-chiudi");
        chiudi.setOnMouseClicked(ev -> this.getChildren().remove(overlay)); //Chiude la scheda

        Label titolo = new Label("Dettagli Recensione");
        titolo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label stelle = new Label("★".repeat(r.getStelle())); //Scrive tante stelle quanto segnate nell'oggetto
        stelle.getStyleClass().add("stelle");

        Label autore = new Label("Autore: " + r.getUtente());
        autore.getStyleClass().add("autore");

        Label categoria = new Label(r.getCategoria());
        categoria.getStyleClass().add("categoria-dettaglio");

        Label lblCommento = new Label("Commento:");
        lblCommento.setStyle("-fx-font-weight: bold;");
        Text commento = new Text(r.getCommento());
        commento.getStyleClass().add("commento-dettaglio");
        commento.setWrappingWidth(320);

        //Sezione frasi ironiche
        VBox boxFrasi = new VBox(5);
        Label lblFrasi = new Label("Frasi selezionate:");
        lblFrasi.setStyle("-fx-font-weight: bold;");
        boxFrasi.getChildren().add(lblFrasi);

        for (String frase : r.getFrasiIroniche()) {
            Text f = new Text("- " + frase);
            f.getStyleClass().add("frase-ironica");
            f.setWrappingWidth(250);
            boxFrasi.getChildren().add(f);
        }

        dettaglio.getChildren().addAll(chiudi, titolo, autore,  stelle, categoria, boxFrasi, lblCommento, commento);
        overlay.getChildren().add(dettaglio);

        //Aggiunge l'overlay sopra a tutto
        this.getChildren().add(overlay);
    }

    private void mostraOverlayAI(String testo) {
        VBox overlay = new VBox(20);
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.8);");
        overlay.setAlignment(Pos.CENTER);

        VBox boxMessaggio = new VBox(15);
        boxMessaggio.getStyleClass().add("login-card");
        boxMessaggio.setMaxWidth(450);
        boxMessaggio.setPadding(new Insets(30));

        Label titolo = new Label("Riassunto Intelligente ✨");
        titolo.getStyleClass().add("login-titolo");

        Text contenuto = new Text(testo);
        contenuto.setWrappingWidth(380);
        contenuto.setStyle("-fx-font-size: 16px; -fx-font-style: italic;");

        Button btnChiudi = new Button("Capito boss!!");
        btnChiudi.getStyleClass().add("btnAccedi");
        btnChiudi.setOnAction(ev -> this.getChildren().remove(overlay));

        boxMessaggio.getChildren().addAll(titolo, contenuto, btnChiudi);
        overlay.getChildren().add(boxMessaggio);

        this.getChildren().add(overlay);
    }
}
