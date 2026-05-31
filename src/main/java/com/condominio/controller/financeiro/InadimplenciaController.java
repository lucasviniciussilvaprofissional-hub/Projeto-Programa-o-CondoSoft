package com.condominio.controller.financeiro;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class InadimplenciaController {

    @FXML
    public void initialize() {
        System.out.println("[CondoSoft] Tela de Inadimplência inicializada.");
    }

    /**
     * BOTÃO VOLTAR: Rota inteligente para encontrar a sua Home
     */
    @FXML
    private void voltarHome(ActionEvent event) {
        System.out.println("[CondoSoft] Solicitando retorno para a Home...");

        String[] caminhosHome = {
                "/com/condominio/home-view.fxml",
                "/com/condominio/unidade/home-view.fxml",
                "/home-view.fxml"
        };

        URL urlHome = null;
        for (String caminho : caminhosHome) {
            urlHome = getClass().getResource(caminho);
            if (urlHome != null) break;
        }

        try {
            if (urlHome == null) {
                throw new IOException("O arquivo 'home-view.fxml' não foi encontrado.");
            }

            Parent root = FXMLLoader.load(urlHome);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CondoSoft - Home");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Navegação");
            alert.setHeaderText("Não foi possível voltar ao início");
            alert.setContentText("Detalhes: " + e.getMessage());
            alert.showAndWait();
        }
    }
}