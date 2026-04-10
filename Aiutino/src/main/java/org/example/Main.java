package org.example;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ui.HomeFrame;

public class Main extends Application{
    @Override
    public void start(Stage mainStage){
        HomeFrame home = new HomeFrame();
        Scene scena = new Scene(home, 1000, 700);
    }
    static void main() {

    }
}
