package com.condominio.controller.unidade;

import com.condominio.models.moradia.Unidade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class DetalhesUnidadeController {

    @FXML private Label lblNomeUnidade;
    @FXML private Label lblTipo;
    @FXML private Label lblArea;
    @FXML private Label lblCapacidade;
    @FXML private Label lblStatusUnidade;
    @FXML private Label lblQtdMoradores;
    @FXML private Label lblFracaoIdeal;
    @FXML private Label lblBadgeUnidade;
    @FXML private Label lblBreadcrumbUnidade;

    private Unidade unidadeAtual;

    // ───── setUnidade (chamado pelo controller anterior) ───────────────────
    public void setUnidade(Unidade unidade) {
        this.unidadeAtual = unidade;

        lblNomeUnidade.setText("Unidade " + unidade.getNumero());
        lblTipo.setText(unidade.getTipo().toString());
        lblArea.setText(unidade.getMetragem());
        lblCapacidade.setText(String.valueOf(unidade.getCapacidade()));
        lblStatusUnidade.setText(unidade.getStatus().toString());
        lblQtdMoradores.setText(unidade.quantidadeMoradores() + " morador(es)");
        lblFracaoIdeal.setText(String.valueOf(unidade.getFracaoIdeal()));
        lblBadgeUnidade.setText(unidade.getNumero() + " - " + unidade.getBloco());
        lblBreadcrumbUnidade.setText("Unidade " + unidade.getNumero());
    }

    // ───── navegação para sub-telas (passando a unidade) ──────────────────
    @FXML
    public void abrirGerenciarMoradores(ActionEvent event) {
        GerenciarMoradoresUnidadeController.setUnidadeSelecionadaGlobal(unidadeAtual);
        CadastrarMoradorController.setUnidadeSelecionadaGlobal(unidadeAtual);
        trocarTelaSimples(event, "/com/condominio/unidade/gerenciar-moradores-unidade-view.fxml");
    }

    @FXML
    public void abrirGerenciarVeiculos(ActionEvent event) {
        VeiculoController.setUnidadeSelecionadaGlobal(unidadeAtual);
        trocarTelaSimples(event, "/com/condominio/unidade/veiculo-view.fxml");
    }

    @FXML
    public void abrirGerenciarPets(ActionEvent event) {
        PetController.setUnidadeSelecionadaGlobal(unidadeAtual);
        trocarTelaSimples(event, "/com/condominio/unidade/pet-view.fxml");
    }

    // ───── navegação genérica ──────────────────────────────────────────────
    @FXML
    public void voltarHome(MouseEvent event) {
        trocarTelaSimples(event, "/com/condominio/home-view.fxml");
    }

    @FXML
    public void voltarUnidades(ActionEvent event) {
        trocarTelaSimples(event, "/com/condominio/unidade/unidade-view.fxml");
    }

    @FXML
    public void voltarUnidades(MouseEvent event) {
        trocarTelaSimples(event, "/com/condominio/unidade/unidade-view.fxml");
    }

    @FXML
    public void voltarListagem(ActionEvent event) {
        trocarTelaSimples(event, "/com/condominio/unidade/listar-unidade-view.fxml");
    }

    @FXML
    public void voltarListagem(MouseEvent event) {
        trocarTelaSimples(event, "/com/condominio/unidade/listar-unidade-view.fxml");
    }

    @FXML
    public void editarUnidade(ActionEvent event) {
        trocarTelaSimples(event, "/com/condominio/unidade/cadastrar-unidade-view.fxml");
    }

    @FXML
    public void confirmarExclusaoUnidade(ActionEvent event) {
        System.out.println("Excluir unidade – implementar confirma Alert aqui");
    }

    // ───── util ───────────────────────────────────────────────────────────
    private void trocarTelaSimples(javafx.event.Event event, String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao abrir: " + fxml);
        }
    }
}
