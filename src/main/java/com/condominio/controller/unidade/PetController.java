package com.condominio.controller.unidade;

import com.condominio.enums.TipoPet;
import com.condominio.models.moradia.Pet;
import com.condominio.models.moradia.Unidade;
import com.condominio.repository.implementation.PetRepositoryImpl;
import com.condominio.repository.implementation.UnidadeRepositoryImpl;

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

public class PetController {

    // ───── estado global ───────────────────────────────────────────────────
    private static Unidade unidadeSelecionadaGlobal = null;

    public static void setUnidadeSelecionadaGlobal(Unidade unidade) {
        unidadeSelecionadaGlobal = unidade;
    }

    public static Unidade getUnidadeSelecionadaGlobal() {
        return unidadeSelecionadaGlobal;
    }

    // ───── FXML ────────────────────────────────────────────────────────────
    @FXML private Label lblBadgeUnidade;
    @FXML private Label lblTotalPets;
    @FXML private Label lblQtdCaes;
    @FXML private Label lblQtdGatos;
    @FXML private Label lblQtdOutros;
    @FXML private Label lblSelecaoInfo;

    @FXML private TextField txtBusca;
    @FXML private ComboBox<String> cmbFiltroTipo;

    @FXML private TableView<Pet>           tabelaPets;
    @FXML private TableColumn<Pet, Number> colId;
    @FXML private TableColumn<Pet, String> colTipo;
    @FXML private TableColumn<Pet, String> colNome;
    @FXML private TableColumn<Pet, String> colRaca;
    @FXML private TableColumn<Pet, String> colCor;
    @FXML private TableColumn<Pet, String> colAcoes;

    // ───── repositórios ────────────────────────────────────────────────────
    private final PetRepositoryImpl    petRepo    = new PetRepositoryImpl();
    private final UnidadeRepositoryImpl unidadeRepo = new UnidadeRepositoryImpl();

    private ObservableList<Pet> listaObservavel;

    // ───── inicialização ───────────────────────────────────────────────────
    @FXML
    public void initialize() {
        configurarColunas();
        preencherFiltroTipo();
        atualizarBadgeUnidade();
        carregarTabela();
        configurarSelecao();
    }

    private void configurarColunas() {
        colId.setCellValueFactory(
                c -> new SimpleIntegerProperty(c.getValue().getId()));
        colTipo.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getTipo().name()));
        colNome.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getNome()));
        colRaca.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getRaca()));
        colCor.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getCor()));
        colAcoes.setCellValueFactory(
                c -> new SimpleStringProperty("—"));
    }

    private void preencherFiltroTipo() {
        cmbFiltroTipo.getItems().clear();
        cmbFiltroTipo.getItems().add("Todos");
        for (TipoPet t : TipoPet.values()) {
            cmbFiltroTipo.getItems().add(t.name());
        }
        cmbFiltroTipo.getSelectionModel().selectFirst();
    }

    private void atualizarBadgeUnidade() {
        if (unidadeSelecionadaGlobal != null) {
            lblBadgeUnidade.setText(
                    "Apto " + unidadeSelecionadaGlobal.getNumero()
                    + " – " + unidadeSelecionadaGlobal.getBloco());
        }
    }

    private void carregarTabela() {
        List<Pet> dados = unidadeSelecionadaGlobal != null
                ? petRepo.listarPorUnidade(unidadeSelecionadaGlobal.getId())
                : petRepo.listar();

        listaObservavel = FXCollections.observableArrayList(dados);
        tabelaPets.setItems(listaObservavel);
        atualizarContadores(dados);
    }

    private void atualizarContadores(List<Pet> dados) {
        long caes   = dados.stream().filter(p -> p.getTipo() == TipoPet.CACHORRO).count();
        long gatos  = dados.stream().filter(p -> p.getTipo() == TipoPet.GATO).count();
        long outros = dados.stream()
                .filter(p -> p.getTipo() != TipoPet.CACHORRO && p.getTipo() != TipoPet.GATO)
                .count();

        lblTotalPets.setText(dados.size() + " pet(s) nesta unidade");
        lblQtdCaes.setText(String.valueOf(caes));
        lblQtdGatos.setText(String.valueOf(gatos));
        lblQtdOutros.setText(String.valueOf(outros));
    }

    private void configurarSelecao() {
        tabelaPets.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, novo) -> {
                    if (novo != null) {
                        lblSelecaoInfo.setText(
                                "Selecionado: " + novo.getNome()
                                + " (" + novo.getTipo().name() + ")");
                    } else {
                        lblSelecaoInfo.setText("Selecione um pet para gerenciá-lo");
                    }
                });
    }

    // ───── filtros ─────────────────────────────────────────────────────────
    @FXML
    public void filtrarPets(KeyEvent event) {
        aplicarFiltro();
    }

    @FXML
    public void filtrarPets(ActionEvent event) {
        aplicarFiltro();
    }

    private void aplicarFiltro() {
        String texto = txtBusca.getText().toLowerCase();
        String tipoFiltro = cmbFiltroTipo.getValue();

        List<Pet> base = unidadeSelecionadaGlobal != null
                ? petRepo.listarPorUnidade(unidadeSelecionadaGlobal.getId())
                : petRepo.listar();

        ObservableList<Pet> filtrados = FXCollections.observableArrayList();
        for (Pet p : base) {
            boolean nomeOuRaca = p.getNome().toLowerCase().contains(texto)
                    || p.getRaca().toLowerCase().contains(texto);
            boolean tipoOk = tipoFiltro == null
                    || tipoFiltro.equals("Todos")
                    || p.getTipo().name().equals(tipoFiltro);
            if (nomeOuRaca && tipoOk) filtrados.add(p);
        }
        tabelaPets.setItems(filtrados);
    }

    @FXML
    public void limparFiltros(ActionEvent event) {
        txtBusca.clear();
        cmbFiltroTipo.getSelectionModel().selectFirst();
        carregarTabela();
    }

    // ───── ações ───────────────────────────────────────────────────────────
    @FXML
    public void abrirCadastrarPet(ActionEvent event) {
        CadastrarPetController.setPetEmEdicao(null);
        trocarTela(event, "/com/condominio/unidade/cadastrar-pet-view.fxml");
    }

    @FXML
    public void editarPet(ActionEvent event) {
        Pet selecionado = tabelaPets.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAviso("Selecione um pet para editar.");
            return;
        }
        CadastrarPetController.setPetEmEdicao(selecionado);
        trocarTela(event, "/com/condominio/unidade/cadastrar-pet-view.fxml");
    }

    @FXML
    public void confirmarRemocaoPet(ActionEvent event) {
        Pet selecionado = tabelaPets.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAviso("Selecione um pet para remover.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Remover Pet");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText(
                "Deseja remover o pet " + selecionado.getNome() + "?");

        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                petRepo.remover(selecionado.getId());

                if (unidadeSelecionadaGlobal != null) {
                    try {
                        unidadeSelecionadaGlobal.removerPet(selecionado);
                        unidadeRepo.atualizar(unidadeSelecionadaGlobal);
                    } catch (Exception ignored) { /* já removido */ }
                }

                carregarTabela();
            }
        });
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
