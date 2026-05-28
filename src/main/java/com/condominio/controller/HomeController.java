package com.condominio.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    public void abrirUnidades(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(
                HomeController.class.getResource(
                        "/com/condominio/unidade-view.fxml"
                )
        );

        Stage stage = (Stage)
                ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    public void abrirMoradores(ActionEvent event) {}

    public void abrirReservas(ActionEvent event) {}

    public void abrirFinanceiro(ActionEvent event) {}

    public void abrirRelatorios(ActionEvent event) {}

    public void abrirPortaria(ActionEvent event) {}

    public void abrirComunicacao(ActionEvent event) {}

    public void abrirDisciplina(ActionEvent event) {}
}