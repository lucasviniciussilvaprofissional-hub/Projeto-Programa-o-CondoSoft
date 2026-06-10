package com.condominio.controller.portaria;

import com.condominio.enums.StatusOcorrencia;
import com.condominio.enums.TipoOcorrencia;
import com.condominio.models.disciplina.Ocorrencia;
import com.condominio.models.moradia.Morador;
import com.condominio.models.moradia.Unidade;
import com.condominio.repository.implementation.MoradorRepositoryImpl;
import com.condominio.repository.implementation.OcorrenciaRepositoryImpl;
import com.condominio.repository.implementation.UnidadeRepositoryImpl;

import javafx.beans.property.SimpleStringProperty;
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
import java.time.LocalDateTime;
import java.util.List;

/**
 * OcorrenciaController - integrado com UnidadeRepositoryImpl e MoradorRepositoryImpl.
 *
 * Fluxo: usuario seleciona Unidade -> cbMorador eh populado com os moradores
 * daquela unidade -> ao salvar, monta um Ocorrencia real (model com Morador e
 * Unidade de verdade) e persiste via OcorrenciaRepositoryImpl (lista static).
 */
public class OcorrenciaController {

    // ── FXML ──────────────────────────────────────────────────────────────
    @FXML private ComboBox<Unidade> cbUnidade;
    @FXML private ComboBox<Morador> cbMorador;
    @FXML private ComboBox<String>  cbTipo;
    @FXML private TextField txtTitulo;
    @FXML private TextArea  txtDescricao;

    @FXML private TableView<Ocorrencia>           tabelaOcorrencias;
    @FXML private TableColumn<Ocorrencia, String> colUnidade;
    @FXML private TableColumn<Ocorrencia, String> colTipo;
    @FXML private TableColumn<Ocorrencia, String> colTitulo;
    @FXML private TableColumn<Ocorrencia, String> colDescricao;
    @FXML private TableColumn<Ocorrencia, String> colStatus;

    // ── repositórios ─────────────────────────────────────────────────────
    private final UnidadeRepositoryImpl    unidadeRepo    = new UnidadeRepositoryImpl();
    private final MoradorRepositoryImpl    moradorRepo    = new MoradorRepositoryImpl();
    private final OcorrenciaRepositoryImpl ocorrenciaRepo = new OcorrenciaRepositoryImpl();

