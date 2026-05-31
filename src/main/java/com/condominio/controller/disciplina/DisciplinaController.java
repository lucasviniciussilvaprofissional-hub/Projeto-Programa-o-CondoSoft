package com.condominio.controller.disciplina;

import com.condominio.models.disciplina.Ocorrencia;
import com.condominio.repository.implementation.OcorrenciaRepositoryImpl;
import com.condominio.repository.interfaces.IOcorrenciaRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class DisciplinaController {

    private final IOcorrenciaRepository repository = new OcorrenciaRepositoryImpl();

    @FXML private Button btnVoltarHome;
    @FXML private Button btnRegistrarOcorrencia;
    @FXML private ComboBox<String> cmbUnidade;
    @FXML private ComboBox<String> cmbTipoOcorrencia;
    @FXML private TextField txtMorador;
    @FXML private TextArea txtDescricao;
    @FXML private Label lblTotalOcorrencias;
    @FXML private Label lblAdvertencias;
    @FXML private Label lblMultas;

    @FXML private TableView<Ocorrencia> tabelaOcorrencias;
    @FXML private TableColumn<Ocorrencia, String> colUnidade;
    @FXML private TableColumn<Ocorrencia, String> colMorador;
    @FXML private TableColumn<Ocorrencia, String> colTipo;
    @FXML private TableColumn<Ocorrencia, String> colData;
    @FXML private TableColumn<Ocorrencia, String> colStatus;

    @FXML
    public void initialize() {
        // Popula as ComboBoxes
        cmbUnidade.getItems().addAll("Apto 101", "Apto 102", "Apto 201", "Apto 202", "Bloco A - 103", "Casa 01", "Casa 02");
        cmbTipoOcorrencia.getItems().addAll("Advertência", "Barulho Excessive", "Uso Indevido de Área Comum", "Infração de Regimento", "Multa");

        atualizarContadores();

        // MAPEAMENTO ROBUSTO USANDO LAMBDAS (Solução definitiva do erro)
        colUnidade.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNomeUnidadeFormatado())
        );

        colMorador.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNomeMoradorFormatado())
        );

        colTipo.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTipo() != null ? cellData.getValue().getTipo().toString() : "")
        );

        colStatus.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus() != null ? cellData.getValue().getStatus().toString() : "")
        );

        colData.setCellValueFactory(cellData -> {
            var data = cellData.getValue().getDataCriacao();
            if (data != null) {
                return new javafx.beans.property.SimpleStringProperty(data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });

        atualizarTabela();
    }

    @FXML
    private void registrarOcorrencia() {
        String unidade = cmbUnidade.getValue();
        String tipo = cmbTipoOcorrencia.getValue();
        String morador = txtMorador.getText();
        String descricao = txtDescricao.getText();

        if (unidade == null || tipo == null || morador.isBlank() || descricao.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos obrigatórios");
            alert.setHeaderText(null);
            alert.setContentText("Preencha todos os campos.");
            alert.showAndWait();
            return;
        }

        Ocorrencia ocorrencia = new Ocorrencia(unidade, morador, tipo, descricao);
        repository.salvar(ocorrencia);

        atualizarTabela();
        atualizarContadores();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText("Ocorrência registrada com sucesso!");
        alert.showAndWait();

        limparFormulario();
    }

    private void atualizarTabela() {
        tabelaOcorrencias.getItems().clear();
        tabelaOcorrencias.getItems().addAll(repository.listar());
    }

    private void atualizarContadores() {
        int total = repository.listar().size();
        int advertencias = 0;
        int multas = 0;

        for (Ocorrencia o : repository.listar()) {
            if (o.getTipo() != null) {
                String nomeEnum = o.getTipo().name().toLowerCase();
                if (nomeEnum.contains("advert")) {
                    advertencias++;
                }
                if (nomeEnum.contains("multa")) {
                    multas++;
                }
            }
        }

        lblTotalOcorrencias.setText(String.valueOf(total));
        lblAdvertencias.setText(String.valueOf(advertencias));
        lblMultas.setText(String.valueOf(multas));
    }

    @FXML
    private void limparFormulario() {
        txtMorador.clear();
        txtDescricao.clear();
        cmbUnidade.getSelectionModel().clearSelection();
        cmbTipoOcorrencia.getSelectionModel().clearSelection();
    }

    @FXML
    private void voltarHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/condominio/home-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML private void filtrarOcorrencias() {}
    @FXML private void visualizarOcorrencia() {}
    @FXML private void aplicarMulta() {}
}