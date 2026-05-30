package com.condominio.controller.financeiro;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class GerarBoletoController {

    public void voltarHome(MouseEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    public void voltarFinanceiro(ActionEvent event) {
        trocarTela(event, "/com/condominio/financeiro/financeiro-view.fxml");
    }

    public void voltarFinanceiro(MouseEvent event) {
        trocarTela(event, "/com/condominio/financeiro/financeiro-view.fxml");
    }

    public void preencherDadosUnidade(ActionEvent event) {}

    public void calcularTotal() {}

    public void limparFormulario(ActionEvent event) {}

    public void gerarBoleto(ActionEvent event) {}

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

    private void trocarTela(MouseEvent event, String caminhoFXML) {
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