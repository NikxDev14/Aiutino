package org.example;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.model.Utente;
import org.example.service.EnvLoader;
import org.example.service.Sessione;
import org.example.ui.HomeFrame;

import java.io.IOException;

public class Main extends Application{
    @Override
    public void start(Stage mainStage){
        HomeFrame home = new HomeFrame();
        Scene scena = new Scene(home, 1000, 700);

        //Caricamento del CSS
        String css = getClass().getResource("/style.css").toExternalForm();
        scena.getStylesheets().add(css);

        mainStage.setTitle("Aiutino?");
        mainStage.setScene(scena);
        mainStage.show();

        try{
            EnvLoader.load();
        } catch (IOException e) {
            System.out.println("Errore caricamento .env");
            throw new RuntimeException(e);
        }

    }
    public static void main(String[] args) {
        launch(args);
    }
}
