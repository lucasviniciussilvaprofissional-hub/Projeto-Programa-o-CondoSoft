package com.condominio.controller.financeiro;

import com.condominio.models.relatorio.Balancete;
import com.condominio.models.finance.Despesa;
import com.condominio.models.finance.Pagamento;
import com.condominio.repository.implementation.BoletoRepositoryImpl;
import com.condominio.repository.implementation.DespesaRepositoryImpl;
import com.condominio.repository.implementation.InadimplenciaRepositoryImpl;
import com.condominio.repository.implementation.PagamentoRepositoryImpl;
import com.condominio.service.RelatorioService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class BalanceteController implements Initializable {

    @FXML private ComboBox<String> cbMes;
    @FXML private ComboBox<String> cbAno;

    @FXML private Label lblTotalArrecadado;
    @FXML private Label lblTotalDespesas;
    @FXML private Label lblSaldo;
    @FXML private Label lblInadimplencia;
    @FXML private Label lblStatus;

    @FXML private TableView<String[]> tabelaLancamentos;
    @FXML private TableColumn<String[], String> colData;
    @FXML private TableColumn<String[], String> colDescricao;
    @FXML private TableColumn<String[], String> colCategoria;
    @FXML private TableColumn<String[], String> colTipo;
    @FXML private TableColumn<String[], String> colValor;
    @FXML private TableColumn<String[], String> colSaldoAcum;

    private static final String[] MESES = {
            "Janeiro","Fevereiro","Marco","Abril","Maio","Junho",
            "Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"
    };

    private final RelatorioService service = new RelatorioService(
            new BoletoRepositoryImpl(),
            new DespesaRepositoryImpl(),
            new PagamentoRepositoryImpl(),
            new InadimplenciaRepositoryImpl()
    );

    private Balancete balanceteAtual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        cbMes.setItems(FXCollections.observableArrayList(MESES));
        cbMes.setValue(MESES[LocalDate.now().getMonthValue() - 1]);

        cbAno.setItems(FXCollections.observableArrayList(
                "2024","2025","2026","2027","2028"
        ));

        cbAno.setValue(String.valueOf(LocalDate.now().getYear()));

        configurarColunas();

        setStatus(
                "Selecione o período e clique em Gerar Balancete.",
                false
        );
    }

    private void configurarColunas() {

        if (colData == null) return;

        colData.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue()[0])
        );

        colDescricao.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue()[1])
        );

        colCategoria.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue()[2])
        );

        colTipo.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue()[3])
        );

        colValor.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue()[4])
        );

        colSaldoAcum.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue()[5])
        );
    }

    @FXML
    private void gerarBalancete(ActionEvent event) {

        int mes = cbMes.getSelectionModel().getSelectedIndex() + 1;

        int ano;

        try {
            ano = Integer.parseInt(cbAno.getValue());
        } catch (Exception e) {
            alerta("Ano inválido.");
            return;
        }

        balanceteAtual = service.gerarBalancete(mes, ano);

        lblTotalArrecadado.setText(
                String.format(
                        "R$ %.2f",
                        balanceteAtual.getTotalReceitas()
                )
        );

        lblTotalDespesas.setText(
                String.format(
                        "R$ %.2f",
                        balanceteAtual.getTotalDespesas()
                )
        );

        lblSaldo.setText(
                String.format(
                        "R$ %.2f",
                        balanceteAtual.getSaldoFinal()
                )
        );

        lblInadimplencia.setText(
                String.format(
                        "R$ %.2f",
                        service.calcularInadimplenciaTotal()
                )
        );

        preencherTabela();

        setStatus(
                "Balancete gerado com sucesso.",
                false
        );
    }

    private void preencherTabela() {

        if (tabelaLancamentos == null || balanceteAtual == null)
            return;

        ObservableList<String[]> linhas =
                FXCollections.observableArrayList();

        double saldo = 0;

        if (balanceteAtual.getPagamentos() != null) {

            for (Pagamento p : balanceteAtual.getPagamentos()) {

                saldo += p.getValorPago();

                String data =
                        p.getDataPagamento() != null
                                ? p.getDataPagamento().toLocalDate().toString()
                                : "";

                String descricao =
                        p.getBoleto() != null &&
                                p.getBoleto().getUnidade() != null
                                ? "Apto " + p.getBoleto().getUnidade().getNumero()
                                : "Pagamento";

                linhas.add(new String[]{
                        data,
                        descricao,
                        "Taxa Condominial",
                        "RECEITA",
                        String.format("R$ %.2f", p.getValorPago()),
                        String.format("R$ %.2f", saldo)
                });
            }
        }

        if (balanceteAtual.getDespesas() != null) {

            for (Despesa d : balanceteAtual.getDespesas()) {

                saldo -= d.getValor();

                linhas.add(new String[]{
                        d.getDataVencimento() != null
                                ? d.getDataVencimento().toString()
                                : "",
                        d.getDescricao(),
                        d.getTipo() != null
                                ? d.getTipo().name()
                                : "",
                        "DESPESA",
                        String.format("R$ %.2f", d.getValor()),
                        String.format("R$ %.2f", saldo)
                });
            }
        }

        tabelaLancamentos.setItems(linhas);
    }

    @FXML
    private void exportarPdf(ActionEvent event) {

        if (balanceteAtual == null) {
            alerta("Gere o balancete primeiro.");
            return;
        }

        String pasta = escolherPasta(event);

        if (pasta == null)
            return;

        try {

            String caminho =
                    service.gerarPdfBalancete(
                            balanceteAtual,
                            pasta
                    );

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(caminho));
            }

            setStatus(
                    "Arquivo salvo em: " + caminho,
                    false
            );

        } catch (Exception e) {

            alerta(
                    "Erro ao exportar PDF: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    @FXML
    private void exportarInadimplentesCSV(ActionEvent event) {

        String pasta = escolherPasta(event);

        if (pasta == null)
            return;

        try {

            String caminho =
                    service.exportarInadimplentesCSV(
                            pasta
                    );

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(caminho));
            }

            setStatus(
                    "CSV exportado: " + caminho,
                    false
            );

        } catch (Exception e) {

            alerta(
                    "Erro ao exportar CSV: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    @FXML
    private void generarBalancete(ActionEvent event) {
        gerarBalancete(event);
    }

    @FXML
    private void voltarRelatorios(ActionEvent event) {
        nav(event,
                "/com/condominio/relatorio/relatorio-view.fxml");
    }

    private String escolherPasta(ActionEvent event) {

        DirectoryChooser dc =
                new DirectoryChooser();

        dc.setTitle(
                "Escolha a pasta"
        );

        dc.setInitialDirectory(
                new File(
                        System.getProperty("user.home")
                )
        );

        Stage stage =
                (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        File pasta = dc.showDialog(stage);

        return pasta != null
                ? pasta.getAbsolutePath()
                : null;
    }

    private void setStatus(
            String msg,
            boolean erro) {

        if (lblStatus == null)
            return;

        lblStatus.setText(msg);

        lblStatus.setStyle(
                erro
                        ? "-fx-text-fill:red;"
                        : "-fx-text-fill:green;"
        );
    }

    private void alerta(String msg) {

        new Alert(
                Alert.AlertType.WARNING,
                msg,
                ButtonType.OK
        ).showAndWait();
    }

    private void nav(
            ActionEvent event,
            String fxml) {

        try {

            URL url =
                    getClass().getResource(fxml);

            Parent root =
                    FXMLLoader.load(url);

            Stage stage =
                    (Stage) ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(
                    new Scene(root)
            );

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();

            alerta(
                    "Erro ao navegar: "
                            + e.getMessage()
            );
        }
    }
}