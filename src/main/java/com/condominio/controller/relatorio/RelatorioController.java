package com.condominio.controller.relatorio;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class RelatorioController {

    @FXML private Label lblArrecadacao;
    @FXML private Label lblReservas;
    @FXML private Label lblOcorrencias;
    @FXML private Label lblInadimplencia;

    @FXML
    public void initialize() {
        System.out.println("[CondoSoft] Inicializando Central de Relatórios com valores zerados...");

        // Configura tudo para iniciar zerado conforme solicitado
        if (lblArrecadacao != null) lblArrecadacao.setText("R$ 0,00");
        if (lblReservas != null) lblReservas.setText("0");
        if (lblOcorrencias != null) lblOcorrencias.setText("0");
        if (lblInadimplencia != null) lblInadimplencia.setText("R$ 0,00");
    }

    @FXML
    private void voltarHome(ActionEvent event) {
        retornarParaHome(event.getSource());
    }

    @FXML
    private void voltarHomeMouse(MouseEvent event) {
        retornarParaHome(event.getSource());
    }

    @FXML
    private void abrirBalancete(ActionEvent event) {
        System.out.println("[CondoSoft] Navegando para Balancete...");
        carregarTela(event, "/com/condominio/financeiro/balancete-view.fxml", "Balancete Financeiro");
    }

    @FXML
    private void abrirReservas(ActionEvent event) {
        System.out.println("[CondoSoft] Navegando para Relatório de Reservas...");
        carregarTela(event, "/com/condominio/reserva/lista-reserva-view.fxml", "Relatório de Reservas");
    }

    @FXML
    private void abrirInadimplencia(ActionEvent event) {
        System.out.println("[CondoSoft] Navegando para Inadimplência...");
        carregarTela(event, "/com/condominio/financeiro/inadimplencia-view.fxml", "Relatório de Inadimplência");
    }

    private void retornarParaHome(Object origem) {
        String[] caminhosPossiveis = {
                "/com/condominio/home-view.fxml",
                "/com/condominio/unidade/home-view.fxml",
                "/home-view.fxml"
        };

        URL urlFxml = null;
        for (String caminho : caminhosPossiveis) {
            urlFxml = getClass().getResource(caminho);
            if (urlFxml != null) break;
        }

        try {
            if (urlFxml == null) {
                throw new IOException("O arquivo da tela Home não foi encontrado.");
            }
            Parent raiz = FXMLLoader.load(urlFxml);
            Stage palco = (Stage) ((Node) origem).getScene().getWindow();
            palco.setScene(new Scene(raiz));
            palco.setTitle("CondoSoft - Home");
            palco.show();
        } catch (IOException e) {
            exibirErro("Erro de Navegação", "Não foi possível retornar para a tela inicial.", e.getMessage());
        }
    }

    private void carregarTela(ActionEvent evento, String caminhoFxml, String titulo) {
        URL urlFxml = getClass().getResource(caminhoFxml);
        if (urlFxml == null) {
            String caminhoLimpo = caminhoFxml.startsWith("/") ? caminhoFxml.substring(1) : caminhoFxml;
            urlFxml = getClass().getClassLoader().getResource(caminhoLimpo);
        }

        try {
            if (urlFxml == null) {
                throw new IOException("Não foi possível localizar o FXML: " + caminhoFxml);
            }
            Parent raiz = FXMLLoader.load(urlFxml);
            Stage palco = (Stage) ((Node) evento.getSource()).getScene().getWindow();
            palco.setScene(new Scene(raiz));
            palco.setTitle("CondoSoft - " + titulo);
            palco.show();
        } catch (IOException e) {
            e.printStackTrace();
            exibirErro("Falha de Módulo", "Erro ao carregar a tela: " + titulo, e.getMessage());
        }
    }

    private void exibirErro(String cabecalho, String contexto, String detalhes) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro Operacional");
        alerta.setHeaderText(cabecalho);
        alerta.setContentText(contexto + "\n\nDetalhes: " + detalhes);
        alerta.showAndWait();
    }
}