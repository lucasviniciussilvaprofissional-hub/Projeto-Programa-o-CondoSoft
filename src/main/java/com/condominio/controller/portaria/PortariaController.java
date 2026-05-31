package com.condominio.controller.portaria;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class PortariaController {

    // 1. BOTÃO DE ENCOMENDAS
    @FXML
    private void abrirEncomendas(ActionEvent event) {
        System.out.println("[CondoSoft] Abrindo tela de Encomendas...");
        carregarTela(event, "/com/condominio/portaria/encomendas-view.fxml", "Controle de Encomendas");
    }

    // 2. BOTÃO DE CONTROLE DE ACESSO
    @FXML
    private void abrirControleAcesso(ActionEvent event) {
        System.out.println("[CondoSoft] Abrindo tela de Controle de Acesso...");
        carregarTela(event, "/com/condominio/portaria/controle-acesso-view.fxml", "Controle de Acesso");
    }

    // 3. BOTÃO DE OCORRÊNCIAS
    @FXML
    private void abrirOcorrencias(ActionEvent event) {
        System.out.println("[CondoSoft] Abrindo tela de Ocorrências...");
        carregarTela(event, "/com/condominio/portaria/ocorrencia-view.fxml", "Livro de Ocorrências");
    }

    // 4. CORREÇÃO DO ERRO DA LINHA 43: Ação do Botão Voltar comum
    @FXML
    private void voltarHome(ActionEvent event) {
        processarRetornoHome(event.getSource());
    }

    // 5. CORREÇÃO DO ERRO DA LINHA 62: Ação do clique de mouse direto no componente/ícone
    @FXML
    private void voltarHomeMouse(MouseEvent event) {
        processarRetornoHome(event.getSource());
    }

    /**
     * Lógica centralizada para retornar à Home principal do CondoSoft
     */
    private void processarRetornoHome(Object source) {
        System.out.println("[CondoSoft] Redirecionando para a Home Inicial...");
        try {
            // Varre o local correto da home na estrutura do seu pacote
            URL fxmlUrl = PortariaController.class.getResource("/com/condominio/home-view.fxml");

            if (fxmlUrl == null) {
                fxmlUrl = PortariaController.class.getClassLoader().getResource("com/condominio/home-view.fxml");
            }

            if (fxmlUrl == null) {
                throw new IOException("O arquivo home-view.fxml não foi localizado na raiz de views.");
            }

            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage) ((Node) source).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CondoSoft - Home");
            stage.show();

        } catch (IOException e) {
            System.out.println("[ERRO] Erro ao carregar tela Home: " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Navegação");
            alert.setHeaderText("Não foi possível voltar para a Home");
            alert.setContentText("Verifique o arquivo home-view.fxml.\nDetalhes: " + e.getLocalizedMessage());
            alert.showAndWait();
        }
    }

    /**
     * Método utilitário para troca de telas das opções da portaria
     */
    private void carregarTela(ActionEvent event, String caminhoFxml, String nomeTela) {
        try {
            URL fxmlUrl = PortariaController.class.getResource(caminhoFxml);

            if (fxmlUrl == null) {
                fxmlUrl = PortariaController.class.getClassLoader().getResource(caminhoFxml.substring(1));
            }

            if (fxmlUrl == null) {
                throw new IOException("Arquivo FXML não encontrado em: " + caminhoFxml);
            }

            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CondoSoft - " + nomeTela);
            stage.show();

        } catch (IOException e) {
            System.out.println("[ERRO] Erro ao carregar a tela " + nomeTela + ": " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Carregamento");
            alert.setHeaderText("Não foi possível abrir: " + nomeTela);
            alert.setContentText("Detalhes: " + e.getLocalizedMessage());
            alert.showAndWait();
        }
    }
}