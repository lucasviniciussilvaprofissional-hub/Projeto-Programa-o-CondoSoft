package com.condominio.controller.unidade;

import com.condominio.enums.StatusUnidade;
import com.condominio.enums.TipoUnidade;
import com.condominio.models.moradia.Unidade;
import com.condominio.repository.implementation.MoradorRepositoryImpl;
import com.condominio.repository.implementation.UnidadeRepositoryImpl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class CadastrarUnidadeController {

    @FXML
    private TextField txtNumeroUnidade;

    @FXML
    private TextField txtBloco;

    @FXML
    private ComboBox<String> cmbTipo;

    @FXML
    private TextField txtCapacidade;

    @FXML
    private TextField txtAndar;

    @FXML
    private TextField txtArea;

    @FXML
    private ComboBox<String> cmbStatus;

    @FXML
    private TextField txtFracaoIdeal;

    @FXML
    private TextArea txtObservacoes;

    @FXML
    private ComboBox<String> cmbSituacaoFinanceira;

    private final UnidadeRepositoryImpl repository =
            new UnidadeRepositoryImpl();

    @FXML
    public void initialize() {

        cmbTipo.getItems().addAll(
                "APARTAMENTO",
                "CASA",
                "COBERTURA",
                "SALA_COMERCIAL"
        );

        cmbStatus.getItems().addAll(
                "OCUPADA",
                "DISPONIVEL",
                "INATIVA",
                "EM_MANUTENCAO"
        );

        cmbSituacaoFinanceira.getItems().addAll(
                "ADIMPLENTE",
                "INADIMPLENTE",
                "EM_NEGOCIACAO"
        );
    }

    // ==========================
    // NAVEGAÇÃO HEADER
    // ==========================

    public void voltarHome(ActionEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    public void voltarUnidades(ActionEvent event) {
        trocarTela(event, "/com/condominio/unidade/unidade-view.fxml");
    }

    // ==========================
    // BREADCRUMB
    // ==========================

    public void voltarHome(MouseEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    public void voltarUnidades(MouseEvent event) {
        trocarTela(event, "/com/condominio/unidade/unidade-view.fxml");
    }

    // ==========================
    // FORMULÁRIO
    // ==========================

    @FXML
    public void salvarUnidade(ActionEvent event) {

        try {

            int numero =
                    Integer.parseInt(txtNumeroUnidade.getText());

            String bloco =
                    txtBloco.getText();

            TipoUnidade tipo =
                    TipoUnidade.valueOf(cmbTipo.getValue());

            StatusUnidade status =
                    StatusUnidade.valueOf(cmbStatus.getValue());

            int capacidade =
                    Integer.parseInt(txtCapacidade.getText());

            double fracaoIdeal =
                    Double.parseDouble(txtFracaoIdeal.getText());

            String metragem =
                    txtArea.getText();

            String situacaoFinanceira =
                    cmbSituacaoFinanceira.getValue();

            Unidade unidade = new Unidade(
                    4,
                    bloco,
                    status,
                    fracaoIdeal,
                    numero,
                    tipo,
                    metragem,
                    capacidade, situacaoFinanceira
            );

            repository.salvar(unidade);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText(null);
            alert.setContentText("Unidade cadastrada com sucesso!");
            alert.showAndWait();

            limparCampos();

        } catch (Exception e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Verifique os dados informados.");
            alert.showAndWait();

            e.printStackTrace();
        }
    }

    @FXML
    public void limparFormulario(ActionEvent event) {
        limparCampos();
    }

    private void limparCampos() {

        txtNumeroUnidade.clear();
        txtBloco.clear();

        cmbTipo.setValue(null);

        txtCapacidade.clear();
        txtAndar.clear();
        txtArea.clear();

        cmbStatus.setValue(null);

        txtFracaoIdeal.clear();

        txtObservacoes.clear();
    }

    // ==========================
    // TROCA DE TELA
    // ==========================

    private void trocarTela(ActionEvent event, String caminhoFXML) {

        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource(caminhoFXML)
            );

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {

            System.out.println("Erro ao abrir: " + caminhoFXML);
            e.printStackTrace();
        }
    }

    private void trocarTela(MouseEvent event, String caminhoFXML) {

        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource(caminhoFXML)
            );

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {

            System.out.println("Erro ao abrir: " + caminhoFXML);
            e.printStackTrace();
        }
    }
}