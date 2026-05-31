package com.condominio.controller.financeiro;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GerarBoletoController {

    @FXML private Label lblVencimento;
    @FXML private Label lblDataDocumento;
    @FXML private Label lblNossoNumero;
    @FXML private Label lblValorDocumento;
    @FXML private Label lblLinhaDigitavel;

    @FXML
    public void initialize() {
        System.out.println("[CondoSoft] Gerando visualização em GerarBoletoController...");

        LocalDate hoje = LocalDate.now();
        LocalDate vencimento = hoje.plusDays(5);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (lblDataDocumento != null) lblDataDocumento.setText(hoje.format(formatter));
        if (lblVencimento != null) lblVencimento.setText(vencimento.format(formatter));
        if (lblNossoNumero != null) lblNossoNumero.setText("09/24018523-1");
        if (lblValorDocumento != null) lblValorDocumento.setText("R$ 350,00");
        if (lblLinhaDigitavel != null) lblLinhaDigitavel.setText("34191.79001 01043.513184 91020.150008 7 97330000035000");
    }

    @FXML
    private void imprimirBoleto(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("CondoSoft - Impressão");
        alert.setHeaderText(null);
        alert.setContentText("Enviando documento para a impressora padrão...");
        alert.showAndWait();
    }

    @FXML
    private void voltarHome(ActionEvent event) {
        String[] caminhosHome = {
                "/com/condominio/home-view.fxml",
                "/com/condominio/unidade/home-view.fxml",
                "/home-view.fxml"
        };

        URL urlHome = null;
        for (String caminho : caminhosHome) {
            urlHome = getClass().getResource(caminho);
            if (urlHome != null) break;
        }

        try {
            if (urlHome == null) throw new IOException("Arquivo 'home-view.fxml' não encontrado.");
            Parent root = FXMLLoader.load(urlHome);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CondoSoft - Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}