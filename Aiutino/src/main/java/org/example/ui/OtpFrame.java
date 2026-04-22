package org.example.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.example.model.Utente;
import org.example.service.Auth;
import org.example.service.FileUtenti;
import org.example.service.Sessione;

public class OtpFrame extends StackPane {

    public OtpFrame(Utente utente, String otp, boolean registrazione){
        VBox card = new VBox(20);
        card.getStyleClass().add("otp-card");
        card.setMaxSize(400,500);
        card.setAlignment(Pos.CENTER);

        //Titolo pagina
        Label titolo = new Label("Controlla la tua casella postale!");
        titolo.getStyleClass().add("otp-titolo");

        Label istruzioni = new Label("Abbiamo inviato un codice a: " + utente.getEmail());
        istruzioni.setStyle("-fx-text-alignment: center; -fx-text-fill: #7f8c8d;");
        istruzioni.setWrapText(true);

        //Input
        TextField campoOtp = new TextField();
        campoOtp.setPromptText("000000");
        campoOtp.getStyleClass().add("campo-input");

        //Scritta in caso di errori
        Text messaggioErrore = new Text("");
        messaggioErrore.getStyleClass().add("scritta-errore");
        messaggioErrore.setManaged(false); //Non occupa spazio finché non è visibile
        messaggioErrore.setTextAlignment(TextAlignment.CENTER);
        messaggioErrore.setWrappingWidth(320); //Per mandare il testo a capo e non farlo uscire
        messaggioErrore.setVisible(false);

        //Bottoni
        Button btnVerifica = new Button("Verifica");
        btnVerifica.getStyleClass().add("btnVerifica");
        btnVerifica.setMaxWidth(Double.MAX_VALUE);
        btnVerifica.setOnAction(e ->{

            campoOtp.getStyleClass().remove("campo-errore");
            messaggioErrore.setVisible(false);
            messaggioErrore.setManaged(false);

            if (Auth.verificaOtp(otp, campoOtp.getText())){
                if (registrazione){
                    FileUtenti.salvaUtente(utente.getEmail(), utente.getPassword(), utente.getUsername());
                    this.getScene().setRoot(new LoginFrame());
                }
                else {
                    Sessione.login(utente);
                    this.getScene().setRoot(new HomeFrame());
                }
            }
            else {
                campoOtp.getStyleClass().add("campo-errore");
                btnVerifica.setStyle("-fx-background-color: red");
                messaggioErrore.setVisible(true);
                messaggioErrore.setManaged(true);
                messaggioErrore.setText("Qualcuno qua non è in grado di copiare dei numeri...");
            }
        });

        //Separatore
        Label separatore = new Label("Oppure");
        separatore.setStyle("-fx-text-fill: #7f8c8d;");

        //Altri bottoni
        HBox bottoniAggiuntivi = new HBox(15);
        bottoniAggiuntivi.setAlignment(Pos.CENTER);

        Button btnHome = new Button("Home");
        btnHome.getStyleClass().add("btnAltro");
        btnHome.setOnAction(e ->{
            this.getScene().setRoot(new HomeFrame());
        });

        //Aggiunta pulsanti secondari in HBox
        bottoniAggiuntivi.getChildren().addAll(btnHome);

        //Aggiungere tutto alla card principale
        card.getChildren().addAll(titolo, istruzioni, campoOtp, messaggioErrore, btnVerifica, separatore, bottoniAggiuntivi);

        //Aggiungere a StackPane
        this.getChildren().add(card);
    }
}
