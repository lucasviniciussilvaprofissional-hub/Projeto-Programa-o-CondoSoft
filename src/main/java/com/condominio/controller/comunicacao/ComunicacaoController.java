package com.condominio.controller.comunicacao;

import com.condominio.models.comunicacao.Aviso;
import com.condominio.service.ComunicacaoService;
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

public class ComunicacaoController {

    private final ComunicacaoService service = new ComunicacaoService();

    @FXML private TextField txtTitulo;
    @FXML private ComboBox<String> cmbCategoria;
    @FXML private TextArea txtMensagem;
    @FXML private TextField txtPesquisar;
    @FXML private ComboBox<String> cmbFiltrar;

    // USANDO SUA CLASSE AVISO DIRETO NA TABELA
    @FXML private TableView<Aviso> tabelaComunicados;
    @FXML private TableColumn<Aviso, String> colTitulo;
    @FXML private TableColumn<Aviso, String> colCategoria;
    @FXML private TableColumn<Aviso, String> colData;
    @FXML private TableColumn<Aviso, String> colAutor;

    @FXML
    public void initialize() {
        cmbCategoria.getItems().addAll("Aviso", "Edital");
        if (cmbFiltrar != null) {
            cmbFiltrar.getItems().addAll("Todos", "Aviso", "Edital");
        }

        // Mapeamento usando as propriedades da sua classe Aviso
        colTitulo.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitulo())
        );

        colCategoria.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty("Aviso") // Texto fixo ou baseado no seu modelo
        );

        colAutor.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty("Administração")
        );

        colData.setCellValueFactory(cellData -> {
            var dataHora = cellData.getValue().getDataCriacao();
            if (dataHora != null) {
                return new javafx.beans.property.SimpleStringProperty(dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });

        atualizarTabela();
    }

    @FXML
    private void enviarComunicado() {
        String titulo = txtTitulo.getText();
        String categoria = cmbCategoria.getValue();
        String mensagem = txtMensagem.getText();

        if (titulo == null || titulo.isBlank() || categoria == null || mensagem == null || mensagem.isBlank()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos obrigatórios", "Por favor, preencha todos os campos.");
            return;
        }

        if (categoria.equalsIgnoreCase("Aviso")) {
            service.publicarAviso(titulo, mensagem);
        } else {
            service.publicarEdital(titulo, mensagem);
        }

        atualizarTabela();
        limparFormulario();
        mostrarAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Publicação enviada com sucesso!");
    }

    private void atualizarTabela() {
        tabelaComunicados.getItems().clear();
        // Carrega os avisos direto para a tabela
        tabelaComunicados.getItems().addAll(service.obterAvisos());
    }

    @FXML
    private void limparFormulario() {
        txtTitulo.clear();
        txtMensagem.clear();
        cmbCategoria.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String texto) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(texto);
        alert.showAndWait();
    }

    @FXML
    private void voltarHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/condominio/home-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void limparForm(ActionEvent actionEvent) {
    }
}