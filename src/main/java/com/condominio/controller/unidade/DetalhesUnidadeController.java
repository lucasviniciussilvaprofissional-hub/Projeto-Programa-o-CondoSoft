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
    @FXML private Label lblContadorMoradores;
    @FXML private Label lblContadorVeiculos;
    @FXML private Label lblContadorPets;

    private Unidade unidadeAtual;

    // ════════════════════════════════════════════════════════════════════════
    // CORREÇÃO: O JavaFX chama este método automaticamente ao carregar a tela!
    // ════════════════════════════════════════════════════════════════════════
    @FXML
    public void initialize() {
        // Se voltamos de VeiculoController, pegamos a unidade salva globalmente lá
        Unidade global = VeiculoController.getUnidadeSelecionadaGlobal();

        if (global != null) {
            setUnidade(global);
        }
    }

    // Métodos de Inicialização / Dados
    public void setUnidade(Unidade unidade) {
        this.unidadeAtual = unidade;

        if (lblNomeUnidade != null) lblNomeUnidade.setText("Unidade " + unidade.getNumero());
        if (lblTipo != null) lblTipo.setText(unidade.getTipo() != null ? unidade.getTipo().toString() : "");
        if (lblArea != null) lblArea.setText(unidade.getMetragem());
        if (lblCapacidade != null) lblCapacidade.setText(String.valueOf(unidade.getCapacidade()));
        if (lblStatusUnidade != null) lblStatusUnidade.setText(unidade.getStatus() != null ? unidade.getStatus().toString() : "");
        if (lblQtdMoradores != null) lblQtdMoradores.setText(unidade.quantidadeMoradores() + " morador(es)");
        if (lblFracaoIdeal != null) lblFracaoIdeal.setText(String.valueOf(unidade.getFracaoIdeal()));
        if (lblBadgeUnidade != null) lblBadgeUnidade.setText(unidade.getNumero() + " - " + unidade.getBloco());
        if (lblBreadcrumbUnidade != null) lblBreadcrumbUnidade.setText("Unidade " + unidade.getNumero());

        // Opcional: Atualizar os contadores da tela de detalhes se os métodos existirem no seu Model
        // if (lblContadorVeiculos != null) lblContadorVeiculos.setText(String.valueOf(unidade.getVeiculos().size()));
    }

    // Navegação para sub-telas (Passando a unidade)
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

    // Ações do Breadcrumb
    @FXML
    public void voltarHome(MouseEvent event) {
        trocarTelaSimples(event, "/com/condominio/home-view.fxml");
    }

    @FXML
    public void voltarUnidades(MouseEvent event) {
        trocarTelaSimples(event, "/com/condominio/unidade/unidade-view.fxml");
    }

    @FXML
    public void voltarListagem(MouseEvent event) {
        trocarTelaSimples(event, "/com/condominio/unidade/listar-unidade-view.fxml");
    }

    @FXML
    public void voltarListagemBotao(ActionEvent event) {
        trocarTelaSimples(event, "/com/condominio/unidade/listar-unidade-view.fxml");
    }

    @FXML
    public void editarUnidade(ActionEvent event) {
        trocarTelaSimples(event, "/com/condominio/unidade/cadastrar-unidade-view.fxml");
    }

    @FXML
    public void confirmarExclusaoUnidade(ActionEvent event) {
        System.out.println("Excluir unidade? Implementar confirma Alert aqui");
    }

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