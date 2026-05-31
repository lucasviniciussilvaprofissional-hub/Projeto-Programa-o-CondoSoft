package com.condominio.controller.portaria;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EncomendasController {

    private static final List<EncomendaAuxiliar> listaMock = new ArrayList<>();

    @FXML private TextField txtMorador;
    @FXML private TextField txtApartamento;

    @FXML private TableView<EncomendaAuxiliar> tabelaEncomendas;
    @FXML private TableColumn<EncomendaAuxiliar, String> colMorador;
    @FXML private TableColumn<EncomendaAuxiliar, String> colApartamento;
    @FXML private TableColumn<EncomendaAuxiliar, String> colStatus;

    @FXML
    public void initialize() {
        colMorador.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMorador())
        );
        colApartamento.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getApartamento())
        );
        colStatus.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus())
        );
        atualizarTabela();
    }

    @FXML
    private void salvarEncomenda() {
        String morador = txtMorador.getText();
        String apto = txtApartamento.getText();

        if (morador == null || morador.isBlank() || apto == null || apto.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Aviso");
            alert.showAndWait();
            return;
        }

        listaMock.add(new EncomendaAuxiliar(morador, apto, "Pendente"));
        txtMorador.clear();
        txtApartamento.clear();
        atualizarTabela();
    }

    @FXML
    private void marcarRetirado() {
        EncomendaAuxiliar selecionada = tabelaEncomendas.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            selecionada.setStatus("Retirado");
            atualizarTabela();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Atenção");
            alert.showAndWait();
        }
    }

    private void atualizarTabela() {
        tabelaEncomendas.getItems().clear();
        tabelaEncomendas.getItems().addAll(listaMock);
    }

    @FXML
    public void voltarPortaria(ActionEvent event) {
        System.out.println("[CondoSoft] Voltando para a Portaria...");
        try {
            URL fxmlUrl = EncomendasController.class.getResource("/com/condominio/portaria/portaria-view.fxml");

            if (fxmlUrl == null) {
                fxmlUrl = EncomendasController.class.getClassLoader().getResource("com/condominio/portaria/portaria-view.fxml");
            }

            if (fxmlUrl == null) {
                throw new IOException("Não foi possível localizar o arquivo 'portaria-view.fxml'.");
            }

            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("[ERRO] Erro ao retornar para a portaria: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static class EncomendaAuxiliar {
        private String morador;
        private String apartamento;
        private String status;

        public EncomendaAuxiliar(String morador, String apartamento, String status) {
            this.morador = morador;
            this.apartamento = apartamento;
            this.status = status;
        }

        public String getMorador() { return morador; }
        public String getApartamento() { return apartamento; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}