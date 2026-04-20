package org.example.ui;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class LoginFrame extends StackPane {

    public LoginFrame(){
        //Card login
        VBox card = new VBox(20);
        card.getStyleClass().add("login-card");
        card.setMaxSize(400,500);
        card.setAlignment(Pos.CENTER);

        //Titolo pagina
        Label titolo = new Label("Bentornato/a!");
        titolo.getStyleClass().add("login-titolo");

        //Input
        TextField email = new TextField();
        email.setPromptText("Email");
        email.getStyleClass().add("campo-input");

        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.getStyleClass().add("campo-input");

        //Bottoni
        Button btnAccedi = new Button("Accedi");
        btnAccedi.getStyleClass().add("btnAccedi");
        btnAccedi.setMaxWidth(Double.MAX_VALUE);

        //Separatore
        Label separatore = new Label("Oppure");
        separatore.setStyle("-fx-text-fill: #7f8c8d;");

        //Bottone Google
        ImageView iconaGoogle = new ImageView(new Image(getClass().getResourceAsStream("/icona-google.png"))); //Prende immagine da cartella resource
        iconaGoogle.setFitHeight(20); //Altezza icona
        iconaGoogle.setPreserveRatio(true); //Mantiene proporzioni
        Button btnGoogle = new Button("Accedi con Google");
        btnGoogle.setGraphic(iconaGoogle); //Aggiunge icona nel bottone
        btnGoogle.setGraphicTextGap(10);  //Spazio tra icona e testo
        btnGoogle.getStyleClass().add("btnGoogle");
        btnGoogle.setMaxWidth(Double.MAX_VALUE);

        //Altri bottoni
        HBox bottoniAggiuntivi = new HBox(15);
        bottoniAggiuntivi.setAlignment(Pos.CENTER);

        Button btnRegistrati = new Button("Registrati");
        btnRegistrati.getStyleClass().add("btnAltro");

        Button btnHome = new Button("Home");
        btnHome.getStyleClass().add("btnAltro");
        btnHome.setOnAction(e ->{
            HomeFrame home = new HomeFrame();
            this.getScene().setRoot(home);
        });

        //Aggiunta pulsanti secondari in HBox
        bottoniAggiuntivi.getChildren().addAll(btnRegistrati, btnHome);

        //Aggiungere tutto alla card principale
        card.getChildren().addAll(titolo, email, password, btnAccedi, separatore, btnGoogle, bottoniAggiuntivi);

        //Aggiungere a StackPane
        this.getChildren().add(card);
    }
}
