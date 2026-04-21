package org.example.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class OtpFrame extends StackPane {

    public OtpFrame(String emailUtente, String otp){
        VBox card = new VBox(20);
        card.getStyleClass().add("otp-card");
        card.setMaxSize(400,500);
        card.setAlignment(Pos.CENTER);

        //Titolo pagina
        Label titolo = new Label("Controlla la tua casella postale!");
        titolo.getStyleClass().add("otp-titolo");

        Label istruzioni = new Label("Abbiamo inviato un codice a: " + emailUtente);
        istruzioni.setStyle("-fx-text-alignment: center; -fx-text-fill: #7f8c8d;");
        istruzioni.setWrapText(true);

        //Input
        TextField campoOtp = new TextField();
        campoOtp.setPromptText("000000");
        campoOtp.getStyleClass().add("campo-input");

        //Bottoni
        Button btnVerifica = new Button("Verifica");
        btnVerifica.getStyleClass().add("btnVerifica");
        btnVerifica.setMaxWidth(Double.MAX_VALUE);

        //Separatore
        Label separatore = new Label("Oppure");
        separatore.setStyle("-fx-text-fill: #7f8c8d;");

        //Altri bottoni
        HBox bottoniAggiuntivi = new HBox(15);
        bottoniAggiuntivi.setAlignment(Pos.CENTER);

        Button btnRegistrati = new Button("Registrati");
        btnRegistrati.getStyleClass().add("btnAltro");
        btnRegistrati.setOnAction(e ->{
            this.getScene().setRoot(new RegistratiFrame());
        });

        Button btnHome = new Button("Home");
        btnHome.getStyleClass().add("btnAltro");
        btnHome.setOnAction(e ->{
            this.getScene().setRoot(new HomeFrame());
        });

        //Aggiunta pulsanti secondari in HBox
        bottoniAggiuntivi.getChildren().addAll(btnRegistrati, btnHome);

        //Aggiungere tutto alla card principale
        card.getChildren().addAll(titolo, istruzioni, campoOtp, btnVerifica, separatore, bottoniAggiuntivi);

        //Aggiungere a StackPane
        this.getChildren().add(card);
    }
}
