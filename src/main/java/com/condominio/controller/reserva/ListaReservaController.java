package com.condominio.controller.reserva;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ListaReservaController {

    public void voltarHome(ActionEvent event) throws IOException {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    public void voltarHome(MouseEvent event) throws IOException {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    public void voltarReservas(ActionEvent event) throws IOException {
        trocarTela(event, "/com/condominio/reserva-view.fxml");
    }

    public void voltarReservas(MouseEvent event) throws IOException {
        trocarTela(event, "/com/condominio/reserva-view.fxml");
    }

    public void abrirNovaReserva(ActionEvent event) throws IOException {
        trocarTela(event, "/com/condominio/cadastrar-reserva-view.fxml");
    }

    public void filtrarReservas(ActionEvent event) {
        System.out.println("Filtrando reservas...");
    }

    public void filtrarReservas(KeyEvent event) {
        System.out.println("Filtrando reservas...");
    }

    public void limparFiltros(ActionEvent event) {
        System.out.println("Limpando filtros...");
    }

    public void verDetalhesReserva(ActionEvent event) {
        System.out.println("Ver detalhes da reserva...");
    }

    public void cancelarReserva(ActionEvent event) {
        System.out.println("Cancelar reserva...");
    }

    private void trocarTela(ActionEvent event, String caminhoFXML) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(caminhoFXML));

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }

    private void trocarTela(MouseEvent event, String caminhoFXML) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(caminhoFXML));

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.setScene(new Scene(root));
        stage.show();
    }
}