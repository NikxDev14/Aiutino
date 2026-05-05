package org.example.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import org.example.model.Recensione;
import org.example.service.FileRecensioni;
import org.example.service.Sessione;

import java.util.ArrayList;
import java.util.Collections;

public class ProfiloFrame extends StackPane {

    private BorderPane layoutPrincipale;

    public ProfiloFrame() {
        this.getStyleClass().add("contenitore-principale");

        layoutPrincipale = new BorderPane();

        layoutPrincipale.setTop(creaHeaderProfilo());
        layoutPrincipale.setCenter(creaBodyProfilo());

        this.getChildren().add(layoutPrincipale);
    }

    private VBox creaHeaderProfilo() {
        VBox header = new VBox(15);
        header.getStyleClass().add("header-profilo");
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(40, 20, 40, 20));

        Button btnBack = new Button("< Torna alla Home");
        btnBack.getStyleClass().add("btnAltro");
        btnBack.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 10px;");
        btnBack.setOnAction(e -> this.getScene().setRoot(new HomeFrame()));

        Region spazio = new Region(); //Componente grafico vuoto che occupa spazio
        HBox.setHgrow(spazio, Priority.ALWAYS); //La sua dimensione cresce schiacciando ciò che c'è dopo

        Button btnLogout = new Button("Log out");
        btnLogout.getStyleClass().add("btnAltro");
        btnLogout.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-radius: 10px;");
        btnLogout.setOnAction(e -> {
            Sessione.logout();
            this.getScene().setRoot(new HomeFrame());
        });

        HBox topBar = new HBox(btnBack, spazio, btnLogout);
        topBar.setAlignment(Pos.TOP_LEFT);

        //Crea "foto profilo" con lettera
        Label avatar = new Label(Sessione.getUtente().getUsername().substring(0, 1).toUpperCase());
        avatar.getStyleClass().add("avatar-profilo");

        Label lblUser = new Label("@" + Sessione.getUtente().getUsername());
        lblUser.getStyleClass().add("titolo-profilo");

        Label sottotitolo = new Label("Le tue recensioni pubblicate");
        sottotitolo.setStyle("-fx-text-fill: rgba(255,255,255,0.8); -fx-font-size: 16px;");

        header.getChildren().addAll(topBar, avatar, lblUser, sottotitolo);
        return header;
    }

    private ScrollPane creaBodyProfilo() {
        VBox contenitoreCards = new VBox(20);
        contenitoreCards.setAlignment(Pos.TOP_CENTER);
        contenitoreCards.setPadding(new Insets(30));
        contenitoreCards.setStyle("-fx-background-color: transparent;");

        ArrayList<Recensione> tutte = FileRecensioni.leggiRecensioni();
        Collections.reverse(tutte);

        boolean haRecensioni = false;

        for (Recensione r : tutte) {    //Stampa solo recensioni di quel utente
            if (r.getUtente().equalsIgnoreCase(Sessione.getUtente().getUsername())) {
                contenitoreCards.getChildren().add(creaCardSemplice(r));
                haRecensioni = true;
            }
        }
        if (!haRecensioni) {
            Label messaggio = new Label("Non hai ancora scritto nessuna cattiveria... ehm, recensione.");
            messaggio.getStyleClass().add("scritta-errore");
            messaggio.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
            contenitoreCards.getChildren().add(messaggio);
        }

        ScrollPane sp = new ScrollPane(contenitoreCards);
        sp.setFitToWidth(true);
        sp.getStyleClass().add("body-scroll");

        return sp;
    }

    private VBox creaCardSemplice(Recensione r) {
        VBox card = new VBox(10);
        card.getStyleClass().add("recensioni-card");
        card.setMaxWidth(600);

        Label stelle = new Label("★".repeat(r.getStelle()));
        stelle.getStyleClass().add("stelle");

        Label cat = new Label(r.getCategoria());
        cat.getStyleClass().add("categoria");

        Text indirizzo = new Text(r.getIndirizzo());
        indirizzo.setStyle("-fx-text-fill: red; -fx-fill: red;");
        indirizzo.getStyleClass().add("indirizzo");
        indirizzo.setWrappingWidth(320);

        Text commento = new Text(r.getCommento());
        commento.getStyleClass().add("commento");
        commento.setWrappingWidth(500);

        Button btnElimina = new Button("Elimina recensione");
        btnElimina.getStyleClass().add("btnElimina");
        btnElimina.setOnAction(e ->{
            FileRecensioni.eliminaRecensione(r);
            layoutPrincipale.setCenter(creaBodyProfilo());
        });
        card.getChildren().addAll(stelle, cat, indirizzo, commento, btnElimina);

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
        dettaglio.getStyleClass().add("recensioni-card");
        dettaglio.setMaxSize(400, 500);
        dettaglio.setPadding(new Insets(30));

        Label chiudi = new Label("X");
        chiudi.getStyleClass().add("btn-chiudi");
        chiudi.setOnMouseClicked(ev -> this.getChildren().remove(overlay)); //Chiude la scheda

        Label titolo = new Label("Dettagli Recensione");
        titolo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label stelle = new Label("★".repeat(r.getStelle())); //Scrive tante stelle quanto segnate nell'oggetto
        stelle.getStyleClass().add("stelle");

        Label categoria = new Label(r.getCategoria());
        categoria.getStyleClass().add("categoria-dettaglio");

        Label lblIndirizzo = new Label("Indirizzo:");
        lblIndirizzo.setStyle("-fx-font-weight: bold;");
        Text indirizzo = new Text(r.getIndirizzo());
        indirizzo.getStyleClass().add("commento-dettaglio");
        indirizzo.setWrappingWidth(320);

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

        dettaglio.getChildren().addAll(chiudi, titolo, stelle, categoria, boxFrasi, lblIndirizzo, indirizzo, lblCommento, commento);
        overlay.getChildren().add(dettaglio);

        //Aggiunge l'overlay sopra a tutto
        this.getChildren().add(overlay);
    }
}