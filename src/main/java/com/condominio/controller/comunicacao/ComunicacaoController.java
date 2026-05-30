package com.condominio.controller.comunicacao;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ComunicacaoController {

    @FXML
    public void voltarInicio(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource(
                        "/com/condominio/home-view.fxml"
                )
        );

        Stage stage = (Stage)
                ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    public void novaMensagem(ActionEvent event) {

    }

    @FXML
    public void enviarComunicado(ActionEvent event) {

    }

    @FXML
    public void pesquisarMensagem(ActionEvent event) {

    }

    @FXML
    public void filtrarMensagens(ActionEvent event) {

    }
}