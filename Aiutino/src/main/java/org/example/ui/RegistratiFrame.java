package org.example.ui;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.example.model.Utente;
import org.example.service.Auth;

public class RegistratiFrame extends StackPane {

    public RegistratiFrame(){
        //Card login
        VBox card = new VBox(20);
        card.getStyleClass().add("registrati-card");
        card.setMaxSize(400,500);
        card.setAlignment(Pos.CENTER);

        //Titolo pagina
        Label titolo = new Label("Registrati!");
        titolo.getStyleClass().add("registrati-titolo");

        //Input
        TextField username = new TextField();
        username.setPromptText("Username");
        username.getStyleClass().add("campo-input");
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            username.getStyleClass().remove("campo-errore"); //Se il campo era rosso, quando l'utente inizia a scrivere tornerà normale
        });

        TextField email = new TextField();
        email.setPromptText("Email");
        email.getStyleClass().add("campo-input");
        email.textProperty().addListener((observable, oldValue, newValue) -> {
            email.getStyleClass().remove("campo-errore"); //Se il campo era rosso, quando l'utente inizia a scrivere tornerà normale
        });

        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.getStyleClass().add("campo-input");
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            password.getStyleClass().remove("campo-errore"); //Se il campo era rosso, quando l'utente inizia a scrivere tornerà normale
        });

        //Scritta in caso di errori
        Text messaggioErrore = new Text("");
        messaggioErrore.getStyleClass().add("scritta-errore");
        messaggioErrore.setManaged(false); //Non occupa spazio finché non è visibile
        messaggioErrore.setTextAlignment(TextAlignment.CENTER);
        messaggioErrore.setWrappingWidth(320); //Per mandare il testo a capo e non farlo uscire
        messaggioErrore.setVisible(false);

        //Bottoni
        Button btnRegistrati = new Button("Registrati");
        btnRegistrati.getStyleClass().add("btnRegistrati");
        btnRegistrati.setMaxWidth(Double.MAX_VALUE);
        btnRegistrati.setOnAction(e ->{
            password.getStyleClass().remove("campo-errore");
            email.getStyleClass().remove("campo-errore");
            username.getStyleClass().remove("campo-errore");
            messaggioErrore.setVisible(false);
            messaggioErrore.setManaged(false);

            Auth auth = new Auth();
            int codice = auth.controlloErrori(username.getText(), email.getText(), password.getText(), true);
            if (codice != 0){
                btnRegistrati.setStyle("-fx-background-color: red");
                messaggioErrore.setVisible(true);
                messaggioErrore.setManaged(true);

                switch (codice){
                    case 1 :{
                        messaggioErrore.setText("Username già preso. Sii più creativo, non è difficile.");
                        username.getStyleClass().add("campo-errore");
                        break;
                    }
                    case 2 :{
                        messaggioErrore.setText("L'email è già stata usata. Inizi a soffrire di alzheimer?");
                        email.getStyleClass().add("campo-errore");
                        break;
                    }
                    case 3 :{
                        messaggioErrore.setText("Uno o più campi sono vuoti. Qualcuno qua ha bisogno degli occhiali...");
                        break;
                    }
                    case 4 :{
                        messaggioErrore.setText("L'email è scritta male. Ti serve un disegnino?");
                        email.getStyleClass().add("campo-errore");
                        break;
                    }
                    case 5 :{
                        messaggioErrore.setText("Password troppo corta. Proprio come la tua pazienza.");
                        password.getStyleClass().add("campo-errore");
                        break;
                    }
                    default:{
                        messaggioErrore.setText("Qualcosa è andato storto, ma non ho voglia di spiegarti cosa.");
                    }
                }
            }
            else if(codice == 0){
                messaggioErrore.setVisible(false);
                messaggioErrore.setManaged(false);

                //Otp
                String otp = Auth.generaOtp();
                System.out.println("Debug --> Otp: " + otp);

                //Invio email
                auth.sendMail(email.getText(), otp);

                //Cambio scena
                this.getScene().setRoot(new OtpFrame(auth.fornisciUtente(), otp, true));
            }
        });

        //Separatore
        Label separatore = new Label("Oppure");
        separatore.setStyle("-fx-text-fill: #7f8c8d;");

        /*Bottone Google
        ImageView iconaGoogle = new ImageView(new Image(getClass().getResourceAsStream("/icona-google.png"))); //Prende immagine da cartella resource
        iconaGoogle.setFitHeight(20); //Altezza icona
        iconaGoogle.setPreserveRatio(true); //Mantiene proporzioni
        Button btnGoogle = new Button("Accedi con Google");
        btnGoogle.setGraphic(iconaGoogle); //Aggiunge icona nel bottone
        btnGoogle.setGraphicTextGap(10);  //Spazio tra icona e testo
        btnGoogle.getStyleClass().add("btnGoogle");
        btnGoogle.setMaxWidth(Double.MAX_VALUE);*/

        //Altri bottoni
        HBox bottoniAggiuntivi = new HBox(15);
        bottoniAggiuntivi.setAlignment(Pos.CENTER);

        Button btnAccedi = new Button("Accedi");
        btnAccedi.getStyleClass().add("btnAltro");
        btnAccedi.setOnAction(e ->{
            this.getScene().setRoot(new LoginFrame());
        });

        Button btnHome = new Button("Home");
        btnHome.getStyleClass().add("btnAltro");
        btnHome.setOnAction(e ->{
            this.getScene().setRoot(new HomeFrame());
        });

        //Aggiunta pulsanti secondari in HBox
        bottoniAggiuntivi.getChildren().addAll(btnAccedi, btnHome);

        //Aggiungere tutto alla card principale
        card.getChildren().addAll(titolo, username, email, password, messaggioErrore, btnRegistrati, separatore, /*btnGoogle,*/ bottoniAggiuntivi);

        //Aggiungere a StackPane
        this.getChildren().add(card);
    }
}
