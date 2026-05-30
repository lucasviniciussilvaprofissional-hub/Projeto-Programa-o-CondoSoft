package com.condominio.controller.reserva;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class AgendaAreaController {

    public VBox vboxAgendaConteudo;

    public void voltarHome(ActionEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    public void voltarHome(MouseEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    public void voltarReservas(ActionEvent event) {
        trocarTela(event, "/com/condominio/reserva-view.fxml");
    }

    public void voltarReservas(MouseEvent event) {
        trocarTela(event, "/com/condominio/reserva-view.fxml");
    }

    public void abrirNovaReserva(ActionEvent event) {
        trocarTela(event, "/com/condominio/cadastrar-reserva-view.fxml");
    }

    public void carregarAgenda(ActionEvent event) {
        System.out.println("Carregar agenda");
    }

    public void semanaAnterior(ActionEvent event) {
        System.out.println("Semana anterior");
    }

    public void proximaSemana(ActionEvent event) {
        System.out.println("Próxima semana");
    }

    public void irParaHoje(ActionEvent event) {
        System.out.println("Ir para hoje");
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