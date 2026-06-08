package com.condominio.controller.relatorio;

import com.condominio.repository.implementation.BoletoRepositoryImpl;
import com.condominio.repository.implementation.DespesaRepositoryImpl;
import com.condominio.repository.implementation.InadimplenciaRepositoryImpl;
import com.condominio.repository.implementation.PagamentoRepositoryImpl;
import com.condominio.service.RelatorioService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RelatorioController implements Initializable {

    @FXML private Label lblArrecadacao;
    @FXML private Label lblReservas;
    @FXML private Label lblOcorrencias;
    @FXML private Label lblInadimplencia;

    private final RelatorioService service = new RelatorioService(
            new BoletoRepositoryImpl(),
            new DespesaRepositoryImpl(),
            new PagamentoRepositoryImpl(),
            new InadimplenciaRepositoryImpl());

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Arrecadacao: soma de pagamentos registrados
        double arrec = new PagamentoRepositoryImpl().listar().stream()
                .mapToDouble(p -> p.getValorPago()).sum();
        if (lblArrecadacao != null) lblArrecadacao.setText(String.format("R$ %.2f", arrec));

        // Inadimplencia total pendente
        double inadim = service.listarInadimplenciasPendentes().stream()
                .mapToDouble(i -> i.getValorDevido()).sum();
        if (lblInadimplencia != null) lblInadimplencia.setText(String.format("R$ %.2f", inadim));

        if (lblReservas != null)   lblReservas.setText("—");
        if (lblOcorrencias != null) lblOcorrencias.setText("—");
    }

    @FXML private void voltarHome(ActionEvent event) { nav(event, "/com/condominio/home-view.fxml"); }
    @FXML private void voltarHomeMouse(MouseEvent event) { navMouse(event, "/com/condominio/home-view.fxml"); }

    @FXML private void abrirBalancete(ActionEvent event) {
        nav(event, "/com/condominio/financeiro/balancete-view.fxml");
    }

    @FXML private void abrirReservas(ActionEvent event) {
        nav(event, "/com/condominio/reserva/lista-reserva-view.fxml");
    }

    @FXML private void abrirInadimplencia(ActionEvent event) {
        nav(event, "/com/condominio/financeiro/inadimplencia-view.fxml");
    }

    private void nav(ActionEvent event, String fxml) {
        try {
            URL u = getClass().getResource(fxml);
            if (u == null) throw new IOException("Nao encontrado: " + fxml);
            Parent root = FXMLLoader.load(u);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Erro ao abrir tela: " + e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    private void navMouse(MouseEvent event, String fxml) {
        try {
            URL u = getClass().getResource(fxml);
            if (u == null) throw new IOException("Nao encontrado: " + fxml);
            Parent root = FXMLLoader.load(u);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }
}
