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

public class ControleAcessoController {

    private static final List<AcessoAuxiliar> listaMock = new ArrayList<>();

    @FXML private TextField txtNome;
    @FXML private TextField txtDocumento;
    @FXML private TextField txtDestino;
    @FXML private ComboBox<String> cbTipo;

    @FXML private TableView<AcessoAuxiliar> tabelaAcessos;
    @FXML private TableColumn<AcessoAuxiliar, String> colNome;
    @FXML private TableColumn<AcessoAuxiliar, String> colDocumento;
    @FXML private TableColumn<AcessoAuxiliar, String> colDestino;
    @FXML private TableColumn<AcessoAuxiliar, String> colTipo;
    @FXML private TableColumn<AcessoAuxiliar, String> colStatus;

    @FXML
    public void initialize() {
        cbTipo.setItems(FXCollections.observableArrayList("Visitante", "Prestador de Serviço", "Mudança"));
        cbTipo.setValue("Visitante");

        colNome.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNome()));
        colDocumento.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDocumento()));
        colDestino.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDestino()));
        colTipo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTipo()));
        colStatus.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        atualizarTabela();
    }

    @FXML
    private void salvarAcesso() {
        String nome = txtNome.getText();
        String doc = txtDocumento.getText();
        String destino = txtDestino.getText();
        String tipo = cbTipo.getValue();

        if (nome == null || nome.isBlank() || doc == null || doc.isBlank() || destino == null || destino.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Campos Incompletos");
            alert.showAndWait();
            return;
        }

        listaMock.add(new AcessoAuxiliar(nome, doc, destino, tipo, "Dentro"));
        txtNome.clear();
        txtDocumento.clear();
        txtDestino.clear();
        cbTipo.setValue("Visitante");
        atualizarTabela();
    }

    @FXML
    private void registrarSaida() {
        AcessoAuxiliar selecionado = tabelaAcessos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            selecionado.setStatus("Saiu");
            atualizarTabela();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Atenção");
            alert.setHeaderText("Nenhum registro selecionado");
            alert.setContentText("Por favor, selecione uma pessoa na tabela antes de registrar a saída.");
            alert.showAndWait();
        }
    }

    private void atualizarTabela() {
        tabelaAcessos.getItems().clear();
        tabelaAcessos.getItems().addAll(listaMock);
    }

    @FXML
    private void voltarPortaria(ActionEvent event) {
        System.out.println("[CondoSoft] Voltando para a Portaria...");
        try {
            URL fxmlUrl = ControleAcessoController.class.getResource("/com/condominio/portaria/portaria-view.fxml");

            if (fxmlUrl == null) {
                fxmlUrl = ControleAcessoController.class.getClassLoader().getResource("com/condominio/portaria/portaria-view.fxml");
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

    public static class AcessoAuxiliar {
        private String nome;
        private String documento;
        private String destino;
        private String tipo;
        private String status;

        public AcessoAuxiliar(String nome, String documento, String destino, String tipo, String status) {
            this.nome = nome;
            this.documento = documento;
            this.destino = destino;
            this.tipo = tipo;
            this.status = status;
        }

        public String getNome() { return nome; }
        public String getDocumento() { return documento; }
        public String getDestino() { return destino; }
        public String getTipo() { return tipo; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}