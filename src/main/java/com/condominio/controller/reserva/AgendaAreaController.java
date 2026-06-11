package com.condominio.controller.reserva;

import com.condominio.models.area.AreaComum;
import com.condominio.models.area.Reserva;
import com.condominio.repository.implementation.BoletoRepositoryImpl;
import com.condominio.repository.implementation.ReservaRepositoryImpl;
import com.condominio.repository.implementation.TaxaLimpezaRepositoryImpl;
import com.condominio.service.ReservaService;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AgendaAreaController {

    // ── FXML ──────────────────────────────────────────────────────────────
    @FXML public VBox  vboxAgendaConteudo;
    @FXML private ComboBox<String> cmbAreaFiltro;
    @FXML private DatePicker       dpSemana;
    @FXML private Label lblDia1, lblDia2, lblDia3,
            lblDia4, lblDia5, lblDia6, lblDia7;

    // ── áreas ─────────────────────────────────────────────────────────────
    private static final List<AreaComum> AREAS = List.of(
            new AreaComum("Salão de Festas", 80),
            new AreaComum("Churrasqueira",   30),
            new AreaComum("Piscina",         50),
            new AreaComum("Quadra",          20),
            new AreaComum("Espaço Gourmet",  40)
    );

    private final ReservaService service = new ReservaService(
            new ReservaRepositoryImpl(),
            new TaxaLimpezaRepositoryImpl(),
            new BoletoRepositoryImpl());

    private LocalDate semanaAtual = LocalDate.now();
    private static final DateTimeFormatter FMT_DIA  = DateTimeFormatter.ofPattern("EEE dd/MM");
    private static final DateTimeFormatter FMT_HORA = DateTimeFormatter.ofPattern("HH:mm");

    // ── init ──────────────────────────────────────────────────────────────
    @FXML
    public void initialize() {
        cmbAreaFiltro.getItems().add("Todas as Áreas");
        for (AreaComum a : AREAS) cmbAreaFiltro.getItems().add(a.getNome());
        cmbAreaFiltro.getSelectionModel().selectFirst();

        dpSemana.setValue(semanaAtual);
        atualizarCabecalhos();
        renderizarAgenda();
    }

    // ── navegação de semana ───────────────────────────────────────────────
    @FXML public void semanaAnterior(ActionEvent event) { semanaAtual = semanaAtual.minusWeeks(1); recarregar(); }
    @FXML public void proximaSemana(ActionEvent event)  { semanaAtual = semanaAtual.plusWeeks(1);  recarregar(); }
    @FXML public void irParaHoje(ActionEvent event)     { semanaAtual = LocalDate.now();           recarregar(); }

    @FXML public void carregarAgenda(ActionEvent event) {
        if (dpSemana.getValue() != null) semanaAtual = dpSemana.getValue();
        recarregar();
    }

    private void recarregar() {
        dpSemana.setValue(semanaAtual);
        atualizarCabecalhos();
        renderizarAgenda();
    }

    private void atualizarCabecalhos() {
        LocalDate dom = semanaAtual.with(DayOfWeek.SUNDAY);
        Label[] labels = {lblDia1, lblDia2, lblDia3, lblDia4, lblDia5, lblDia6, lblDia7};
        for (int i = 0; i < 7; i++)
            if (labels[i] != null) labels[i].setText(dom.plusDays(i).format(FMT_DIA));
    }

    private void renderizarAgenda() {
        if (vboxAgendaConteudo == null) return;
        vboxAgendaConteudo.getChildren().clear();

        String filtro = cmbAreaFiltro.getValue();
        LocalDate dom = semanaAtual.with(DayOfWeek.SUNDAY);
        LocalDate sab = dom.plusDays(6);

        List<Reserva> todas = service.listarReservas();
        int exibidas = 0;

        for (Reserva r : todas) {
            if (r.getDataInicio() == null) continue;

            LocalDate diaR = r.getDataInicio().toLocalDate();
            if (diaR.isBefore(dom) || diaR.isAfter(sab)) continue;

            boolean passaFiltro = filtro == null || "Todas as Áreas".equals(filtro)
                    || r.getNomeArea().equals(filtro);
            if (!passaFiltro) continue;

            String cor = switch (r.getStatus()) {
                case ATIVA      -> "#DCFCE7";
                case CANCELADA  -> "#FEE2E2";
                case FINALIZADA -> "#E0E7FF";
            };

            Label card = new Label(
                    r.getDataInicio().format(FMT_DIA) + "  "
                            + r.getDataInicio().format(FMT_HORA) + "–"
                            + r.getDataFim().format(FMT_HORA) + "  │  "
                            + r.getNomeArea() + "  │  "
                            + r.getNomeResponsavel()
                            + "  │  " + r.getQuantidadePessoas() + " pess."
                            + "  │  [" + r.getStatus() + "]");
            card.setMaxWidth(Double.MAX_VALUE);
            card.setWrapText(true);
            card.setStyle("-fx-background-color:" + cor + ";"
                    + "-fx-background-radius:8;"
                    + "-fx-padding:10 14;"
                    + "-fx-font-size:12;"
                    + "-fx-border-color:#CBD5E1;"
                    + "-fx-border-radius:8;"
                    + "-fx-border-width:1;");
            vboxAgendaConteudo.getChildren().add(card);
            exibidas++;
        }

        if (exibidas == 0) {
            Label vazio = new Label("Nenhuma reserva encontrada para esta semana / filtro.");
            vazio.setStyle("-fx-text-fill:#94A3B8; -fx-font-size:13; -fx-padding:20;");
            vboxAgendaConteudo.getChildren().add(vazio);
        }
    }

    // ── navegação corrigida para a lista principal de reservas ────────────
    @FXML public void abrirNovaReserva(ActionEvent event) { nav(event, "/com/condominio/reserva/nova-reserva-view.fxml"); }
    @FXML public void voltarHome(ActionEvent event)       { nav(event, "/com/condominio/reserva/lista-reserva-view.fxml"); }
    @FXML public void voltarHome(MouseEvent event)        { nav(event, "/com/condominio/reserva/lista-reserva-view.fxml"); }
    @FXML public void voltarReservas(ActionEvent event)   { nav(event, "/com/condominio/reserva/lista-reserva-view.fxml"); }
    @FXML public void voltarReservas(MouseEvent event)    { nav(event, "/com/condominio/reserva/lista-reserva-view.fxml"); }

    // ── util ──────────────────────────────────────────────────────────────
    private void nav(javafx.event.Event event, String fxml) {
        try {
            FXMLLoader l = new FXMLLoader(getClass().getResource(fxml));
            Parent r = l.load();
            Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
            s.setScene(new Scene(r)); s.show();
        } catch (IOException e) { e.printStackTrace(); }
    }
}