package com.condominio;

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
                        "/com/condominio/unidade/unidade-view.fxml"
                )
        );

        Stage stage = (Stage)
                ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    public void abrirMoradores(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(
                HomeController.class.getResource(
                        "/com/condominio/unidade/listar-moradores-view.fxml"
                )
        );

        Stage stage = (Stage)
                ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }
    public void abrirReservas(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource(
                        "/com/condominio/reserva/reserva-view.fxml"
                )
        );

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    public void abrirFinanceiro(ActionEvent event) throws  IOException{
        Parent root = FXMLLoader.load(
            getClass().getResource(
                    "/com/condominio/financeiro/financeiro-view.fxml"
            )
    );

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }


    public void abrirRelatorios(ActionEvent event) throws  IOException{
        Parent root = FXMLLoader.load(
                getClass().getResource(
                        "/com/condominio/relatorio/relatorio-view.fxml"
                )
        );

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    public void abrirPortaria(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load(
                getClass().getResource(
                        "/com/condominio/portaria/portaria-view.fxml"
                )
        );

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    public void abrirComunicacao(ActionEvent event) {}

    public void abrirDisciplina(ActionEvent event) {}

}