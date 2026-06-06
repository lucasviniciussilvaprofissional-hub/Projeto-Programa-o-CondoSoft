package com.condominio.controller.reserva;

import com.condominio.models.area.Reserva;
import com.condominio.service.ReservaService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class ListaReservaController {

    @FXML
    private TableView<Reserva> tvReservas;

    private final ReservaService reservaService = new ReservaService();

    @FXML
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void initialize() {
        System.out.println("[CondoSoft] Inicializando tabela com Callbacks explícitos para evitar erros de tipo.");

        if (tvReservas != null && !tvReservas.getColumns().isEmpty()) {
            try {
                // Forçamos a conversão das colunas genéricas do FXML para o tipo correto (Reserva, String)
                // Isso elimina o erro "SimpleStringProperty cannot be converted to ObservableValue<capture of ?>"

                TableColumn<Reserva, String> colInicio = (TableColumn<Reserva, String>) tvReservas.getColumns().get(0);
                colInicio.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reserva, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Reserva, String> cellData) {
                        return new SimpleStringProperty(cellData.getValue().getStringInicio());
                    }
                });

                TableColumn<Reserva, String> colArea = (TableColumn<Reserva, String>) tvReservas.getColumns().get(1);
                colArea.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reserva, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Reserva, String> cellData) {
                        return new SimpleStringProperty(cellData.getValue().getNomeArea());
                    }
                });

                TableColumn<Reserva, String> colMorador = (TableColumn<Reserva, String>) tvReservas.getColumns().get(2);
                colMorador.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reserva, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Reserva, String> cellData) {
                        return new SimpleStringProperty(cellData.getValue().getNomeResponsavel());
                    }
                });

                TableColumn<Reserva, String> colFim = (TableColumn<Reserva, String>) tvReservas.getColumns().get(3);
                colFim.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reserva, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Reserva, String> cellData) {
                        return new SimpleStringProperty(cellData.getValue().getStringFim());
                    }
                });

                TableColumn<Reserva, String> colStatus = (TableColumn<Reserva, String>) tvReservas.getColumns().get(4);
                colStatus.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reserva, String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<Reserva, String> cellData) {
                        return new SimpleStringProperty(cellData.getValue().getStringStatus());
                    }
                });

                // Alimenta a tabela puxando as reservas da memória
                tvReservas.setItems(reservaService.listarReservas());
                tvReservas.refresh();

                System.out.println("[CondoSoft] Tabela montada com sucesso! Linhas: " + tvReservas.getItems().size());
            } catch (Exception e) {
                System.out.println("[CondoSoft] Erro ao converter colunas: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("[CondoSoft] ERRO: Componente tvReservas nulo ou sem colunas.");
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