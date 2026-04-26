package org.example.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.model.Recensione;
import org.example.service.FileFrasi;
//import org.example.service.FileRecensioni;
import org.example.service.FileRecensioni;
import org.example.service.Sessione;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AggiungiRecensioneOverlay extends StackPane {

    private VBox containerCheckBox = new VBox(10);
    private Map<String, List<String>> archivioFrasi;

    public AggiungiRecensioneOverlay(Runnable onAggiornaHome) {
        //Copia le frasi da csv
        this.archivioFrasi = FileFrasi.caricaFrasiPerCategoria();

        //Sfondo che nasconde ciò che c'è dietro
        this.getStyleClass().add("overlay-sfondo");

        //Card aggiunta recensione
        VBox card = new VBox(20);
        card.getStyleClass().add("login-card"); //Riutilizza css login (da rivedere)
        card.setMaxSize(500, 650);
        card.setPadding(new Insets(30));
        card.setAlignment(Pos.TOP_CENTER);

        //Header card
        HBox header = new HBox();
        Label titolo = new Label("Nuova Recensione");
        titolo.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Region spazio = new Region(); //Come nella HomeFrame, crea uno spazio vuoto
        HBox.setHgrow(spazio, Priority.ALWAYS); //Prende tutto lo spazio disponibile

        Label btnChiudi = new Label("X");
        btnChiudi.getStyleClass().add("btn-chiudi");
        btnChiudi.setOnMouseClicked(e -> chiudi());

        header.getChildren().addAll(titolo, spazio, btnChiudi);

        //Selezione categoria
        ComboBox<String> comboCategorie = new ComboBox<>();
        comboCategorie.getItems().addAll(archivioFrasi.keySet()); //Aggiunge le varie categorie
        comboCategorie.setPromptText("Seleziona una categoria");
        comboCategorie.setMaxWidth(Double.MAX_VALUE);

        //CheckBox dinamico
        comboCategorie.setOnAction(e -> {
            containerCheckBox.getChildren().clear();
            List<String> frasi = archivioFrasi.get(comboCategorie.getValue()); //Salva nella lista le frasi in base alla categoria selezionata
            if (frasi != null) {
                for (String frase : frasi) {
                    CheckBox cb = new CheckBox(frase);
                    cb.setWrapText(true);
                    containerCheckBox.getChildren().add(cb);
                }
            }
        });

        //Selezione delle stelle
        Label lblStelle = new Label("Quanto è stato tragico? (1-5 stelle)");
        Slider sliderStelle = new Slider(1, 5, 3);
        sliderStelle.setMajorTickUnit(1);
        sliderStelle.setMinorTickCount(0);
        sliderStelle.setSnapToTicks(true);
        sliderStelle.setShowTickMarks(true);

        //Commento
        TextArea areaCommento = new TextArea();
        areaCommento.setPromptText("Aggiungi un commento (se hai ancora fiato per lamentarti)...");
        areaCommento.setPrefRowCount(3);
        areaCommento.setWrapText(true);

        //Bottone pubblica
        Button btnPubblica = new Button("PUBBLICA RECENSIONE");
        btnPubblica.getStyleClass().add("btnAccedi"); //Riusco css bottone accedi
        btnPubblica.setMaxWidth(Double.MAX_VALUE);

        btnPubblica.setOnAction(e -> {
            if (comboCategorie.getValue() == null) {
                System.out.println("Seleziona almeno una categoria!");
                return;
            }

            //Salva frasi spuntate
            List<String> frasiSelezionate = new ArrayList<>();
            containerCheckBox.getChildren().forEach(nodo -> {
                if (nodo instanceof CheckBox cb && cb.isSelected()) {
                    frasiSelezionate.add(cb.getText());
                }
            });

            //Crea un nuovo oggetto
            Recensione r = new Recensione(Sessione.getUtente().getUsername(), comboCategorie.getValue(), (int) sliderStelle.getValue(), areaCommento.getText(), frasiSelezionate);

            FileRecensioni.salvaRecensione(r);
            onAggiornaHome.run(); //Ricarica home
            chiudi();
        });

        //Scrollpane per checkbox
        ScrollPane scrollFrasi = new ScrollPane(containerCheckBox);
        scrollFrasi.setFitToWidth(true);
        scrollFrasi.setPrefHeight(150);
        scrollFrasi.setStyle("-fx-background-color: transparent;");

        card.getChildren().addAll(header, comboCategorie, new Label("Scegli le frasi adatte:"), scrollFrasi, lblStelle, sliderStelle, areaCommento, btnPubblica);
        this.getChildren().add(card);
    }

    private void chiudi() {
        //Si autoelimina
        if (this.getParent() instanceof Pane genitore) {
            genitore.getChildren().remove(this);
        }
    }
}