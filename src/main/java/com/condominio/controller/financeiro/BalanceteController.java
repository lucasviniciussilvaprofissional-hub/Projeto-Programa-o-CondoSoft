package com.condominio.controller.financeiro;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class BalanceteController {

    @FXML private ComboBox<String> cbMes;
    @FXML private ComboBox<String> cbAno;

    @FXML
    public void initialize() {
        System.out.println("[CondoSoft] Inicializando seletores do Balancete...");
        if (cbMes != null) {
            cbMes.setItems(FXCollections.observableArrayList(
                    "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
                    "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
            ));
            cbMes.setValue("Maio");
        }
        if (cbAno != null) {
            cbAno.setItems(FXCollections.observableArrayList(
                    "2024", "2025", "2026", "2027"
            ));
            cbAno.setValue("2026");
        }
    }

    // Vinculado ao botão "Gerar Balancete" mapeando as duas grafias possíveis
    @FXML
    private void gerarBalancete(ActionEvent event) {
        System.out.println("[CondoSoft] Gerando relatório para o período selecionado...");
    }

    @FXML
    private void generarBalancete(ActionEvent event) {
        System.out.println("[CondoSoft] Gerando relatório para o período selecionado...");
    }

    /**
     * BOTÃO VOLTAR: Corrigido para carregar "relatorio-view.fxml" na pasta "relatorio"
     */
    @FXML
    private void voltarRelatorios(ActionEvent event) {
        System.out.println("[CondoSoft] Solicitando retorno para o painel de relatórios...");

        // Caminho corrigido com base na sua árvore de ficheiros real!
        String rotaFxml = "/com/condominio/relatorio/relatorio-view.fxml";

        URL fxmlUrl = getClass().getResource(rotaFxml);
        if (fxmlUrl == null) {
            fxmlUrl = BalanceteController.class.getResource(rotaFxml);
        }
        if (fxmlUrl == null) {
            fxmlUrl = Thread.currentThread().getContextClassLoader().getResource(rotaFxml);
        }

        try {
            if (fxmlUrl == null) {
                throw new IOException("O ficheiro 'relatorio-view.fxml' não foi encontrado em: resources" + rotaFxml);
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CondoSoft - Central de Relatórios");
            stage.show();

        } catch (Exception e) {
            System.out.println("[ERRO] Erro ao carregar a tela anterior.");
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Rota");
            alert.setHeaderText("Não foi possível carregar a tela anterior");
            alert.setContentText("Mensagem: " + e.getMessage());
            alert.showAndWait();
        }
    }
}