    @FXML
    public void initialize() {
        // Tipos de ocorrência (enum real)
        cbTipo.setItems(FXCollections.observableArrayList(
                "Advertência", "Barulho Excessivo", "Uso Indevido de Área Comum",
                "Infração ao Regimento", "Multa", "Outros"));
        cbTipo.setValue("Barulho Excessivo");

        // Unidades reais do repositório
        cbUnidade.setItems(FXCollections.observableArrayList(unidadeRepo.listar()));
        cbUnidade.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Unidade u) {
                return u == null ? "" : "Apto " + u.getNumero() + " – " + u.getBloco();
            }
            @Override public Unidade fromString(String s) { return null; }
        });

        cbMorador.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(Morador m) {
                return m == null ? "" : m.getNome();
            }
            @Override public Morador fromString(String s) { return null; }
        });
        cbMorador.setPromptText("Selecione a unidade antes...");

        configurarColunas();
        atualizarTabela();
    }

    private void configurarColunas() {
        colUnidade.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getUnidade() != null
                        ? "Apto " + d.getValue().getUnidade().getNumero()
                          + " – " + d.getValue().getUnidade().getBloco()
                        : "—"));
        colTipo.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getTipo() != null ? d.getValue().getTipo().name() : "—"));
        colTitulo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTitulo()));
        colDescricao.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDescricao()));
        colStatus.setCellValueFactory(d -> new SimpleStringProperty(
                d.getValue().getStatus() != null ? d.getValue().getStatus().name() : "—"));
    }

    /** Ao escolher a unidade, popula cbMorador com os moradores reais dessa unidade. */
    @FXML
    private void aoSelecionarUnidade(ActionEvent event) {
        Unidade unidade = cbUnidade.getValue();
        cbMorador.getItems().clear();

        if (unidade == null) return;

        List<Morador> moradores = unidade.getMoradores();
        if (moradores == null || moradores.isEmpty()) {
            cbMorador.setPromptText("Nenhum morador nesta unidade");
            return;
        }

        cbMorador.setItems(FXCollections.observableArrayList(moradores));
        cbMorador.setPromptText("Selecione...");
        cbMorador.getSelectionModel().selectFirst();
    }

    // ── salvar ────────────────────────────────────────────────────────────
    @FXML
    private void salvarOcorrencia() {
        Unidade unidade  = cbUnidade.getValue();
        Morador morador  = cbMorador.getValue();
        String  tipoStr  = cbTipo.getValue();
        String  titulo   = txtTitulo.getText();
        String  descricao = txtDescricao.getText();

        if (unidade == null) {
            alerta("Selecione a unidade.");
            return;
        }
        if (morador == null) {
            alerta("Selecione o morador envolvido.");
            return;
        }
        if (titulo == null || titulo.isBlank()
                || descricao == null || descricao.isBlank()) {
            alerta("Preencha o título e a descrição.");
            return;
        }

        TipoOcorrencia tipo = mapearTipo(tipoStr);
        int novoId = ocorrenciaRepo.listar().size() + 1;

        Ocorrencia ocorrencia = new Ocorrencia(
                novoId,
                titulo,
                descricao,
                tipo,
                StatusOcorrencia.ABERTA,
                morador,
                unidade,
                LocalDateTime.now());

        ocorrenciaRepo.salvar(ocorrencia);

        // limpa formulário
        txtTitulo.clear();
        txtDescricao.clear();
        cbTipo.setValue("Barulho Excessivo");
        cbUnidade.getSelectionModel().clearSelection();
        cbMorador.getItems().clear();
        cbMorador.setPromptText("Selecione a unidade antes...");

        atualizarTabela();
    }

    @FXML
    private void resolverOcorrencia() {
        Ocorrencia selecionada = tabelaOcorrencias.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            new Alert(Alert.AlertType.INFORMATION, "Selecione uma ocorrência para resolver.")
                    .showAndWait();
            return;
        }
        try {
            selecionada.finalizarOcorrencia();
            atualizarTabela();
        } catch (IllegalArgumentException e) {
            alerta(e.getMessage());
        }
    }

    private void atualizarTabela() {
        tabelaOcorrencias.setItems(FXCollections.observableArrayList(ocorrenciaRepo.listar()));
    }

    private TipoOcorrencia mapearTipo(String tipoStr) {
        if (tipoStr == null) return TipoOcorrencia.OUTROS;
        return switch (tipoStr) {
            case "Advertência"                  -> TipoOcorrencia.ADVERTENCIA;
            case "Barulho Excessivo"             -> TipoOcorrencia.BARULHO_EXCESSIVO;
            case "Uso Indevido de Área Comum"    -> TipoOcorrencia.USO_INDEVIDO_AREA_COMUM;
            case "Infração ao Regimento"         -> TipoOcorrencia.INFRACAO_REGIMENTO;
            case "Multa"                         -> TipoOcorrencia.MULTA;
            default                              -> TipoOcorrencia.OUTROS;
        };
    }

    private void alerta(String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait();
    }

    // ── navegação ─────────────────────────────────────────────────────────
    @FXML
    private void voltarPortaria(ActionEvent event) {
        try {
            URL fxmlUrl = getClass().getResource("/com/condominio/portaria/portaria-view.fxml");
            if (fxmlUrl == null) {
                fxmlUrl = getClass().getClassLoader()
                        .getResource("com/condominio/portaria/portaria-view.fxml");
            }
            if (fxmlUrl == null) throw new IOException("portaria-view.fxml não encontrado.");

            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
