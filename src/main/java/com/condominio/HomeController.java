package com.condominio;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class HomeController {

    @FXML
    private Label lblDataAtual;

    @FXML
    public void initialize() {
        // ... seu código existente (combos, tabelas, etc.) ...

        // Formata a data por extenso em português (ex: quinta-feira, 28 de maio de 2026)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));

        if (lblDataAtual != null) {
            lblDataAtual.setText(LocalDate.now().format(formatter));
        }
    }

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


    public void abrirReservas(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource(
                        "/com/condominio/reserva/lista-reserva-view.fxml"
                )
        );

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    public void abrirFinanceiro(ActionEvent event) throws IOException {
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


    public void abrirRelatorios(ActionEvent event) throws IOException {
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

    public void abrirPortaria(ActionEvent event) throws IOException {
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

    public void abrirComunicacao(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(
                getClass().getResource(
                        "/com/condominio/comunicacao/comunicacao-view.fxml"
                )
        );

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    public void abrirDisciplina(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource(
                        "/com/condominio/disciplina/disciplina-view.fxml"
                )
        );

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(root));
        stage.show();

    }
}