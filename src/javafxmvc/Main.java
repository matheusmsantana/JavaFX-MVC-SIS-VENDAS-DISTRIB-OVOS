package javafxmvc;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        //Carregando meu arquivo FXMLVBOXMain que esta no pacote view
        Parent root = FXMLLoader.load(getClass().getResource("view/FXMLVBoxMain.fxml"));
         //Instanciando a cena para que a tela possa aparecer
        Scene scene = new Scene(root);
        //setando a cena no stage
        stage.setScene(scene);
        //setando um titulo para tela principal
        stage.setTitle("Distribuidora de Ovos Pai & Filho");
        //bloqueando o redimencionamento da tela
        stage.setResizable(false);
        //mostrando a tela
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}