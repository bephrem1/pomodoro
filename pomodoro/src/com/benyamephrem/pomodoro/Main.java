package com.benyamephrem.pomodoro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the font
        Font.loadFont(getClass().getResource("/fonts/ProximaNova.ttf").toExternalForm(), 20);
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/home.fxml"));
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.setResizable(false); //User can't grab the edge of the window and resize application

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
