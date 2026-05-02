package org.example.ui;

import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.example.model.Utente;
import org.example.service.Auth;
import org.example.service.FileUtenti;
import org.example.service.GoogleAuthService;
import org.example.service.Sessione;

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

        //Scritta in caso di errori
        Text messaggioErrore = new Text("");
        messaggioErrore.getStyleClass().add("scritta-errore");
        messaggioErrore.setManaged(false); //Non occupa spazio finché non è visibile
        messaggioErrore.setTextAlignment(TextAlignment.CENTER);
        messaggioErrore.setWrappingWidth(320); //Per mandare il testo a capo e non farlo uscire
        messaggioErrore.setVisible(false);

        //Bottoni
        Button btnAccedi = new Button("Accedi");
        btnAccedi.setDefaultButton(true);
        btnAccedi.getStyleClass().add("btnAccedi");
        btnAccedi.setMaxWidth(Double.MAX_VALUE);
        btnAccedi.setOnAction(e ->{
            password.getStyleClass().remove("campo-errore");
            email.getStyleClass().remove("campo-errore");
            messaggioErrore.setVisible(false);
            messaggioErrore.setManaged(false);

            Auth auth = new Auth();
            int codice = auth.controlloErrori(null, email.getText(), password.getText(), false);
            if (codice != 0){
                btnAccedi.setStyle("-fx-background-color: red");
                messaggioErrore.setVisible(true);
                messaggioErrore.setManaged(true);

                switch (codice){
                    case 1 :{
                        messaggioErrore.setText("Uno o più campi sono vuoti. Qualcuno qua ha bisogno degli occhiali...");
                        break;
                    }
                    case 2 :{
                        messaggioErrore.setText("L'email non è stata trovata, ma ci sei mai stato qui?");
                        email.getStyleClass().add("campo-errore");
                        break;
                    }
                    case 3 :{
                        messaggioErrore.setText("Password errata, so che ti sei dimenticato dove hai messo il bigliettino con la password.");
                        password.getStyleClass().add("campo-errore");
                        break;
                    }
                    case 4 :{
                        messaggioErrore.setText("L'email è scritta male. Ti serve un disegnino?");
                        email.getStyleClass().add("campo-errore");
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

                Sessione.login(auth.fornisciUtente()); //Autentica utente
                this.getScene().setRoot(new HomeFrame()); //Cambia scena
            }
        });

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
        btnGoogle.setOnAction(e -> {
            new Thread(() -> {
                try {
                    //Recupero dati utente da Google
                    var googleInfo = GoogleAuthService.login();
                    String emailGoogle = googleInfo.getEmail();
                    String nomeGoogle = googleInfo.getName();

                    //Controllo esistenza utente
                    Utente utenteTrovato = FileUtenti.cercaPerEmail(emailGoogle);

                    if (utenteTrovato != null) {
                        //Utente esistente
                        System.out.println("Utente trovato! Accesso all'account: " + utenteTrovato.getUsername());
                        Platform.runLater(() -> {
                            Sessione.login(utenteTrovato);
                            this.getScene().setRoot(new HomeFrame());
                        });
                    } else {
                        //Utente non esistente
                        System.out.println("Nuovo utente. Generazione username...");
                        String usernameUnivoco = FileUtenti.generaUsernameUnico(nomeGoogle);

                        //Creazione user
                        Utente nuovoUtente = new Utente(usernameUnivoco, emailGoogle, "AUTH_GOOGLE");
                        FileUtenti.salvaUtente(nuovoUtente);

                        Platform.runLater(() -> {
                            Sessione.login(nuovoUtente);
                            this.getScene().setRoot(new HomeFrame());
                        });
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();
        });

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
        card.getChildren().addAll(titolo, email, password, messaggioErrore, btnAccedi, separatore, btnGoogle, bottoniAggiuntivi);

        //Aggiungere a StackPane
        this.getChildren().add(card);
    }
}
