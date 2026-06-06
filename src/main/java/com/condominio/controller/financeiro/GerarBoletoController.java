package com.condominio.controller.financeiro;

import com.condominio.models.moradia.Unidade;
import com.condominio.models.finance.Rateio;
import com.condominio.repository.implementation.*;
import com.condominio.service.FinanceiroService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class GerarBoletoController implements Initializable {

    @FXML private ComboBox<Unidade> cbUnidade;
    @FXML private Label             lblFracaoIdeal;
    @FXML private Label             lblValorSugerido;
    @FXML private TextField         txtValor;
    @FXML private DatePicker        dpVencimento;
    @FXML private TextField         txtCompetencia;

    private final FinanceiroService service = new FinanceiroService(
            new BoletoRepositoryImpl(), new DespesaRepositoryImpl(),
            new RateioRepositoryImpl(), new PagamentoRepositoryImpl(),
            new InadimplenciaRepositoryImpl());

    private final UnidadeRepositoryImpl unidadeRepo = new UnidadeRepositoryImpl();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbUnidade.getItems().addAll(unidadeRepo.listar());

        // Dispara a atualização sempre que mudar a unidade OU digitar uma nova competência
        cbUnidade.setOnAction(e -> atualizarInfos());
        txtCompetencia.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // Quando o campo perde o foco
                atualizarInfos();
            }
        });

        dpVencimento.setValue(LocalDate.now().plusDays(10));
        txtCompetencia.setText(String.format("%02d/%d",
                LocalDate.now().getMonthValue(), LocalDate.now().getYear()));
    }

    private void atualizarInfos() {
        Unidade u = cbUnidade.getValue();
        if (u == null) return;

        lblFracaoIdeal.setText(String.format("%.4f", u.getFracaoIdeal()));

        String comp = txtCompetencia.getText().trim();
        if (comp.isEmpty()) {
            lblValorSugerido.setText("R$ 0,00");
            txtValor.setText("0.00");
            return;
        }

        // 1. Busca todos os rateios já calculados para a competência informada
        List<Rateio> rateiosDaCompetencia = service.listarRateiosPorCompetencia(comp);

        // 2. Filtra os rateios específicos desta unidade e soma o valor rateado de cada despesa
        double valorSugeridoSomado = rateiosDaCompetencia.stream()
                .filter(r -> r.getUnidade() != null && r.getUnidade().getId() == u.getId())
                .mapToDouble(Rateio::getValorRateado)
                .sum();

        // 3. Atualiza a interface gráfica com o valor real calculado dinamicamente
        lblValorSugerido.setText(String.format("R$ %.2f", valorSugeridoSomado));
        txtValor.setText(String.format("%.2f", valorSugeridoSomado).replace(",", "."));
    }

    @FXML
    private void gerarBoleto() {
        Unidade u = cbUnidade.getValue();
        if (u == null) { alerta("Selecione uma unidade."); return; }
        float valor = parseValor(); if (valor <= 0) return;
        String comp = txtCompetencia.getText().trim();
        if (comp.isEmpty()) { alerta("Informe a competência."); return; }

        service.gerarBoleto(u, valor, comp, dpVencimento.getValue());
        new Alert(Alert.AlertType.INFORMATION,
                "Boleto gerado para Apto " + u.getNumero(), ButtonType.OK).showAndWait();
    }

    @FXML
    private void gerarTodosBoletos() {
        List<Unidade> unidades = unidadeRepo.listar();
        if (unidades.isEmpty()) { alerta("Nenhuma unidade cadastrada."); return; }
        String comp = txtCompetencia.getText().trim();
        if (comp.isEmpty()) { alerta("Informe a competência."); return; }

        // Para gerar todos, precisamos descobrir o valor TOTAL das despesas daquela competência para passar ao método genérico
        List<Rateio> rateiosDaCompetencia = service.listarRateiosPorCompetencia(comp);

        if (rateiosDaCompetencia.isEmpty()) {
            alerta("Não existem rateios calculados para a competência " + comp + ". Calcule-os na tela de Rateio primeiro.");
            return;
        }

        // Soma o valor total de todas as despesas mapeadas na competência
        double valorTotalDespesas = rateiosDaCompetencia.stream()
                .mapToDouble(Rateio::getValorRateado)
                .sum();

        var gerados = service.gerarBoletosTodos(unidades, (float) valorTotalDespesas,
                comp, dpVencimento.getValue());
        new Alert(Alert.AlertType.INFORMATION,
                gerados.size() + " boletos gerados automaticamente baseados no rateio somado.", ButtonType.OK).showAndWait();
    }

    @FXML
    public void voltarFinanceiro(ActionEvent event) {
        nav(event, "/com/condominio/financeiro/financeiro-view.fxml");
    }

    private float parseValor() {
        try { return Float.parseFloat(txtValor.getText().replace(",", ".")); }
        catch (NumberFormatException e) { alerta("Valor inválido."); return -1; }
    }

    private void alerta(String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait();
    }

    private void nav(ActionEvent event, String fxml) {
        try {
            FXMLLoader l = new FXMLLoader(getClass().getResource(fxml));
            Parent r = l.load();
            Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
            s.setScene(new Scene(r)); s.show();
        } catch (IOException e) { e.printStackTrace(); }
    }
}