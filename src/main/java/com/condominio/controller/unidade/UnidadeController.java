package com.condominio.controller.unidade;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UnidadeController {

    public void voltarHome(ActionEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    public void abrirCadastrarUnidade(ActionEvent event) {
        trocarTela(event, "/com/condominio/unidade/cadastrar-unidade-view.fxml");
    }

    public void abrirListarUnidades(ActionEvent event) {
        trocarTela(event, "/com/condominio/unidade/listar-unidade-view.fxml");
    }

    private void trocarTela(ActionEvent event, String caminhoFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));

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