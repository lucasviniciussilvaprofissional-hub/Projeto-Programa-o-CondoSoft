package com.condominio.controller.reserva;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ReservaController {

    /* ==========================
       NAVEGAÇÃO
    ========================== */

    public void voltarHome(ActionEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    public void voltarHome(MouseEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    public void abrirNovaReserva(ActionEvent event) {
        trocarTela(event,
                "/com/condominio/reserva/nova-reserva-view.fxml");
    }

    public void abrirListaReservas(ActionEvent event) {
        trocarTela(event,
                "/com/condominio/reserva/lista-reserva-view.fxml");
    }

    public void abrirAgendaArea(ActionEvent event) {
        trocarTela(event,
                "/com/condominio/reserva/agenda-area-view.fxml");
    }

    /* ==========================
       MÉTODO AUXILIAR
    ========================== */

    private void trocarTela(ActionEvent event, String caminhoFXML) {
        try {
            Parent root = FXMLLoader.load(ReservaController.class.getResource(caminhoFXML)
            );

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
            Parent root = FXMLLoader.load(ReservaController.class.getResource(caminhoFXML)
            );

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