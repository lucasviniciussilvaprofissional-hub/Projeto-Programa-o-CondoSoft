package com.condominio.controller.financeiro;

import com.condominio.models.finance.Inadimplencia;
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

/** REQ07 — Histórico de inadimplência. */
public class InadimplenciaController {

    @FXML private TableView<Inadimplencia>           tvInadimplencia;
    @FXML private TableColumn<Inadimplencia, String> colUnidade;
    @FXML private TableColumn<Inadimplencia, String> colBoleto;
    @FXML private TableColumn<Inadimplencia, String> colValor;
    @FXML private TableColumn<Inadimplencia, String> colMulta;
    @FXML private TableColumn<Inadimplencia, String> colDias;
    @FXML private TableColumn<Inadimplencia, String> colStatus;

    @FXML private Label lblTotalDevido;
    @FXML private Label lblQtdInadimplentes;

    private final FinanceiroService service = new FinanceiroService(
            new BoletoRepositoryImpl(), new DespesaRepositoryImpl(),
            new RateioRepositoryImpl(), new PagamentoRepositoryImpl(),
            new InadimplenciaRepositoryImpl());

    @FXML
    public void initialize() {
        service.verificarInadimplencias();
        configurarColunas();
        carregarTabela();
    }

    private void configurarColunas() {
        colUnidade.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getUnidade() != null
                        ? "Apto " + c.getValue().getUnidade().getNumero()
                          + " – " + c.getValue().getUnidade().getBloco() : "—"));
        colBoleto.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getBoleto() != null
                        ? c.getValue().getBoleto().getCodigoBarras() : "—"));
        colValor.setCellValueFactory(c ->
                new SimpleStringProperty(String.format("R$ %.2f",
                        c.getValue().getBoleto() != null
                                ? c.getValue().getBoleto().getValor() : 0f)));
        colMulta.setCellValueFactory(c ->
                new SimpleStringProperty(String.format("R$ %.2f",
                        c.getValue().getBoleto() != null
                                ? (float) c.getValue().getBoleto().calcularMulta() : 0f)));
        colDias.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDiasAtraso() + " dias"));
        colStatus.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatus()));

        tvInadimplencia.setRowFactory(tv -> new TableRow<>() {
            @Override protected void updateItem(Inadimplencia i, boolean empty) {
                super.updateItem(i, empty);
                if (i == null || empty) { setStyle(""); return; }
                setStyle("REGULARIZADO".equals(i.getStatus())
                        ? "-fx-background-color: #DCFCE7;"
                        : "-fx-background-color: #FEE2E2;");
            }
        });
    }

    private void carregarTabela() {
        var dados = service.listarInadimplencias();
        tvInadimplencia.setItems(FXCollections.observableArrayList(dados));

        double total = dados.stream().filter(i -> "PENDENTE".equals(i.getStatus()))
                .mapToDouble(Inadimplencia::getValorDevido).sum();
        long qtd = dados.stream().filter(i -> "PENDENTE".equals(i.getStatus())).count();

        lblTotalDevido.setText(String.format("R$ %.2f", total));
        lblQtdInadimplentes.setText(qtd + " unidade(s)");
    }

    @FXML public void atualizar(ActionEvent event) { service.verificarInadimplencias(); carregarTabela(); }

    @FXML public void voltarHome(ActionEvent event) {
        nav(event, "/com/condominio/home-view.fxml");
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
