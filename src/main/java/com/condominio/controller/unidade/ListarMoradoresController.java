package com.condominio.controller.unidade;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ListarMoradoresController {

    /* ==========================
       NAVEGAÇÃO
    ========================== */

    public void voltarHome(javafx.scene.input.MouseEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    public void voltarUnidades(javafx.scene.input.MouseEvent event) {
        trocarTela(event, "/com/condominio/unidade/unidade-view.fxml");
    }

    public void voltarDetalhesUnidade(ActionEvent event) {
        trocarTela(event, "/com/condominio/unidade/detalhes-unidade-view.fxml");
    }

    public void voltarDetalhesUnidade(javafx.scene.input.MouseEvent event) {
        trocarTela(event, "/com/condominio/unidade/detalhes-unidade-view.fxml");
    }

    public void abrirCadastrarMorador(ActionEvent event) {
        trocarTela(event, "/com/condominio/cadastrar-morador-view.fxml");
    }

    public void abrirDetalhesMorador(ActionEvent event) {
        System.out.println("Abrir detalhes do morador");
        // trocarTela(event, "/com/condominio/detalhes-morador-view.fxml");
    }

    public void editarMorador(ActionEvent event) {
        System.out.println("Editar morador");
        // trocarTela(event, "/com/condominio/editar-morador-view.fxml");
    }

    public void removerMorador(ActionEvent event) {
        System.out.println("Remover morador");
    }

    /* ==========================
       FILTROS
    ========================== */

    public void filtrarMoradores(ActionEvent event) {
        System.out.println("Filtrando moradores...");
    }

    public void filtrarMoradores(KeyEvent event) {
        System.out.println("Filtrando moradores...");
    }

    public void limparFiltros(ActionEvent event) {
        System.out.println("Filtros limpos.");
    }

    /* ==========================
       MÉTODO AUXILIAR
    ========================== */

    private void trocarTela(ActionEvent event, String caminhoFXML) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource(caminhoFXML)
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

    private void trocarTela(javafx.scene.input.MouseEvent event, String caminhoFXML) {
        try {
            Parent root = FXMLLoader.load(
                    getClass().getResource(caminhoFXML)
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