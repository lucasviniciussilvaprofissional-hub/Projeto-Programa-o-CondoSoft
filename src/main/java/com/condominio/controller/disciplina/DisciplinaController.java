package com.condominio.controller.disciplina;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

public class DisciplinaController {

    @FXML
    private Button btnVoltarHome;

    @FXML
    private Button btnRegistrarOcorrencia;

    @FXML
    private ComboBox<String> cmbUnidade;

    @FXML
    private ComboBox<String> cmbTipoOcorrencia;

    @FXML
    private TextField txtMorador;

    @FXML
    private TextArea txtDescricao;

    @FXML
    private Label lblTotalOcorrencias;

    @FXML
    private Label lblAdvertencias;

    @FXML
    private Label lblMultas;

    @FXML
    private TableView<?> tabelaOcorrencias;

    @FXML
    private TableColumn<?, ?> colUnidade;

    @FXML
    private TableColumn<?, ?> colMorador;

    @FXML
    private TableColumn<?, ?> colTipo;

    @FXML
    private TableColumn<?, ?> colData;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    public void initialize() {

        cmbTipoOcorrencia.getItems().addAll(
                "Advertência",
                "Barulho Excessivo",
                "Uso Indevido de Área Comum",
                "Infração de Regimento",
                "Multa"
        );

    }

    @FXML
    private void voltarHome(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource(
                        "/com/condominio/home-view.fxml"
                )
        );

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(root));
        stage.show();

    }

    @FXML
    private void registrarOcorrencia() {

    }

    @FXML
    private void limparFormulario() {

        txtMorador.clear();
        txtDescricao.clear();

        cmbUnidade.getSelectionModel().clearSelection();
        cmbTipoOcorrencia.getSelectionModel().clearSelection();

    }

    @FXML
    private void filtrarOcorrencias() {

    }

    @FXML
    private void visualizarOcorrencia() {

    }

    @FXML
    private void aplicarMulta() {

    }

}