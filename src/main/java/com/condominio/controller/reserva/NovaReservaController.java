package com.condominio.controller.reserva;

import com.condominio.models.area.AreaComum;
import com.condominio.models.area.Convidado;
import com.condominio.models.area.Reserva;
import com.condominio.models.moradia.Morador;
import com.condominio.models.moradia.Unidade;
import com.condominio.repository.implementation.BoletoRepositoryImpl;
import com.condominio.repository.implementation.MoradorRepositoryImpl;
import com.condominio.repository.implementation.ReservaRepositoryImpl;
import com.condominio.repository.implementation.TaxaLimpezaRepositoryImpl;
import com.condominio.service.ReservaService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NovaReservaController {

    // ── FXML ──────────────────────────────────────────────────────────────
    @FXML private ComboBox<String>  cmbAreaComum;
    @FXML private ComboBox<String>  cmbUnidade;      // populado do repositório
    @FXML private ComboBox<String>  cmbHorarioInicio;
    @FXML private ComboBox<String>  cmbHorarioFim;
    @FXML private DatePicker        dpDataReserva;
    @FXML private TextField         txtNumConvidados;
    @FXML private TextArea          txtObservacoes;

    // Convidados (REQ09)
    @FXML private TextField         txtNomeConvidado;
    @FXML private TextField         txtDocConvidado;
    @FXML private TableView<String> tabelaConvidados;
    @FXML private TableColumn<String, String> colNomeConvidado;
    @FXML private TableColumn<String, String> colDocConvidado;
    @FXML private TableColumn<String, String> colRemoverConvidado;
    @FXML private Label             lblContadorConvidados;

    // Disponibilidade
    @FXML private Label             lblDisponibilidade;
    @FXML private HBox              hboxDisponibilidade;

    // ── áreas comuns com capacidade ───────────────────────────────────────
    private static final List<AreaComum> AREAS = List.of(
            new AreaComum("Salão de Festas",  80),
            new AreaComum("Churrasqueira",    30),
            new AreaComum("Piscina",          50),
            new AreaComum("Quadra",           20),
            new AreaComum("Espaço Gourmet",   40)
    );

    private static final List<String> HORARIOS = List.of(
            "08:00","09:00","10:00","11:00","12:00","13:00",
            "14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00"
    );

    // ── service + repos ───────────────────────────────────────────────────
    private final ReservaService service = new ReservaService(
            new ReservaRepositoryImpl(),
            new TaxaLimpezaRepositoryImpl(),
            new BoletoRepositoryImpl());

    private final MoradorRepositoryImpl moradorRepo = new MoradorRepositoryImpl();

    // reserva salva na sessão para adição de convidados após criação
    private Reserva reservaCriada = null;

    // ── init ──────────────────────────────────────────────────────────────
    @FXML
    public void initialize() {
        // Áreas
        cmbAreaComum.getItems().clear();
        for (AreaComum a : AREAS) cmbAreaComum.getItems().add(a.getNome());

        // REQ08/REQ16 — Unidades via repositório (moradores com unidade)
        cmbUnidade.getItems().clear();
        for (Morador m : moradorRepo.listar()) {
            if (m.getUnidade() != null)
                cmbUnidade.getItems().add(labelMorador(m));
        }
        if (cmbUnidade.getItems().isEmpty())
            cmbUnidade.getItems().add("(Nenhum morador cadastrado)");

        // Horários (08:00–21:00, respeitando REQ21)
        cmbHorarioInicio.setItems(FXCollections.observableArrayList(HORARIOS.subList(0, HORARIOS.size()-1)));
        cmbHorarioFim.setItems(FXCollections.observableArrayList(HORARIOS.subList(1, HORARIOS.size())));

        hboxDisponibilidade.setVisible(false);
        hboxDisponibilidade.setManaged(false);

        // Colunas da tabela de convidados (REQ09)
        if (colNomeConvidado != null)
            colNomeConvidado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().split("\\|")[0].trim()));
        if (colDocConvidado != null)
            colDocConvidado.setCellValueFactory(c -> {
                String[] p = c.getValue().split("\\|");
                return new SimpleStringProperty(p.length > 1 ? p[1].trim() : "");
            });

        atualizarContadorConvidados();
    }

    private String labelMorador(Morador m) {
        return m.getNome() + " – Apto " + m.getUnidade().getNumero()
                + " / " + m.getUnidade().getBloco();
    }

    // ── verificar disponibilidade ─────────────────────────────────────────
    @FXML
    public void verificarDisponibilidade(ActionEvent event) {
        if (cmbAreaComum.getValue() == null
                || dpDataReserva.getValue() == null
                || cmbHorarioInicio.getValue() == null
                || cmbHorarioFim.getValue() == null) return;

        hboxDisponibilidade.setVisible(true);
        hboxDisponibilidade.setManaged(true);

        try {
            AreaComum area  = areaByNome(cmbAreaComum.getValue());
            LocalDateTime i = toDateTime(dpDataReserva.getValue(), cmbHorarioInicio.getValue());
            LocalDateTime f = toDateTime(dpDataReserva.getValue(), cmbHorarioFim.getValue());

            boolean livre = service.isDisponivel(area, i, f);
            lblDisponibilidade.setText(livre
                    ? "✔ Horário disponível para reserva"
                    : "✘ Conflito: área já reservada neste período");
            lblDisponibilidade.setStyle(livre
                    ? "-fx-text-fill:#059669; -fx-font-weight:bold;"
                    : "-fx-text-fill:#DC2626; -fx-font-weight:bold;");
        } catch (Exception e) {
            lblDisponibilidade.setText("⚠ " + e.getMessage());
            lblDisponibilidade.setStyle("-fx-text-fill:#D97706;");
        }
    }

    // ── adicionar convidado REQ09 ─────────────────────────────────────────
    @FXML
    public void adicionarConvidado(ActionEvent event) {
        String nome = txtNomeConvidado.getText().trim();
        if (nome.isEmpty()) return;
        String doc = txtDocConvidado.getText().trim();

        // Se a reserva já foi criada, adiciona via service (valida capacidade)
        if (reservaCriada != null) {
            try {
                service.adicionarConvidado(reservaCriada.getId(), nome, doc);
            } catch (IllegalArgumentException e) { alerta(e.getMessage()); return; }
        }

        tabelaConvidados.getItems().add(nome + " | " + doc);
        txtNomeConvidado.clear();
        txtDocConvidado.clear();
        atualizarContadorConvidados();
    }

    // ── confirmar reserva REQ08 ───────────────────────────────────────────
    @FXML
    public void confirmarReserva(ActionEvent event) {
        if (cmbAreaComum.getValue() == null || cmbUnidade.getValue() == null
                || dpDataReserva.getValue() == null
                || cmbHorarioInicio.getValue() == null || cmbHorarioFim.getValue() == null) {
            alerta("Preencha Área, Unidade/Responsável, Data e Horários."); return;
        }

        Morador responsavel = moradorByLabel(cmbUnidade.getValue());
        if (responsavel == null) { alerta("Morador não encontrado."); return; }

        try {
            AreaComum area  = areaByNome(cmbAreaComum.getValue());
            LocalDateTime i = toDateTime(dpDataReserva.getValue(), cmbHorarioInicio.getValue());
            LocalDateTime f = toDateTime(dpDataReserva.getValue(), cmbHorarioFim.getValue());

            int qtd = 1;
            try { qtd = Integer.parseInt(txtNumConvidados.getText().trim()); } catch (Exception ignored) {}

            reservaCriada = service.criarReserva(responsavel, area, i, f, qtd);

            // Adiciona convidados já digitados na tabela
            for (String alignment : tabelaConvidados.getItems()) {
                String[] p = alignment.split("\\|");
                String nome = p[0].trim();
                String doc  = p.length > 1 ? p[1].trim() : "";
                if (!nome.isEmpty()) {
                    try { service.adicionarConvidado(reservaCriada.getId(), nome, doc); }
                    catch (IllegalArgumentException ignored) {}
                }
            }

            new Alert(Alert.AlertType.INFORMATION,
                    "Reserva #" + reservaCriada.getId() + " criada com sucesso!\n"
                            + "Área: " + area.getNome() + "\n"
                            + "Convidados: " + reservaCriada.getConvidados().size(),
                    ButtonType.OK).showAndWait();

            trocarTela(event, "/com/condominio/reserva/lista-reserva-view.fxml");

        } catch (IllegalArgumentException e) {
            alerta(e.getMessage());
        }
    }

    // ── navegação ─────────────────────────────────────────────────────────
    @FXML public void voltarHome(ActionEvent event)     { trocarTela(event, "/com/condominio/reserva/lista-reserva-view.fxml"); }
    @FXML public void voltarHome(MouseEvent event)      { trocarTela(event, "/com/condominio/reserva/lista-reserva-view.fxml"); }
    @FXML public void voltarReservas(ActionEvent event) { trocarTela(event, "/com/condominio/reserva/lista-reserva-view.fxml"); }
    @FXML public void voltarReservas(MouseEvent event)  { trocarTela(event, "/com/condominio/reserva/lista-reserva-view.fxml"); }

    // ── util ──────────────────────────────────────────────────────────────
    private void atualizarContadorConvidados() {
        lblContadorConvidados.setText(tabelaConvidados.getItems().size() + " convidados adicionados");
    }

    private AreaComum areaByNome(String nome) {
        for (AreaComum a : AREAS) if (a.getNome().equals(nome)) return a;
        throw new IllegalArgumentException("Área não encontrada: " + nome);
    }

    private Morador moradorByLabel(String label) {
        for (Morador m : moradorRepo.listar())
            if (m.getUnidade() != null && labelMorador(m).equals(label)) return m;
        return null;
    }

    private LocalDateTime toDateTime(LocalDate d, String hora) {
        return LocalDateTime.of(d, LocalTime.parse(hora, DateTimeFormatter.ofPattern("HH:mm")));
    }

    private void alerta(String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait();
    }

    private void trocarTela(javafx.event.Event event, String fxml) {
        try {
            FXMLLoader l = new FXMLLoader(getClass().getResource(fxml));
            Parent r = l.load();
            Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
            s.setScene(new Scene(r)); s.show();
        } catch (IOException e) { e.printStackTrace(); }
    }
}