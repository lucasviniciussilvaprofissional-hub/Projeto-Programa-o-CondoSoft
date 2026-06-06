package com.condominio.controller.financeiro;

import com.condominio.enums.TipoDespesa;
import com.condominio.models.finance.Despesa;
import com.condominio.models.finance.DespesaExtraordinaria;
import com.condominio.models.finance.DespesaFixa;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/** REQ05 — Registrar despesas Fixas e Extraordinárias usando herança. */
public class DespesasController {

    // ── FXML ──────────────────────────────────────────────────────────────
    @FXML private ComboBox<String> cmbTipoDespesa;
    @FXML private TextField        txtDescricao;
    @FXML private TextField        txtValor;
    @FXML private DatePicker       dpVencimento;
    @FXML private TextField        txtPeriodicidade;   // só para FIXA
    @FXML private TextField        txtMotivo;          // só para EXTRAORDINARIA
    @FXML private VBox             secaoFixa;
    @FXML private VBox             secaoExtraordinaria;

    @FXML private TableView<Despesa>           tabela;
    @FXML private TableColumn<Despesa, String> colTipo;
    @FXML private TableColumn<Despesa, String> colDescricao;
    @FXML private TableColumn<Despesa, String> colValor;
    @FXML private TableColumn<Despesa, String> colVencimento;
    @FXML private TableColumn<Despesa, String> colStatus;
    @FXML private TableColumn<Despesa, String> colExtra;

    @FXML private Label lblTotalFixas;
    @FXML private Label lblTotalExtra;
    @FXML private Label lblTotalGeral;

    // ── serviço ───────────────────────────────────────────────────────────
    private final FinanceiroService service = new FinanceiroService(
            new BoletoRepositoryImpl(), new DespesaRepositoryImpl(),
            new RateioRepositoryImpl(), new PagamentoRepositoryImpl(),
            new InadimplenciaRepositoryImpl());

    // ── init ──────────────────────────────────────────────────────────────
    @FXML
    public void initialize() {
        cmbTipoDespesa.setItems(FXCollections.observableArrayList("FIXA", "EXTRAORDINARIA"));
        cmbTipoDespesa.getSelectionModel().selectFirst();
        alternarSecoes(null);

        configurarColunas();
        carregarTabela();
    }

    @FXML
    public void alternarSecoes(ActionEvent event) {
        boolean fixa = "FIXA".equals(cmbTipoDespesa.getValue());
        secaoFixa.setVisible(fixa);           secaoFixa.setManaged(fixa);
        secaoExtraordinaria.setVisible(!fixa); secaoExtraordinaria.setManaged(!fixa);
    }

    private void configurarColunas() {
        colTipo.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getTipo() != null
                        ? c.getValue().getTipo().name() : "—"));
        colDescricao.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDescricao()));
        colValor.setCellValueFactory(c ->
                new SimpleStringProperty(String.format("R$ %.2f", c.getValue().getValor())));
        colVencimento.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDataVencimento() != null
                        ? c.getValue().getDataVencimento().toString() : "—"));
        colStatus.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatus() != null
                        ? c.getValue().getStatus().name() : "—"));
        colExtra.setCellValueFactory(c -> {
            if (c.getValue() instanceof DespesaFixa d)
                return new SimpleStringProperty("Periodicidade: " + d.getPeriodicidade());
            if (c.getValue() instanceof DespesaExtraordinaria d)
                return new SimpleStringProperty("Motivo: " + d.getMotivo());
            return new SimpleStringProperty("—");
        });
    }

    private void carregarTabela() {
        List<Despesa> dados = service.listarDespesas();
        tabela.setItems(FXCollections.observableArrayList(dados));

        double fixas = dados.stream()
                .filter(d -> d.getTipo() == TipoDespesa.FIXA)
                .mapToDouble(Despesa::getValor).sum();
        double extra = dados.stream()
                .filter(d -> d.getTipo() == TipoDespesa.EXTRAORDINARIA)
                .mapToDouble(Despesa::getValor).sum();

        lblTotalFixas.setText(String.format("R$ %.2f", fixas));
        lblTotalExtra.setText(String.format("R$ %.2f", extra));
        lblTotalGeral.setText(String.format("R$ %.2f", fixas + extra));
    }

    // ── salvar ────────────────────────────────────────────────────────────
    @FXML
    public void salvarDespesa(ActionEvent event) {
        String tipo      = cmbTipoDespesa.getValue();
        String descricao = txtDescricao.getText().trim();
        String valorTxt  = txtValor.getText().trim();
        LocalDate vcto   = dpVencimento.getValue();

        if (descricao.isEmpty() || valorTxt.isEmpty() || vcto == null) {
            alerta("Preencha Descrição, Valor e Vencimento."); return;
        }

        float valor;
        try { valor = Float.parseFloat(valorTxt.replace(",", ".")); }
        catch (NumberFormatException e) { alerta("Valor inválido."); return; }

        try {
            if ("FIXA".equals(tipo)) {
                String per = txtPeriodicidade.getText().trim();
                if (per.isEmpty()) per = "MENSAL";
                service.registrarDespesaFixa(descricao, valor, vcto, per);
            } else {
                String motivo = txtMotivo.getText().trim();
                if (motivo.isEmpty()) motivo = "Não informado";
                service.registrarDespesaExtraordinaria(descricao, valor, vcto, motivo);
            }
            limpar(null);
            carregarTabela();
        } catch (IllegalArgumentException e) { alerta(e.getMessage()); }
    }

    @FXML
    public void marcarPaga(ActionEvent event) {
        Despesa sel = tabela.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("Selecione uma despesa."); return; }
        service.marcarDespesaPaga(sel.getId());
        carregarTabela();
    }

    @FXML
    public void limpar(ActionEvent event) {
        txtDescricao.clear(); txtValor.clear();
        txtPeriodicidade.clear(); txtMotivo.clear();
        dpVencimento.setValue(null);
    }

    // ── navegação ─────────────────────────────────────────────────────────
    @FXML public void voltarFinanceiro(ActionEvent event) {
        nav(event, "/com/condominio/financeiro/financeiro-view.fxml");
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
