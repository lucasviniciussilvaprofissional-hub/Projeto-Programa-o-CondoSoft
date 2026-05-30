package com.condominio.controller.financeiro;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class BalanceteController {

    public void voltarHome(ActionEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    public void voltarHome(MouseEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    public void voltarRelatorios(ActionEvent event) {
        trocarTela(event, "/com/condominio/financeiro/relatorios-view.fxml");
    }

    public void voltarRelatorios(MouseEvent event) {
        trocarTela(event, "/com/condominio/financeiro/relatorios-view.fxml");
    }

    public void gerarBalancete(ActionEvent event) {
        System.out.println("Gerar balancete");
    }

    public void exportarPDF(ActionEvent event) {
        System.out.println("Exportar PDF");
    }

    public void filtrarLancamentos(ActionEvent event) {
        System.out.println("Filtrar lançamentos");
    }

    private void trocarTela(ActionEvent event, String caminhoFXML) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(caminhoFXML));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

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

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.out.println("Erro ao abrir: " + caminhoFXML);
            e.printStackTrace();
        }
    }
}