package com.condominio.controller.financeiro;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class FinanceiroController {

    @FXML
    private void abrirGerarBoleto(ActionEvent event) {
        try {
            URL url = getClass().getResource("/com/condominio/financeiro/gerar-boleto-view.fxml");
            if (url != null) {
                Parent root = FXMLLoader.load(url);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("CondoSoft - Gerar Boleto");
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirTelaPagamentos(ActionEvent event) {
        try {
            URL url = getClass().getResource("/com/condominio/financeiro/pagamentos-view.fxml");
            if (url != null) {
                Parent root = FXMLLoader.load(url);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("CondoSoft - Histórico de Pagamentos");
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirInadimplencia(ActionEvent event) {
        try {
            URL url = getClass().getResource("/com/condominio/financeiro/inadimplencia-view.fxml");
            if (url != null) {
                Parent root = FXMLLoader.load(url);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("CondoSoft - Inadimplência");
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void voltarHome(ActionEvent event) {
        String[] rotasHome = {
                "/com/condominio/home-view.fxml",
                "/com/condominio/unidade/home-view.fxml",
                "/home-view.fxml"
        };

        URL urlHome = null;
        for (String caminho : rotasHome) {
            urlHome = getClass().getResource(caminho);
            if (urlHome != null) break;
        }

        try {
            if (urlHome == null) return;
            Parent root = FXMLLoader.load(urlHome);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CondoSoft - Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}