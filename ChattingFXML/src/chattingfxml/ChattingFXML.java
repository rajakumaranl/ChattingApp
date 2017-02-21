/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chattingfxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author rajakumaran
 */
public class ChattingFXML extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Welcome to Login Page");
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root,500,300);
        stage.setScene(scene);
        scene.getStylesheets().add(ChattingFXML.class.getResource("Login.css").toExternalForm());
        stage.show();
    }
 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //calling the launch method to show the login page
        launch(args);
    }
    
}
