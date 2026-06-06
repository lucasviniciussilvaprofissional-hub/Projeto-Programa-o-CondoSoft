package com.condominio.controller.financeiro;

import com.condominio.repository.implementation.*;
import com.condominio.service.FinanceiroService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FinanceiroController implements Initializable {

    @FXML private Label lblArrecadado;
    @FXML private Label lblInadimplencia;
    @FXML private Label lblBoletosAbertos;

    private final FinanceiroService service = new FinanceiroService(
            new BoletoRepositoryImpl(),
            new DespesaRepositoryImpl(),
            new RateioRepositoryImpl(),
            new PagamentoRepositoryImpl(),
            new InadimplenciaRepositoryImpl()
    );

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        service.verificarInadimplencias(); // REQ07: atualiza vencidos
        atualizarDashboard();
    }

    private void atualizarDashboard() {
        lblArrecadado.setText(String.format("R$ %.2f", service.totalArrecadado()));
        lblInadimplencia.setText(String.format("R$ %.2f", service.totalInadimplencia()));
        lblBoletosAbertos.setText(String.valueOf(service.qtdBoletosAbertos()));
    }

    @FXML private void abrirGerarBoleto(ActionEvent event) {
        nav(event, "/com/condominio/financeiro/gerar-boleto-view.fxml", "Gerar Boleto");
    }
    @FXML private void abrirTelaPagamentos(ActionEvent event) {
        nav(event, "/com/condominio/financeiro/pagamentos-view.fxml", "Pagamentos");
    }
    @FXML private void abrirInadimplencia(ActionEvent event) {
        nav(event, "/com/condominio/financeiro/inadimplencia-view.fxml", "Inadimplência");
    }
    @FXML private void abrirDespesas(ActionEvent event) {
        nav(event, "/com/condominio/financeiro/despesas-view.fxml", "Despesas");
    }
    @FXML private void abrirRateio(ActionEvent event) {
        nav(event, "/com/condominio/financeiro/rateio-view.fxml", "Rateio");
    }
    @FXML private void voltarHome(ActionEvent event) {
        nav(event, "/com/condominio/home-view.fxml", "CondoSoft");
    }

    private void nav(ActionEvent event, String fxml, String titulo) {
        try {
            URL url = getClass().getResource(fxml);
            if (url == null) return;
            Parent root = FXMLLoader.load(url);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CondoSoft – " + titulo);
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }
}
