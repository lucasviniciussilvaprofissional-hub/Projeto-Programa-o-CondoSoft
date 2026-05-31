package com.condominio.controller.financeiro;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class PagamentosController {

    @FXML private TableView<ItemLinhaPagamento> tvPagamentos;
    @FXML private TableColumn<ItemLinhaPagamento, String> colDescricao;
    @FXML private TableColumn<ItemLinhaPagamento, String> colVencimento;
    @FXML private TableColumn<ItemLinhaPagamento, String> colPagamento;
    @FXML private TableColumn<ItemLinhaPagamento, String> colValor;
    @FXML private TableColumn<ItemLinhaPagamento, String> colStatus;

    @FXML
    public void initialize() {
        System.out.println("[CondoSoft] Inicializando tabelas de Pagamento...");

        if (colDescricao != null) colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        if (colVencimento != null) colVencimento.setCellValueFactory(new PropertyValueFactory<>("dataVencimento"));
        if (colPagamento != null) colPagamento.setCellValueFactory(new PropertyValueFactory<>("dataPagamento"));
        if (colValor != null) colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        if (colStatus != null) colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        if (tvPagamentos != null) {
            ObservableList<ItemLinhaPagamento> dados = FXCollections.observableArrayList(
                    new ItemLinhaPagamento("Taxa Condominial - Maio/2026", "10/05/2026", "08/05/2026", "R$ 350,00", "PAGO"),
                    new ItemLinhaPagamento("Taxa Condominial - Abril/2026", "10/04/2026", "09/04/2026", "R$ 350,00", "PAGO"),
                    new ItemLinhaPagamento("Taxa Condominial - Junho/2026", "10/06/2026", "-", "R$ 350,00", "PENDENTE")
            );
            tvPagamentos.setItems(dados);
        }
    }

    @FXML
    private void voltarFinanceiro(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/condominio/financeiro/financeiro-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class ItemLinhaPagamento {
        private String descricao;
        private String dataVencimento;
        private String dataPagamento;
        private String valor;
        private String status;

        public ItemLinhaPagamento(String d, String v, String p, String val, String s) {
            this.descricao = d;
            this.dataVencimento = v;
            this.dataPagamento = p;
            this.valor = val;
            this.status = s;
        }

        public String getDescricao() { return descricao; }
        public String getDataVencimento() { return dataVencimento; }
        public String getDataPagamento() { return dataPagamento; }
        public String getValor() { return valor; }
        public String getStatus() { return status; }
    }
}