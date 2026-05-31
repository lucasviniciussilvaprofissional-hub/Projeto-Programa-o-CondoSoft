package com.condominio.controller.unidade;

import com.condominio.models.moradia.Unidade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class DetalhesUnidadeController {
    @FXML
    private Label lblNomeUnidade;

    @FXML
    private Label lblTipo;

    @FXML
    private Label lblArea;

    @FXML
    private Label lblCapacidade;

    @FXML
    private Label lblStatusUnidade;

    @FXML
    private Label lblQtdMoradores;

    @FXML
    private Label lblFracaoIdeal;

    @FXML
    private Label lblBadgeUnidade;

    @FXML
    private Label lblBreadcrumbUnidade;



    public void voltarHome(javafx.scene.input.MouseEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    public void voltarUnidades(ActionEvent event) {
        trocarTela(event, "/com/condominio/unidade/unidade-view.fxml");
    }

    public void voltarUnidades(javafx.scene.input.MouseEvent event) {
        trocarTela(event, "/com/condominio/unidade/unidade-view.fxml");
    }

    public void voltarListagem(ActionEvent event) {
        trocarTela(event, "/com/condominio/unidade/listar-unidade-view.fxml");
    }

    public void voltarListagem(javafx.scene.input.MouseEvent event) {
        trocarTela(event, "/com/condominio/unidade/listar-unidade-view.fxml");
    }

    public void editarUnidade(ActionEvent event) {
        trocarTela(event, "/com/condominio/unidade/cadastrar-unidade-view.fxml");
    }

    public void abrirGerenciarMoradores(ActionEvent event) {
        System.out.println("Abrir gerenciamento de moradores");
        trocarTela(event, "/com/condominio/gerenciar-moradores-unidade-view.fxml");
    }

    public void abrirGerenciarVeiculos(ActionEvent event) {
        System.out.println("Abrir gerenciamento de veículos");
        trocarTela(event, "/com/condominio/veiculo-view.fxml");
    }

    public void abrirGerenciarPets(ActionEvent event) {
        System.out.println("Abrir gerenciamento de pets");
        trocarTela(event, "/com/condominio/pet-view.fxml");
    }

    public void confirmarExclusaoUnidade(ActionEvent event) {
        System.out.println("Excluir unidade");
        // colocar Alert de confirmação aqui depois
    }

    public void setUnidade(Unidade unidade) {

        lblNomeUnidade.setText(
                "Unidade " + unidade.getNumero()
        );

        lblTipo.setText(
                unidade.getTipo().toString()
        );

        lblArea.setText(
                unidade.getMetragem()
        );

        lblCapacidade.setText(
                String.valueOf(
                        unidade.getCapacidade()
                )
        );

        lblStatusUnidade.setText(
                unidade.getStatus().toString()
        );

        lblQtdMoradores.setText(
                unidade.quantidadeMoradores()
                        + " moradores"
        );

        lblFracaoIdeal.setText(
                String.valueOf(
                        unidade.getFracaoIdeal()
                )
        );

        lblBadgeUnidade.setText(
                unidade.getNumero()
                        + " - "
                        + unidade.getBloco()
        );

        lblBreadcrumbUnidade.setText(
                "Unidade " + unidade.getNumero()
        );
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