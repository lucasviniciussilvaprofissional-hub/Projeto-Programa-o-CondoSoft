package com.condominio.controller.unidade;

import com.condominio.models.moradia.Unidade;
import com.condominio.models.moradia.Veiculo;
import com.condominio.repository.implementation.UnidadeRepositoryImpl;
import com.condominio.repository.implementation.VeiculoRepositoryImpl;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class VeiculoController {

    // ───── estado global (passado entre telas) ─────────────────────────────
    private static Unidade unidadeSelecionadaGlobal = null;

    public static void setUnidadeSelecionadaGlobal(Unidade unidade) {
        unidadeSelecionadaGlobal = unidade;
    }

    public static Unidade getUnidadeSelecionadaGlobal() {
        return unidadeSelecionadaGlobal;
    }

    // ───── FXML ────────────────────────────────────────────────────────────
    @FXML private Label lblBadgeUnidade;
    @FXML private Label lblTotalVeiculos;
    @FXML private Label lblBadgeTotal;
    @FXML private Label lblSelecaoInfo;

    @FXML private TextField txtBusca;

    @FXML private TableView<Veiculo>         tabelaVeiculos;
    @FXML private TableColumn<Veiculo, Number> colId;
    @FXML private TableColumn<Veiculo, String> colModelo;
    @FXML private TableColumn<Veiculo, String> colPlaca;
    @FXML private TableColumn<Veiculo, String> colCor;
    @FXML private TableColumn<Veiculo, String> colAcoes;

    // ───── repositórios ────────────────────────────────────────────────────
    private final VeiculoRepositoryImpl veiculoRepo = new VeiculoRepositoryImpl();
    private final UnidadeRepositoryImpl unidadeRepo  = new UnidadeRepositoryImpl();

    private ObservableList<Veiculo> listaObservavel;

    // ───── inicialização ───────────────────────────────────────────────────
    @FXML
    public void initialize() {
        configurarColunas();
        atualizarBadgeUnidade();
        carregarTabela();
        configurarSelecao();
    }

    private void configurarColunas() {
        colId.setCellValueFactory(
                c -> new SimpleIntegerProperty(c.getValue().getId()));

        colModelo.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getModelo()));

        colPlaca.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getPlaca()));

        colCor.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getCor()));

        colAcoes.setCellValueFactory(
                c -> new SimpleStringProperty("—"));
    }

    private void atualizarBadgeUnidade() {
        if (unidadeSelecionadaGlobal != null) {
            lblBadgeUnidade.setText(
                    "Apto " + unidadeSelecionadaGlobal.getNumero()
                    + " – " + unidadeSelecionadaGlobal.getBloco());
        }
    }

    private void carregarTabela() {
        List<Veiculo> dados;

        if (unidadeSelecionadaGlobal != null) {
            dados = veiculoRepo.listarPorUnidade(unidadeSelecionadaGlobal.getId());
        } else {
            dados = veiculoRepo.listar();
        }

        listaObservavel = FXCollections.observableArrayList(dados);
        tabelaVeiculos.setItems(listaObservavel);
        atualizarContadores(dados.size());
    }

    private void atualizarContadores(int total) {
        lblTotalVeiculos.setText(total + " veículo(s) nesta unidade");
        lblBadgeTotal.setText(total + " veículo(s)");
    }

    private void configurarSelecao() {
        tabelaVeiculos.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, novo) -> {
                    if (novo != null) {
                        lblSelecaoInfo.setText(
                                "Selecionado: " + novo.getModelo()
                                + " – " + novo.getPlaca());
                    } else {
                        lblSelecaoInfo.setText("Selecione um veículo para gerenciá-lo");
                    }
                });
    }

    // ───── filtro ──────────────────────────────────────────────────────────
    @FXML
    public void filtrarVeiculos(KeyEvent event) {
        aplicarFiltro();
    }

    private void aplicarFiltro() {
        String texto = txtBusca.getText().toLowerCase();

        List<Veiculo> base = unidadeSelecionadaGlobal != null
                ? veiculoRepo.listarPorUnidade(unidadeSelecionadaGlobal.getId())
                : veiculoRepo.listar();

        ObservableList<Veiculo> filtrados = FXCollections.observableArrayList();
        for (Veiculo v : base) {
            if (v.getPlaca().toLowerCase().contains(texto)
                    || v.getModelo().toLowerCase().contains(texto)) {
                filtrados.add(v);
            }
        }
        tabelaVeiculos.setItems(filtrados);
    }

    @FXML
    public void limparBusca(ActionEvent event) {
        txtBusca.clear();
        carregarTabela();
    }

    // ───── ações ───────────────────────────────────────────────────────────
    @FXML
    public void editarVeiculo(ActionEvent event) {
        Veiculo selecionado = tabelaVeiculos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAviso("Selecione um veículo para editar.");
            return;
        }
        CadastrarVeiculoController.setVeiculoEmEdicao(selecionado);
        trocarTela(event, "/com/condominio/unidade/cadastrar-veiculo-view.fxml");
    }

    @FXML
    public void confirmarRemocaoVeiculo(ActionEvent event) {
        Veiculo selecionado = tabelaVeiculos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAviso("Selecione um veículo para remover.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Remover Veículo");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText(
                "Deseja remover o veículo " + selecionado.getModelo()
                + " (placa: " + selecionado.getPlaca() + ")?");

        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                // Remove do repositório e da unidade
                veiculoRepo.remover(selecionado.getId());

                if (unidadeSelecionadaGlobal != null) {
                    try {
                        unidadeSelecionadaGlobal.removerVeiculo(selecionado);
                        unidadeRepo.atualizar(unidadeSelecionadaGlobal);
                    } catch (Exception ignored) { /* já removido */ }
                }

                carregarTabela();
            }
        });
    }

    @FXML
    public void abrirCadastrarVeiculo(ActionEvent event) {
        CadastrarVeiculoController.setVeiculoEmEdicao(null);
        trocarTela(event, "/com/condominio/unidade/cadastrar-veiculo-view.fxml");
    }

    // ───── navegação ───────────────────────────────────────────────────────
    @FXML
    public void voltarHome(MouseEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    @FXML
    public void voltarUnidades(MouseEvent event) {
        trocarTela(event, "/com/condominio/unidade/unidade-view.fxml");
    }

    @FXML
    public void voltarDetalhesUnidade(ActionEvent event) {
        trocarTela(event, "/com/condominio/unidade/detalhes-unidade-view.fxml");
    }

    @FXML
    public void voltarDetalhesUnidadeBreadCrumb(MouseEvent event) {
        trocarTela(event, "/com/condominio/unidade/detalhes-unidade-view.fxml");
    }

    // ───── utilidades ──────────────────────────────────────────────────────
    private void mostrarAviso(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void trocarTela(javafx.event.Event event, String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao abrir: " + fxml);
        }
    }
}
