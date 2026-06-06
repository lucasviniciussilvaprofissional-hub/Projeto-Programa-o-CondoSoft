package com.condominio.controller.financeiro;

import com.condominio.enums.StatusBoleto;
import com.condominio.models.finance.Boleto;
import com.condominio.models.finance.Pagamento;
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
import java.time.format.DateTimeFormatter;

/** REQ07 — Registrar pagamentos de boletos e histórico. */
public class PagamentosController {

    // ── FXML ──────────────────────────────────────────────────────────────
    @FXML private TableView<Boleto>           tvBoletos;
    @FXML private TableColumn<Boleto, String> colUnidade;
    @FXML private TableColumn<Boleto, String> colDescricao;
    @FXML private TableColumn<Boleto, String> colCompetencia;
    @FXML private TableColumn<Boleto, String> colVencimento;
    @FXML private TableColumn<Boleto, String> colValor;
    @FXML private TableColumn<Boleto, String> colStatus;

    @FXML private TableView<Pagamento>           tvHistorico;
    @FXML private TableColumn<Pagamento, String> colHUnidade;
    @FXML private TableColumn<Pagamento, String> colHValor;
    @FXML private TableColumn<Pagamento, String> colHForma;
    @FXML private TableColumn<Pagamento, String> colHData;

    @FXML private ComboBox<String> cmbFiltroStatus;
    @FXML private Label lblTotalPago;
    @FXML private Label lblTotalPendente;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final FinanceiroService service = new FinanceiroService(
            new BoletoRepositoryImpl(), new DespesaRepositoryImpl(),
            new RateioRepositoryImpl(), new PagamentoRepositoryImpl(),
            new InadimplenciaRepositoryImpl());

    // ── init ──────────────────────────────────────────────────────────────
    @FXML
    public void initialize() {
        service.verificarInadimplencias();

        cmbFiltroStatus.setItems(FXCollections.observableArrayList(
                "Todos", "PENDENTE", "PAGO", "VENCIDO", "CANCELADO"));
        cmbFiltroStatus.getSelectionModel().selectFirst();

        configurarColunasBoletos();
        configurarColunasHistorico();
        carregarTudo();
    }

    private void configurarColunasBoletos() {
        colUnidade.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getUnidade() != null
                        ? "Apto " + c.getValue().getUnidade().getNumero() : "—"));
        colDescricao.setCellValueFactory(c ->
                new SimpleStringProperty("Taxa Condominial"));
        colCompetencia.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getCompetencia()));
        colVencimento.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDataVencimento() != null
                        ? c.getValue().getDataVencimento().toString() : "—"));
        colValor.setCellValueFactory(c ->
                new SimpleStringProperty(String.format("R$ %.2f", c.getValue().getValor())));
        colStatus.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatus().name()));

        // destaque visual por status
        tvBoletos.setRowFactory(tv -> new TableRow<>() {
            @Override protected void updateItem(Boleto b, boolean empty) {
                super.updateItem(b, empty);
                if (b == null || empty) { setStyle(""); return; }
                switch (b.getStatus()) {
                    case PAGO      -> setStyle("-fx-background-color: #DCFCE7;");
                    case VENCIDO   -> setStyle("-fx-background-color: #FEE2E2;");
                    case PENDENTE  -> setStyle("-fx-background-color: #FEF9C3;");
                    default        -> setStyle("");
                }
            }
        });
    }

    private void configurarColunasHistorico() {
        colHUnidade.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getBoleto() != null
                        && c.getValue().getBoleto().getUnidade() != null
                        ? "Apto " + c.getValue().getBoleto().getUnidade().getNumero() : "—"));
        colHValor.setCellValueFactory(c ->
                new SimpleStringProperty(String.format("R$ %.2f", c.getValue().getValorPago())));
        colHForma.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getFormaPagamento()));
        colHData.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDataPagamento() != null
                        ? c.getValue().getDataPagamento().format(FMT) : "—"));
    }

    private void carregarTudo() {
        // boletos com filtro
        String f = cmbFiltroStatus.getValue();
        var boletos = "Todos".equals(f)
                ? service.listarBoletos()
                : service.listarBoletos().stream()
                        .filter(b -> b.getStatus().name().equals(f))
                        .toList();
        tvBoletos.setItems(FXCollections.observableArrayList(boletos));

        // histórico completo
        tvHistorico.setItems(FXCollections.observableArrayList(service.listarPagamentos()));

        // totais
        double pago = service.listarBoletosPagos().stream().mapToDouble(Boleto::getValor).sum();
        double pend = service.listarBoletosPendentes().stream().mapToDouble(Boleto::getValor).sum()
                    + service.listarBoletosVencidos().stream().mapToDouble(Boleto::getValor).sum();
        lblTotalPago.setText(String.format("R$ %.2f", pago));
        lblTotalPendente.setText(String.format("R$ %.2f", pend));
    }

    // ── registrar pagamento ───────────────────────────────────────────────
    @FXML
    public void registrarPagamento(ActionEvent event) {
        Boleto sel = tvBoletos.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("Selecione um boleto."); return; }
        if (sel.getStatus() == StatusBoleto.PAGO) { alerta("Boleto já pago."); return; }
        if (sel.getStatus() == StatusBoleto.CANCELADO) { alerta("Boleto cancelado."); return; }

        TextInputDialog dlg = new TextInputDialog("PIX");
        dlg.setTitle("Registrar Pagamento");
        dlg.setHeaderText("Boleto: " + sel.getCodigoBarras()
                + "\nValor: R$ " + String.format("%.2f", sel.getValor()));
        dlg.setContentText("Forma de pagamento:");
        dlg.showAndWait().ifPresent(forma -> {
            try {
                service.registrarPagamento(sel.getId(), forma.isBlank() ? "PIX" : forma);
                new Alert(Alert.AlertType.INFORMATION, "Pagamento registrado!", ButtonType.OK)
                        .showAndWait();
                carregarTudo();
            } catch (IllegalArgumentException e) { alerta(e.getMessage()); }
        });
    }

    @FXML public void filtrarBoletos(ActionEvent event) { carregarTudo(); }
    @FXML public void atualizar(ActionEvent event)       { service.verificarInadimplencias(); carregarTudo(); }

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
