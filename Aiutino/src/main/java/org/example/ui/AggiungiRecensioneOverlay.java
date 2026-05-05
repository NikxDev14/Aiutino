package org.example.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
        card.getStyleClass().add("card-add-recensioni"); // css
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
        comboCategorie.getStyleClass().add("box-categoria");

        comboCategorie.setMaxWidth(Double.MAX_VALUE);

        //CheckBox dinamico
        comboCategorie.setOnAction(e -> {
            containerCheckBox.getChildren().clear();
            List<String> frasi = archivioFrasi.get(comboCategorie.getValue()); //Salva nella lista le frasi in base alla categoria selezionata
            if (frasi != null) {
                for (String frase : frasi) {
                    CheckBox cb = new CheckBox(frase);
                    cb.setWrapText(true);

                    cb.getStyleClass().add("checkbox");

                    containerCheckBox.getChildren().add(cb);
                }
            }
        });

        //Selezione delle stelle
        Label lblStelle = new Label("Quanto è stato tragico? (1-5 stelle)");
        lblStelle.getStyleClass().add("lblStelle");

        Slider sliderStelle = new Slider(1, 5, 3);
        sliderStelle.setMajorTickUnit(1);
        sliderStelle.setMinorTickCount(0);
        sliderStelle.setSnapToTicks(true);
        sliderStelle.setShowTickMarks(true);
        sliderStelle.getStyleClass().add("slider");

        //Indirizzo
        TextArea areaIndirizzo = new TextArea();
        areaIndirizzo.setPromptText("Lo scrivi il nome del posto e l'indirizzo o è un segreto?");
        areaIndirizzo.getStyleClass().add("lblStelle");//stesso css di lblStelle

        areaIndirizzo.setPrefRowCount(1); //righe da visualizzare
        areaIndirizzo.setWrapText(true);

        //Commento
        TextArea areaCommento = new TextArea();
        areaCommento.setPromptText("Aggiungi un commento (se hai ancora fiato per lamentarti)...");
        areaCommento.getStyleClass().add("lblStelle");//stesso css di lblStelle

        areaCommento.setPrefRowCount(3);
        areaCommento.setWrapText(true);

        //Bottone pubblica
        Button btnPubblica = new Button("PUBBLICA RECENSIONE");
        btnPubblica.getStyleClass().add("btnAccedi"); //Riusco css bottone accedi
        btnPubblica.setMaxWidth(Double.MAX_VALUE);

        Text messaggioErrore = new Text("");
        messaggioErrore.getStyleClass().add("scritta-errore");
        messaggioErrore.setManaged(false); //Non occupa spazio finché non è visibile
        messaggioErrore.setTextAlignment(TextAlignment.CENTER);
        messaggioErrore.setWrappingWidth(320); //Per mandare il testo a capo e non farlo uscire
        messaggioErrore.setVisible(false);

        btnPubblica.setOnAction(e -> {
            areaIndirizzo.getStyleClass().remove("campo-errore");
            comboCategorie.getStyleClass().remove("campo-errore");
            messaggioErrore.setVisible(false);
            messaggioErrore.setManaged(false);

            if (areaIndirizzo.getText() == "") {
                btnPubblica.setStyle("-fx-background-color: red");
                areaIndirizzo.getStyleClass().add("campo-errore");
                messaggioErrore.setVisible(true);
                messaggioErrore.setManaged(true);
                messaggioErrore.setText("L'indirizzo del posto vuoi tenertelo solo per te?");
                return;
            }
            if (comboCategorie.getValue() == null) {
                btnPubblica.setStyle("-fx-background-color: red");
                comboCategorie.getStyleClass().add("campo-errore");
                messaggioErrore.setVisible(true);
                messaggioErrore.setManaged(true);
                messaggioErrore.setText("Ehmmm... hai dimenticato di selezionare la categoria!");
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
            Recensione r = new Recensione(Sessione.getUtente().getUsername(), comboCategorie.getValue(), (int) sliderStelle.getValue(), areaIndirizzo.getText(), areaCommento.getText(), frasiSelezionate);

            FileRecensioni.salvaRecensione(r);
            onAggiornaHome.run(); //Ricarica home
            chiudi();
        });

        //Scrollpane per checkbox
        ScrollPane scrollFrasi = new ScrollPane(containerCheckBox);
        scrollFrasi.setFitToWidth(true);
        scrollFrasi.setPrefHeight(150);
        scrollFrasi.setStyle("-fx-background-color: transparent;");

        card.getChildren().addAll(header, comboCategorie, new Label("Scegli le frasi adatte:"), scrollFrasi, lblStelle, sliderStelle,areaIndirizzo, areaCommento, messaggioErrore, btnPubblica);
        this.getChildren().add(card);
    }

    private void chiudi() {
        //Si autoelimina
        if (this.getParent() instanceof Pane genitore) {
            genitore.getChildren().remove(this);
        }
    }
}