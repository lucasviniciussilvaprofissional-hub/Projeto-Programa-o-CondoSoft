package com.condominio.controller.financeiro;

import com.condominio.models.finance.Despesa;
import com.condominio.models.finance.Rateio;
import com.condominio.models.moradia.Unidade;
import com.condominio.repository.implementation.*;
import com.condominio.service.FinanceiroService;

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
import java.time.LocalDate;
import java.util.List;

/** REQ06 — Calcular rateio mensal baseado na fração ideal. */
public class RateioController {

    // ── FXML ──────────────────────────────────────────────────────────────
    @FXML private ComboBox<String>  cmbDespesa;
    @FXML private TextField         txtCompetencia;
    @FXML private Label             lblTotalDespesa;
    @FXML private Label             lblQtdUnidades;
    @FXML private Label             lblResultado;

    @FXML private TableView<Rateio>           tabela;
    @FXML private TableColumn<Rateio, String> colUnidade;
    @FXML private TableColumn<Rateio, String> colFracao;
    @FXML private TableColumn<Rateio, String> colValor;
    @FXML private TableColumn<Rateio, String> colCompetencia;

    // ── serviço + repos ───────────────────────────────────────────────────
    private final FinanceiroService service = new FinanceiroService(
            new BoletoRepositoryImpl(), new DespesaRepositoryImpl(),
            new RateioRepositoryImpl(), new PagamentoRepositoryImpl(),
            new InadimplenciaRepositoryImpl());

    private final DespesaRepositoryImpl despesaRepo   = new DespesaRepositoryImpl();
    private final UnidadeRepositoryImpl  unidadeRepo   = new UnidadeRepositoryImpl();

    // ── init ──────────────────────────────────────────────────────────────
    @FXML
    public void initialize() {
        carregarComboDespesas();
        configurarColunas();

        // competência padrão = mês atual
        txtCompetencia.setText(String.format("%02d/%d",
                LocalDate.now().getMonthValue(), LocalDate.now().getYear()));

        carregarTabela();
    }

    private void carregarComboDespesas() {
        cmbDespesa.getItems().clear();
        for (Despesa d : despesaRepo.listar())
            cmbDespesa.getItems().add(d.getId() + " – " + d.getDescricao()
                    + " (R$ " + String.format("%.2f", d.getValor()) + ")");
        if (!cmbDespesa.getItems().isEmpty())
            cmbDespesa.getSelectionModel().selectFirst();

        atualizarInfoDespesa(null);
    }

    @FXML
    public void atualizarInfoDespesa(ActionEvent event) {
        Despesa d = despesaSelecionada();
        List<Unidade> unidades = unidadeRepo.listar();
        if (d != null) {
            lblTotalDespesa.setText(String.format("R$ %.2f", d.getValor()));
            lblQtdUnidades.setText(unidades.size() + " unidade(s)");
        }
    }

    private void configurarColunas() {
        colUnidade.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getUnidade() != null
                        ? "Apto " + c.getValue().getUnidade().getNumero()
                          + " – " + c.getValue().getUnidade().getBloco() : "—"));
        colFracao.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getUnidade() != null
                        ? String.format("%.4f", c.getValue().getUnidade().getFracaoIdeal()) : "—"));
        colValor.setCellValueFactory(c ->
                new SimpleStringProperty(String.format("R$ %.2f", c.getValue().getValorRateado())));
        colCompetencia.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getCompetencia()));
    }

    private void carregarTabela() {
        String comp = txtCompetencia.getText().trim();
        List<Rateio> dados = comp.isEmpty()
                ? service.listarRateiosPorCompetencia("")
                : service.listarRateiosPorCompetencia(comp);
        tabela.setItems(FXCollections.observableArrayList(dados));
    }

    // ── calcular rateio ───────────────────────────────────────────────────
    @FXML
    public void calcularRateio(ActionEvent event) {
        Despesa d = despesaSelecionada();
        if (d == null) { alerta("Selecione uma despesa."); return; }

        String comp = txtCompetencia.getText().trim();
        if (comp.isEmpty()) { alerta("Informe a competência (ex.: 06/2026)."); return; }

        List<Unidade> unidades = unidadeRepo.listar();
        if (unidades.isEmpty()) { alerta("Nenhuma unidade cadastrada."); return; }

        try {
            List<Rateio> gerados = service.calcularRateio(d, unidades, comp);
            lblResultado.setText(gerados.size() + " rateio(s) calculado(s) para " + comp);
            carregarTabela();
        } catch (IllegalArgumentException e) { alerta(e.getMessage()); }
    }

    @FXML
    public void filtrarPorCompetencia(ActionEvent event) { carregarTabela(); }

    // ── navegação ─────────────────────────────────────────────────────────
    @FXML public void voltarFinanceiro(ActionEvent event) {
        nav(event, "/com/condominio/financeiro/financeiro-view.fxml");
    }

    // ── util ──────────────────────────────────────────────────────────────
    private Despesa despesaSelecionada() {
        String sel = cmbDespesa.getValue();
        if (sel == null) return null;
        try {
            int id = Integer.parseInt(sel.split("–")[0].trim());
            return despesaRepo.buscarPorId(id);
        } catch (Exception e) { return null; }
    }

    private void alerta(String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait();
    }
    private void nav(ActionEvent event, String fxml) {
        try {
            FXMLLoader l = new FXMLLoader(getClass().getResource(fxml));
            Parent r = l.load();
            Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
            s.setScene(new Scene(r)); s.show();
        } catch (IOException e) { e.printStackTrace(); }
    }
}
