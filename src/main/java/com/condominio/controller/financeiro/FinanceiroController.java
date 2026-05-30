package com.condominio.controller.financeiro;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class FinanceiroController {

    public void voltarHome(ActionEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    public void voltarHome(MouseEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    public void abrirGerarBoleto(ActionEvent event) {
        System.out.println("Abrir tela Gerar Boleto");
        trocarTela(event, "/com/condominio/financeiro/gerar-boleto-view.fxml");
    }

    public void abrirPagamentos(ActionEvent event) {
        System.out.println("Abrir tela Pagamentos");
        // trocarTela(event, "/com/condominio/financeiro/pagamentos-view.fxml");
    }

    public void abrirInadimplencia(ActionEvent event) {
        System.out.println("Abrir tela Inadimplência");
        trocarTela(event, "/com/condominio/financeiro/inadimplencia-view.fxml");
    }

    private void trocarTela(ActionEvent event, String caminhoFXML) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(caminhoFXML));

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.out.println("Erro ao abrir: " + caminhoFXML);
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
            System.out.println("Erro ao abrir: " + caminhoFXML);
            e.printStackTrace();
        }
    }
}