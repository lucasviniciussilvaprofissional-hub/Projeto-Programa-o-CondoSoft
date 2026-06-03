package com.condominio.controller.unidade;

import com.condominio.models.moradia.Unidade;
import com.condominio.repository.implementation.UnidadeRepositoryImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;

public class ListarUnidadeController {

    @FXML
    private TableView<Unidade> tabelaUnidades;

    @FXML
    private TableColumn<Unidade, Integer> colNumero;

    @FXML
    private TableColumn<Unidade, String> colBloco;

    @FXML
    private TableColumn<Unidade, String> colTipo;

    @FXML
    private TableColumn<Unidade, String> colArea;

    @FXML
    private TableColumn<Unidade, Integer> colCapacidade;

    @FXML
    private TableColumn<Unidade, String> colStatus;

    @FXML
    private Label lblTotalUnidades;

    @FXML
    private TextField txtBusca;

    @FXML
    private TableColumn<Unidade, String> colAndar;

    @FXML
    private TableColumn<Unidade, String> colAcoes;

    @FXML
    private Label lblNomeUnidade;

    @FXML
    private Label lblTipo;

    @FXML
    private Label lblArea;

    @FXML
    private Label lblCapacidade;

    @FXML
    private Label lblStatusUnidade;

    @FXML
    private Label lblQtdMoradores;

    @FXML
    private Label lblFracaoIdeal;

    @FXML
    private Label lblBadgeUnidade;

    @FXML
    private Label lblBreadcrumbUnidade;

    // ==========================
// NAVEGAÇÃO
// ==========================

    public void abrirCadastrarUnidade(ActionEvent event) {
        trocarTela(
                event,
                "/com/condominio/unidade/cadastrar-unidade-view.fxml"
        );
    }

    public void voltarUnidades(ActionEvent event) {
        trocarTela(
                event,
                "/com/condominio/unidade/unidade-view.fxml"
        );
    }

    public void voltarUnidadesBreadcrumb(MouseEvent event) {
        trocarTela(
                event,
                "/com/condominio/unidade/unidade-view.fxml"
        );
    }

    public void voltarHome(MouseEvent event) {
        trocarTela(
                event,
                "/com/condominio/home-view.fxml"
        );
    }

    private final UnidadeRepositoryImpl repository =
            new UnidadeRepositoryImpl();

    @FXML
    public void initialize() {

        configurarTabela();
        carregarTabela();
    }

    private void configurarTabela() {

        colNumero.setCellValueFactory(
                new PropertyValueFactory<>("numero")
        );

        colBloco.setCellValueFactory(
                new PropertyValueFactory<>("bloco")
        );

        colTipo.setCellValueFactory(
                new PropertyValueFactory<>("tipo")
        );

        colArea.setCellValueFactory(
                new PropertyValueFactory<>("metragem")
        );

        colCapacidade.setCellValueFactory(
                new PropertyValueFactory<>("capacidade")
        );

        colStatus.setCellValueFactory(
                new PropertyValueFactory<>("status")
        );
    }

    private void carregarTabela() {

        System.out.println("TOTAL: " + repository.listar().size());

        for (Unidade u : repository.listar()) {
            System.out.println(
                    u.getNumero() + " - " +
                            u.getBloco()
            );
        }

        ObservableList<Unidade> unidades =
                FXCollections.observableArrayList(
                        repository.listar()
                );

        tabelaUnidades.setItems(unidades);

        lblTotalUnidades.setText(
                unidades.size() + " unidades encontradas"
        );
    }


    // FILTROS

    public void filtrarUnidades() {

        String busca = txtBusca.getText().toLowerCase();

        ObservableList<Unidade> filtradas =
                FXCollections.observableArrayList();

        for (Unidade u : repository.listar()) {

            if (String.valueOf(u.getNumero())
                    .contains(busca)
                    ||
                    u.getBloco()
                            .toLowerCase()
                            .contains(busca)) {

                filtradas.add(u);
            }
        }

        tabelaUnidades.setItems(filtradas);
    }

    public void filtrarUnidades(KeyEvent event) {
        filtrarUnidades();
    }

    public void limparFiltros(ActionEvent event) {

        txtBusca.clear();
        carregarTabela();
    }

    // AÇÕES DA TABELA

    public void abrirDetalhesUnidade(ActionEvent event) {

        Unidade selecionada =
                tabelaUnidades.getSelectionModel()
                        .getSelectedItem();

        if (selecionada == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText(
                    "Selecione uma unidade primeiro."
            );
            alert.showAndWait();

            return;
        }

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/condominio/unidade/detalhes-unidade-view.fxml"
                    )
            );

            Parent root = loader.load();

            DetalhesUnidadeController controller =
                    loader.getController();

            controller.setUnidade(selecionada);

            Stage stage = (Stage)
                    ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editarUnidade(ActionEvent event) {
        System.out.println("Editar unidade");
    }

    public void confirmarRemocaoUnidade(ActionEvent event) {

        Unidade selecionada =
                tabelaUnidades.getSelectionModel()
                        .getSelectedItem();

        if (selecionada == null) {
            return;
        }

        repository.remover(
                selecionada.getId()
        );

        carregarTabela();
    }

    private void trocarTela(ActionEvent event,
                            String caminhoFXML) {

        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource(caminhoFXML)
            );

            Stage stage = (Stage)
                    ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void trocarTela(MouseEvent event,
                            String caminhoFXML) {

        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource(caminhoFXML)
            );

            Stage stage = (Stage)
                    ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}