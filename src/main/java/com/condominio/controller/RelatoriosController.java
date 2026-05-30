package com.condominio.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class RelatoriosController {

    public void voltarHome(ActionEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    public void voltarHomeMouse(MouseEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    public void abrirBalancete(ActionEvent event) {
        trocarTela(event, "/com/condominio/financeiro/balancete-view.fxml");
    }

    public void abrirReservas(ActionEvent event) {
        trocarTela(event, "/com/condominio/reservas/lista-reserva-view.fxml");
    }

    public void abrirInadimplencia(ActionEvent event) {
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