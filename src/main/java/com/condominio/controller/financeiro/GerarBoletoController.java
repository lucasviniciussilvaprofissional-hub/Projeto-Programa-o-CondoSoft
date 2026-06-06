package com.condominio.controller.financeiro;

import com.condominio.enums.StatusBoleto;
import com.condominio.models.finance.Boleto;
import com.condominio.models.moradia.Unidade;
import com.condominio.repository.implementation.BoletoRepositoryImpl;
import com.condominio.repository.implementation.UnidadeRepositoryImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class GerarBoletoController implements Initializable {

    @FXML
    private ComboBox<Unidade> cbUnidade;

    @FXML
    private Label lblFracaoIdeal;

    @FXML
    private Label lblValorSugerido;

    @FXML
    private TextField txtValor;

    @FXML
    private DatePicker dpVencimento;

    @FXML
    private Button btnGerar;

    @FXML
    private Button btnGerarTodos;

    @FXML
    private Button btnVoltar;

    private final UnidadeRepositoryImpl unidadeRepository =
            new UnidadeRepositoryImpl();

    private final BoletoRepositoryImpl boletoRepository =
            new BoletoRepositoryImpl();

    private static final double VALOR_TOTAL_CONDOMINIO = 10000.0;

    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {

        carregarUnidades();

        cbUnidade.setOnAction(
                event -> atualizarInformacoes()
        );

        dpVencimento.setValue(
                LocalDate.now().plusDays(10)
        );
    }

    private void carregarUnidades() {

        cbUnidade.getItems().addAll(
                unidadeRepository.listar()
        );
    }

    private void atualizarInformacoes() {

        Unidade unidade =
                cbUnidade.getValue();

        if (unidade == null) {
            return;
        }

        lblFracaoIdeal.setText(
                String.valueOf(
                        unidade.getFracaoIdeal()
                )
        );

        double valorSugerido = VALOR_TOTAL_CONDOMINIO/unidade.getFracaoIdeal();

        lblValorSugerido.setText(
                String.format(
                        "R$ %.2f",
                        valorSugerido
                )
        );

        txtValor.setText(
                String.format(
                        "%.2f",
                        valorSugerido
                )
        );
    }

    @FXML
    private void gerarBoleto() {

        Unidade unidade =
                cbUnidade.getValue();

        if (unidade == null) {

            Alert alert =
                    new Alert(
                            Alert.AlertType.WARNING
                    );

            alert.setContentText(
                    "Selecione uma unidade."
            );

            alert.showAndWait();

            return;
        }

        Boleto boleto =
                new Boleto(
                        boletoRepository.listar().size() + 1,
                        "BOL" + System.currentTimeMillis(),
                        Float.parseFloat(
                                txtValor.getText()
                        ),
                        "06/2026",
                        dpVencimento.getValue(),
                        StatusBoleto.PENDENTE
                );

        boleto.setUnidade(unidade);

        boletoRepository.salvar(boleto);

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle("Sucesso");

        alert.setHeaderText(
                "Boleto Gerado"
        );

        alert.setContentText(
                "Boleto criado para a unidade "
                        + unidade.getNumero()
        );

        alert.showAndWait();
    }

    @FXML
    private void gerarTodosBoletos() {

        int quantidade = 0;

        for (Unidade unidade :
                unidadeRepository.listar()) {

            float valor =
                    (float)
                            (VALOR_TOTAL_CONDOMINIO/unidade.getFracaoIdeal());

            Boleto boleto =
                    new Boleto(
                            boletoRepository.listar().size() + 1,
                            "BOL" + System.nanoTime(),
                            valor,
                            "06/2026",
                            dpVencimento.getValue(),
                            StatusBoleto.PENDENTE
                    );

            boleto.setUnidade(unidade);

            boletoRepository.salvar(
                    boleto
            );

            quantidade++;
        }

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                "Envio realizado"
        );

        alert.setHeaderText(
                "Boletos enviados"
        );

        alert.setContentText(
                quantidade
                        + " boletos gerados e enviados por e-mail (simulação)."
        );

        alert.showAndWait();
    }


    @FXML
    public void voltarFinanceiro(ActionEvent event) {
        trocarTela(
                event,
                "/com/condominio/financeiro/financeiro-view.fxml"
        );
    }


    // ───── utilidades ──────────────────────────────────────────────────────
    private void mostrarAviso(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void trocarTela(javafx.event.Event event,
                            String fxml) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(fxml)
                    );

            Parent root =
                    loader.load();

            Stage stage =
                    (Stage)
                            ((Node) event.getSource())
                                    .getScene()
                                    .getWindow();

            stage.setScene(
                    new Scene(root)
            );

            stage.show();

        } catch (IOException e) {

            e.printStackTrace();

            System.out.println(
                    "Erro ao abrir: "
                            + fxml
            );
        }
    }
}