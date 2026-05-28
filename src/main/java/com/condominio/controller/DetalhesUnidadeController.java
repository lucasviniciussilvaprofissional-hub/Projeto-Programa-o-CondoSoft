package com.condominio.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DetalhesUnidadeController {

    public void voltarHome(javafx.scene.input.MouseEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    public void voltarUnidades(ActionEvent event) {
        trocarTela(event, "/com/condominio/unidade-view.fxml");
    }

    public void voltarUnidades(javafx.scene.input.MouseEvent event) {
        trocarTela(event, "/com/condominio/unidade-view.fxml");
    }

    public void voltarListagem(ActionEvent event) {
        trocarTela(event, "/com/condominio/listar-unidade-view.fxml");
    }

    public void voltarListagem(javafx.scene.input.MouseEvent event) {
        trocarTela(event, "/com/condominio/listar-unidade-view.fxml");
    }

    public void editarUnidade(ActionEvent event) {
        trocarTela(event, "/com/condominio/cadastrar-unidade-view.fxml");
    }

    public void abrirGerenciarMoradores(ActionEvent event) {
        System.out.println("Abrir gerenciamento de moradores");
        // trocarTela(event, "/com/condominio/morador-view.fxml");
    }

    public void abrirGerenciarVeiculos(ActionEvent event) {
        System.out.println("Abrir gerenciamento de veículos");
        // trocarTela(event, "/com/condominio/veiculo-view.fxml");
    }

    public void abrirGerenciarPets(ActionEvent event) {
        System.out.println("Abrir gerenciamento de pets");
        // trocarTela(event, "/com/condominio/pet-view.fxml");
    }

    public void confirmarExclusaoUnidade(ActionEvent event) {
        System.out.println("Excluir unidade");
        // colocar Alert de confirmação aqui depois
    }

    private void trocarTela(javafx.event.Event event, String caminhoFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(caminhoFXML)
            );

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao abrir: " + caminhoFXML);
        }
    }
}