package com.condominio.controller.financeiro;

import com.condominio.enums.StatusBoleto;
import com.condominio.models.finance.Boleto;
import com.condominio.repository.implementation.BoletoRepositoryImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FinanceiroController implements Initializable {

    @FXML
    private Label lblArrecadado;

    @FXML
    private Label lblInadimplencia;

    @FXML
    private Label lblBoletosAbertos;

    private final BoletoRepositoryImpl boletoRepository =
            new BoletoRepositoryImpl();

    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {

        atualizarDashboard();
    }

    private void atualizarDashboard() {

        float arrecadado = 0;

        float inadimplencia = 0;

        int boletosAbertos = 0;

        for (Boleto boleto :
                boletoRepository.listar()) {

            if (boleto.getStatus() ==
                    StatusBoleto.PAGO) {

                arrecadado += boleto.getValor();
            }

            if (boleto.getStatus() ==
                    StatusBoleto.PENDENTE) {

                inadimplencia += boleto.getValor();

                boletosAbertos++;
            }

            if (boleto.getStatus() ==
                    StatusBoleto.VENCIDO) {

                inadimplencia += boleto.getValor();

                boletosAbertos++;
            }
        }

        lblArrecadado.setText(
                String.format(
                        "R$ %.2f",
                        arrecadado
                )
        );

        lblInadimplencia.setText(
                String.format(
                        "R$ %.2f",
                        inadimplencia
                )
        );

        lblBoletosAbertos.setText(
                String.valueOf(
                        boletosAbertos
                )
        );
    }

    @FXML
    private void abrirGerarBoleto(ActionEvent event) {
        abrirTela(
                event,
                "/com/condominio/financeiro/gerar-boleto-view.fxml",
                "CondoSoft - Gerar Boleto"
        );
    }

    @FXML
    private void abrirTelaPagamentos(ActionEvent event) {
        abrirTela(
                event,
                "/com/condominio/financeiro/pagamentos-view.fxml",
                "CondoSoft - Pagamentos"
        );
    }

    @FXML
    private void abrirInadimplencia(ActionEvent event) {
        abrirTela(
                event,
                "/com/condominio/financeiro/inadimplencia-view.fxml",
                "CondoSoft - Inadimplência"
        );
    }

    @FXML
    private void voltarHome(ActionEvent event) {

        abrirTela(
                event,
                "/com/condominio/home-view.fxml",
                "CondoSoft"
        );
    }

    private void abrirTela(ActionEvent event,
                           String caminho,
                           String titulo) {

        try {

            URL url =
                    getClass().getResource(caminho);

            if (url == null) {
                return;
            }

            Parent root =
                    FXMLLoader.load(url);

            Stage stage =
                    (Stage) ((Node)
                            event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(
                    new Scene(root)
            );

            stage.setTitle(titulo);

            stage.show();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}