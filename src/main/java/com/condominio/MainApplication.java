package com.condominio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/com/condominio/home-view.fxml"
                )
        );

        Scene scene = new Scene(
                loader.load(),
                1200,
                750
        );

        stage.setTitle("CondoSoft");
        stage.setMinWidth(1000);
        stage.setMinHeight(650);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}