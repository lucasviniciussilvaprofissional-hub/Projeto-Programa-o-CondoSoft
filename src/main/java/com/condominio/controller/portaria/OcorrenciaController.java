package com.condominio.controller.portaria;

import javafx.collections.FXCollections;
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

public class OcorrenciaController {

    private static final List<OcorrenciaAuxiliar> listaMock = new ArrayList<>();

    @FXML private TextField txtUnidade;
    @FXML private ComboBox<String> cbTipo;
    @FXML private TextField txtTitulo;
    @FXML private TextArea txtDescricao;

    @FXML private TableView<OcorrenciaAuxiliar> tabelaOcorrencias;
    @FXML private TableColumn<OcorrenciaAuxiliar, String> colUnidade;
    @FXML private TableColumn<OcorrenciaAuxiliar, String> colTipo;
    @FXML private TableColumn<OcorrenciaAuxiliar, String> colTitulo;
    @FXML private TableColumn<OcorrenciaAuxiliar, String> colDescricao;
    @FXML private TableColumn<OcorrenciaAuxiliar, String> colStatus;

    @FXML
    public void initialize() {
        cbTipo.setItems(FXCollections.observableArrayList("Barulho", "Infraestrutura", "Mudança", "Conflito", "Outros"));
        cbTipo.setValue("Barulho");

        colUnidade.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getUnidade()));
        colTipo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTipo()));
        colTitulo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitulo()));
        colDescricao.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescricao()));
        colStatus.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        atualizarTabela();
    }

    @FXML
    private void salvarOcorrencia() {
        String unidade = txtUnidade.getText();
        String tipo = cbTipo.getValue();
        String titulo = txtTitulo.getText();
        String descricao = txtDescricao.getText();

        if (unidade == null || unidade.isBlank() || titulo == null || titulo.isBlank() || descricao == null || descricao.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Campos Vazios");
            alert.showAndWait();
            return;
        }

        listaMock.add(new OcorrenciaAuxiliar(unidade, tipo, titulo, descricao, "Pendente"));
        txtUnidade.clear();
        txtTitulo.clear();
        txtDescricao.clear();
        cbTipo.setValue("Barulho");
        atualizarTabela();
    }

    @FXML
    private void resolverOcorrencia() {
        OcorrenciaAuxiliar selecionada = tabelaOcorrencias.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            selecionada.setStatus("Resolvida");
            atualizarTabela();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Atenção");
            alert.showAndWait();
        }
    }

    private void atualizarTabela() {
        tabelaOcorrencias.getItems().clear();
        tabelaOcorrencias.getItems().addAll(listaMock);
    }

    @FXML
    private void voltarPortaria(ActionEvent event) {
        System.out.println("[CondoSoft] Voltando para a Portaria...");
        try {
            URL fxmlUrl = OcorrenciaController.class.getResource("/com/condominio/portaria/portaria-view.fxml");

            if (fxmlUrl == null) {
                fxmlUrl = OcorrenciaController.class.getClassLoader().getResource("com/condominio/portaria/portaria-view.fxml");
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

    public static class OcorrenciaAuxiliar {
        private String unidade;
        private String tipo;
        private String titulo;
        private String descricao;
        private String status;

        public OcorrenciaAuxiliar(String unidade, String tipo, String titulo, String descricao, String status) {
            this.unidade = unidade;
            this.tipo = tipo;
            this.titulo = titulo;
            this.descricao = descricao;
            this.status = status;
        }

        public String getUnidade() { return unidade; }
        public String getTipo() { return tipo; }
        public String getTitulo() { return titulo; }
        public String getDescricao() { return descricao; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}