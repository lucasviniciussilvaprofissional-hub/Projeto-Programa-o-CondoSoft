package com.condominio.controller.reserva;

import com.condominio.enums.StatusReserva;
import com.condominio.models.area.Reserva;
import com.condominio.models.area.TaxaLimpeza;
import com.condominio.repository.implementation.BoletoRepositoryImpl;
import com.condominio.repository.implementation.ReservaRepositoryImpl;
import com.condominio.repository.implementation.TaxaLimpezaRepositoryImpl;
import com.condominio.service.ReservaService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ListaReservaController {

    // ── FXML ──────────────────────────────────────────────────────────────
    @FXML private TableView<Reserva>           tvReservas;
    @FXML private TableColumn<Reserva, String> colId;
    @FXML private TableColumn<Reserva, String> colArea;
    @FXML private TableColumn<Reserva, String> colUnidade;
    @FXML private TableColumn<Reserva, String> colResponsavel;
    @FXML private TableColumn<Reserva, String> colInicio;
    @FXML private TableColumn<Reserva, String> colFim;
    @FXML private TableColumn<Reserva, String> colPessoas;
    @FXML private TableColumn<Reserva, String> colStatus;

    @FXML private ComboBox<String> cmbFiltroStatus;
    @FXML private Label lblTotalAtivas;
    @FXML private Label lblTotalCanceladas;
    @FXML private Label lblTotalFinalizadas;
    @FXML private Label lblInfo;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM HH:mm");

    private final ReservaService service = new ReservaService(
            new ReservaRepositoryImpl(),
            new TaxaLimpezaRepositoryImpl(),
            new BoletoRepositoryImpl());

    // ── init ──────────────────────────────────────────────────────────────
    @FXML
    public void initialize() {
        cmbFiltroStatus.setItems(FXCollections.observableArrayList(
                "Todas", "ATIVA", "CANCELADA", "FINALIZADA"));
        cmbFiltroStatus.getSelectionModel().selectFirst();

        configurarColunas();
        carregarTabela();

        tvReservas.getSelectionModel().selectedItemProperty().addListener((obs, old, novo) -> {
            if (novo != null)
                lblInfo.setText("Selecionada: #" + novo.getId()
                        + " – " + novo.getNomeArea()
                        + " | " + novo.getConvidados().size() + " convidado(s)");
        });
    }

    private void configurarColunas() {
        colId.setCellValueFactory(c -> new SimpleStringProperty("#" + c.getValue().getId()));
        colArea.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNomeArea()));
        colUnidade.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getUnidade() != null
                        ? "Apto " + c.getValue().getUnidade().getNumero() : "—"));
        colResponsavel.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNomeResponsavel()));
        colInicio.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDataInicio() != null ? c.getValue().getDataInicio().format(FMT) : "—"));
        colFim.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getDataFim() != null ? c.getValue().getDataFim().format(FMT) : "—"));
        colPessoas.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getQuantidadePessoas() + " pess."));
        colStatus.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getStatus().name()));

        // destaque por status
        tvReservas.setRowFactory(tv -> new TableRow<>() {
            @Override protected void updateItem(Reserva r, boolean empty) {
                super.updateItem(r, empty);
                if (r == null || empty) { setStyle(""); return; }
                switch (r.getStatus()) {
                    case ATIVA       -> setStyle("-fx-background-color: #DCFCE7;");
                    case CANCELADA   -> setStyle("-fx-background-color: #FEE2E2;");
                    case FINALIZADA  -> setStyle("-fx-background-color: #E0E7FF;");
                }
            }
        });
    }

    private void carregarTabela() {
        String f = cmbFiltroStatus.getValue();
        List<Reserva> dados;

        if (f == null || "Todas".equals(f)) {
            dados = service.listarReservas();
        } else {
            dados = service.listarPorStatus(StatusReserva.valueOf(f));
        }

        tvReservas.setItems(FXCollections.observableArrayList(dados));

        long ativas      = service.listarPorStatus(StatusReserva.ATIVA).size();
        long canceladas  = service.listarPorStatus(StatusReserva.CANCELADA).size();
        long finalizadas = service.listarPorStatus(StatusReserva.FINALIZADA).size();
        lblTotalAtivas.setText(String.valueOf(ativas));
        lblTotalCanceladas.setText(String.valueOf(canceladas));
        lblTotalFinalizadas.setText(String.valueOf(finalizadas));
    }

    // ── ações ─────────────────────────────────────────────────────────────
    @FXML
    public void filtrarReservas(ActionEvent event) { carregarTabela(); }

    @FXML
    public void cancelarReservaSelecionada(ActionEvent event) {
        Reserva sel = tvReservas.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("Selecione uma reserva."); return; }
        if (sel.getStatus() != StatusReserva.ATIVA) { alerta("Apenas reservas ATIVAS podem ser canceladas."); return; }

        Alert conf = new Alert(Alert.AlertType.CONFIRMATION,
                "Cancelar a reserva #" + sel.getId() + " – " + sel.getNomeArea() + "?",
                ButtonType.YES, ButtonType.NO);
        conf.showAndWait().ifPresent(r -> {
            if (r == ButtonType.YES) {
                service.cancelarReserva(sel.getId());
                carregarTabela();
            }
        });
    }

    /** REQ10 — Finaliza e lança taxa de limpeza automaticamente */
    @FXML
    public void finalizarReservaSelecionada(ActionEvent event) {
        Reserva sel = tvReservas.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("Selecione uma reserva."); return; }
        if (sel.getStatus() != StatusReserva.ATIVA) { alerta("Apenas reservas ATIVAS podem ser finalizadas."); return; }

        Alert conf = new Alert(Alert.AlertType.CONFIRMATION,
                "Finalizar reserva #" + sel.getId() + "?\nUma taxa de limpeza será lançada automaticamente.",
                ButtonType.YES, ButtonType.NO);
        conf.showAndWait().ifPresent(r -> {
            if (r == ButtonType.YES) {
                TaxaLimpeza taxa = service.finalizarReserva(sel.getId());
                new Alert(Alert.AlertType.INFORMATION,
                        "Reserva finalizada!\nTaxa de limpeza de R$ "
                        + String.format("%.2f", taxa.getValor()) + " lançada.",
                        ButtonType.OK).showAndWait();
                carregarTabela();
            }
        });
    }

    @FXML public void verConvidados(ActionEvent event) {
        Reserva sel = tvReservas.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("Selecione uma reserva."); return; }

        StringBuilder sb = new StringBuilder("Convidados da reserva #" + sel.getId() + ":\n\n");
        if (sel.getConvidados().isEmpty()) {
            sb.append("Nenhum convidado registrado.");
        } else {
            sel.getConvidados().forEach(c ->
                    sb.append("• ").append(c.getNome())
                      .append(c.getDocumento() != null && !c.getDocumento().isEmpty()
                              ? " (" + c.getDocumento() + ")" : "").append("\n"));
        }
        new Alert(Alert.AlertType.INFORMATION, sb.toString(), ButtonType.OK).showAndWait();
    }

    @FXML public void atualizar(ActionEvent event) { carregarTabela(); }

    // ── navegação ─────────────────────────────────────────────────────────
    @FXML public void voltarHome(ActionEvent event) { nav(event, "/com/condominio/home-view.fxml"); }
    @FXML public void novaReserva(ActionEvent event) { nav(event, "/com/condominio/reserva/nova-reserva-view.fxml"); }
    @FXML public void abrirAgenda(ActionEvent event) { nav(event, "/com/condominio/reserva/agenda-area-view.fxml"); }

    // ── util ──────────────────────────────────────────────────────────────
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
