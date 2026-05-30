package com.condominio.controller.financeiro;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class PagamentosController {

    public void voltarFinanceiro(ActionEvent event) {
        trocarTela(event, "/com/condominio/financeiro/financeiro-view.fxml");
    }

    public void carregarBoletosUnidade(ActionEvent event) {}

    public void preencherValorBoleto(ActionEvent event) {}

    public void registrarPagamento(ActionEvent event) {}

    public void filtrarPagamentos(ActionEvent event) {}

    public void filtrarPagamentos(KeyEvent event) {}

    private void trocarTela(ActionEvent event, String caminhoFXML) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(caminhoFXML));

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}