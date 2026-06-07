package com.condominio.controller.reserva;

import com.condominio.models.area.AreaComum;
import com.condominio.models.moradia.Morador;
import com.condominio.repository.implementation.BoletoRepositoryImpl;
import com.condominio.repository.implementation.MoradorRepositoryImpl;
import com.condominio.repository.implementation.ReservaRepositoryImpl;
import com.condominio.repository.implementation.TaxaLimpezaRepositoryImpl;
import com.condominio.service.ReservaService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Tela simplificada de reserva (reserva-view.fxml). */
public class ReservaController {

    @FXML private ComboBox<String> cbAreaComum;
    @FXML private ComboBox<String> cbMorador;
    @FXML private DatePicker       dpData;
    @FXML private TextField        txtHoraInicio;
    @FXML private TextField        txtHoraFim;

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

    private final MoradorRepositoryImpl moradorRepo = new MoradorRepositoryImpl();

    @FXML
    public void initialize() {
        cbAreaComum.getItems().clear();
        for (AreaComum a : AREAS) cbAreaComum.getItems().add(a.getNome());

        // carrega moradores reais do repositório
        if (cbMorador != null) {
            cbMorador.getItems().clear();
            for (Morador m : moradorRepo.listar()) {
                if (m.getUnidade() != null)
                    cbMorador.getItems().add(m.getNome() + " – Apto "
                            + m.getUnidade().getNumero());
            }
            if (cbMorador.getItems().isEmpty())
                cbMorador.getItems().add("(Nenhum morador cadastrado)");
        }
    }

    @FXML
    private void salvarReserva(ActionEvent event) {
        if (cbAreaComum.getValue() == null || dpData.getValue() == null
                || txtHoraInicio.getText().isBlank() || txtHoraFim.getText().isBlank()) {
            alerta("Preencha todos os campos."); return;
        }

        List<Morador> moradores = moradorRepo.listar().stream()
                .filter(m -> m.getUnidade() != null).toList();
        if (moradores.isEmpty()) { alerta("Nenhum morador com unidade cadastrado."); return; }

        Morador responsavel = moradores.get(0); // usa primeiro disponível nesta tela simples
        if (cbMorador != null && cbMorador.getValue() != null) {
            for (Morador m : moradores)
                if ((m.getNome() + " – Apto " + m.getUnidade().getNumero())
                        .equals(cbMorador.getValue())) { responsavel = m; break; }
        }

        try {
            AreaComum area = AREAS.stream()
                    .filter(a -> a.getNome().equals(cbAreaComum.getValue()))
                    .findFirst().orElseThrow();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
            LocalDateTime ini = LocalDateTime.of(dpData.getValue(),
                    LocalTime.parse(txtHoraInicio.getText().trim(), fmt));
            LocalDateTime fim = LocalDateTime.of(dpData.getValue(),
                    LocalTime.parse(txtHoraFim.getText().trim(), fmt));

            service.criarReserva(responsavel, area, ini, fim, 1);

            new Alert(Alert.AlertType.INFORMATION,
                    "Reserva criada com sucesso!", ButtonType.OK).showAndWait();
            nav(event, "/com/condominio/reserva/lista-reserva-view.fxml");

        } catch (Exception e) { alerta(e.getMessage()); }
    }

    @FXML private void voltarHome(ActionEvent event) { nav(event, "/com/condominio/home-view.fxml"); }

    private void alerta(String msg) { new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait(); }
    private void nav(ActionEvent event, String fxml) {
        try {
            FXMLLoader l = new FXMLLoader(getClass().getResource(fxml));
            Parent r = l.load();
            Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
            s.setScene(new Scene(r)); s.show();
        } catch (IOException e) { e.printStackTrace(); }
    }
}
