package com.condominio.controller.reserva;

import javafx.collections.FXCollections;
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

public class NovaReservaController {

    /* ==========================
       COMPONENTES FXML
    ========================== */

    @FXML
    private ComboBox<String> cmbAreaComum;

    @FXML
    private ComboBox<String> cmbUnidade;

    @FXML
    private ComboBox<String> cmbHorarioInicio;

    @FXML
    private ComboBox<String> cmbHorarioFim;

    @FXML
    private DatePicker dpDataReserva;

    @FXML
    private TextField txtNumConvidados;

    @FXML
    private TextArea txtObservacoes;

    @FXML
    private TextField txtNomeConvidado;

    @FXML
    private TextField txtDocConvidado;

    @FXML
    private TableView<String> tabelaConvidados;

    @FXML
    private TableColumn<String, String> colNomeConvidado;

    @FXML
    private TableColumn<String, String> colDocConvidado;

    @FXML
    private TableColumn<String, String> colRemoverConvidado;

    @FXML
    private Label lblContadorConvidados;

    @FXML
    private Label lblDisponibilidade;

    @FXML
    private HBox hboxDisponibilidade;

    /* ==========================
       INICIALIZAÇÃO
    ========================== */

    @FXML
    public void initialize() {

        // Áreas comuns
        cmbAreaComum.setItems(FXCollections.observableArrayList(
                "Salão de Festas",
                "Churrasqueira",
                "Piscina",
                "Quadra",
                "Espaço Gourmet"
        ));

        // Unidades
        cmbUnidade.setItems(FXCollections.observableArrayList(
                "Apto 101 - Bloco A",
                "Apto 102 - Bloco A",
                "Apto 201 - Bloco B"
        ));

        // Horários
        cmbHorarioInicio.setItems(FXCollections.observableArrayList(
                "08:00",
                "09:00",
                "10:00",
                "11:00",
                "12:00",
                "13:00",
                "14:00",
                "15:00",
                "16:00",
                "17:00",
                "18:00",
                "19:00",
                "20:00"
        ));

        cmbHorarioFim.setItems(FXCollections.observableArrayList(
                "09:00",
                "10:00",
                "11:00",
                "12:00",
                "13:00",
                "14:00",
                "15:00",
                "16:00",
                "17:00",
                "18:00",
                "19:00",
                "20:00",
                "21:00",
                "22:00"
        ));

        // Esconde painel de disponibilidade
        hboxDisponibilidade.setVisible(false);
        hboxDisponibilidade.setManaged(false);

        atualizarContadorConvidados();
    }

    /* ==========================
       AÇÕES
    ========================== */

    @FXML
    public void verificarDisponibilidade(ActionEvent event) {

        if (cmbAreaComum.getValue() != null
                && dpDataReserva.getValue() != null
                && cmbHorarioInicio.getValue() != null) {

            hboxDisponibilidade.setVisible(true);
            hboxDisponibilidade.setManaged(true);

            lblDisponibilidade.setText(
                    "Horário disponível para reserva"
            );
        }
    }

    @FXML
    public void adicionarConvidado(ActionEvent event) {

        String nome = txtNomeConvidado.getText();

        if (nome == null || nome.isEmpty()) {
            return;
        }

        tabelaConvidados.getItems().add(nome);

        txtNomeConvidado.clear();
        txtDocConvidado.clear();

        atualizarContadorConvidados();
    }

    @FXML
    public void confirmarReserva(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Reserva");
        alert.setHeaderText("Reserva criada");
        alert.setContentText(
                "A reserva foi registrada com sucesso!"
        );

        alert.showAndWait();

        voltarReservas(event);
    }

    /* ==========================
       NAVEGAÇÃO
    ========================== */

    @FXML
    public void voltarHome(ActionEvent event) {
        trocarTela(event,
                "/com/condominio/home-view.fxml");
    }

    @FXML
    public void voltarHome(MouseEvent event) {
        trocarTela(event,
                "/com/condominio/home-view.fxml");
    }

    @FXML
    public void voltarReservas(ActionEvent event) {
        trocarTela(event,
                "/com/condominio/reserva-view.fxml");
    }

    @FXML
    public void voltarReservas(MouseEvent event) {
        trocarTela(event,
                "/com/condominio/reserva-view.fxml");
    }

    /* ==========================
       MÉTODOS AUXILIARES
    ========================== */

    private void atualizarContadorConvidados() {

        int quantidade =
                tabelaConvidados.getItems().size();

        lblContadorConvidados.setText(
                quantidade + " convidados adicionados"
        );
    }

    private void trocarTela(
            ActionEvent event,
            String caminhoFXML
    ) {

        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource(caminhoFXML)
            );

            Stage stage = (Stage)
                    ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {

            System.out.println(
                    "Erro ao abrir: "
                            + caminhoFXML
            );

            e.printStackTrace();
        }
    }

    private void trocarTela(
            MouseEvent event,
            String caminhoFXML
    ) {

        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource(caminhoFXML)
            );

            Stage stage = (Stage)
                    ((Node) event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {

            System.out.println(
                    "Erro ao abrir: "
                            + caminhoFXML
            );

            e.printStackTrace();
        }
    }
}