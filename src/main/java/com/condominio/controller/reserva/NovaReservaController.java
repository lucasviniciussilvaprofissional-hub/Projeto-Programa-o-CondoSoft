package com.condominio.controller.reserva;

import com.condominio.enums.StatusReserva;
import com.condominio.models.area.AreaComum;
import com.condominio.models.area.Reserva;
import com.condominio.models.moradia.Morador;
import com.condominio.models.moradia.Proprietario;
import com.condominio.models.moradia.Unidade;
import com.condominio.enums.StatusMorador;
import com.condominio.service.ReservaService;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

public class NovaReservaController {

    @FXML private ComboBox<String> cbAreaComum;
    @FXML private ComboBox<String> cbUnidade;
    @FXML private ComboBox<String> cbMorador;
    @FXML private DatePicker dpData;
    @FXML private TextField txtHoraInicio;
    @FXML private TextField txtHoraFim;

    private final ReservaService reservaService = new ReservaService();

    @FXML
    public void initialize() {
        cbAreaComum.setItems(FXCollections.observableArrayList("Salão de Festas", "Churrasqueira", "Piscina"));
        cbUnidade.setItems(FXCollections.observableArrayList("Apto 101 - Bloco A", "Apto 102 - Bloco A"));
        cbMorador.setItems(FXCollections.observableArrayList("Morador 101", "Morador 102"));
        txtHoraInicio.setText("08:00");
        txtHoraFim.setText("12:00");
    }

    @FXML
    public void salvarReserva(ActionEvent event) {
        if (cbAreaComum.getValue() == null || cbUnidade.getValue() == null || dpData.getValue() == null) {
            new Alert(Alert.AlertType.ERROR, "Preencha os campos obrigatórios!").showAndWait();
            return;
        }

        try {
            AreaComum area = new AreaComum(cbAreaComum.getValue(), 50);
            area.setId(new Random().nextInt(100) + 1);

            Unidade unidade = new Unidade();
            unidade.setId(new Random().nextInt(100) + 1);
            unidade.setNumero(101);

            Morador responsavel = new Proprietario(1, "Morador do " + cbUnidade.getValue(), "00000000000", "999999999", unidade, "morador@email.com", StatusMorador.ATIVO);

            LocalDate data = dpData.getValue();
            LocalTime inicio = LocalTime.parse(txtHoraInicio.getText().trim());
            LocalTime fim = LocalTime.parse(txtHoraFim.getText().trim());

            Reserva novaReserva = new Reserva(
                    reservaService.listarReservas().size() + 1,
                    area, unidade, responsavel,
                    LocalDateTime.of(data, inicio),
                    LocalDateTime.of(data, fim),
                    1, StatusReserva.ATIVA
            );

            // ENVIA PARA A CLASSE QUE CONTROLA A LISTA
            reservaService.realizarReserva(novaReserva);

            new Alert(Alert.AlertType.INFORMATION, "Reserva realizada com sucesso!").showAndWait();
            voltarReservas(event);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Erro no formato da hora! Use HH:MM").showAndWait();
            e.printStackTrace();
        }
    }

    public void voltarReservas(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/condominio/reserva/lista-reserva-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void voltarHome(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/condominio/home-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}