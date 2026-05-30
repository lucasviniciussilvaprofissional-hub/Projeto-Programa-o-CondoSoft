package com.condominio.controller.comunicacao;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class ComunicacaoController {

    @FXML
    private Button btnVoltarHome;

    @FXML
    private Button btnNovoComunicado;

    @FXML
    private Button btnEnviar;

    @FXML
    private Label lblComunicados;

    @FXML
    private Label lblLidos;

    @FXML
    private Label lblAvisosAtivos;

    @FXML
    private ComboBox<String> cmbCategoria;

    @FXML
    private ComboBox<String> cmbDestinatario;

    @FXML
    private TextField txtTitulo;

    @FXML
    private TextArea txtMensagem;

    @FXML
    private TableView<?> tabelaComunicados;

    @FXML
    private TableColumn<?, ?> colTitulo;

    @FXML
    private TableColumn<?, ?> colCategoria;

    @FXML
    private TableColumn<?, ?> colDestinatario;

    @FXML
    private TableColumn<?, ?> colData;

    @FXML
    private TableColumn<?, ?> colStatus;

    @FXML
    public void initialize() {

        cmbCategoria.getItems().addAll(
                "Aviso",
                "Assembleia",
                "Comunicado",
                "Manutenção",
                "Urgente"
        );

        cmbDestinatario.getItems().addAll(
                "Todos os Moradores",
                "Bloco A",
                "Bloco B",
                "Bloco C",
                "Síndico"
        );

    }

    @FXML
    private void voltarHome() {

    }

    @FXML
    private void novoComunicado() {

        txtTitulo.clear();
        txtMensagem.clear();

        cmbCategoria.getSelectionModel().clearSelection();
        cmbDestinatario.getSelectionModel().clearSelection();

    }

    @FXML
    private void enviarComunicado() {

    }

    @FXML
    private void filtrarComunicados() {

    }

    @FXML
    private void visualizarComunicado() {

    }

    @FXML
    private void excluirComunicado() {

    }

}