package com.condominio.controller.comunicacao;

import com.condominio.models.comunicacao.Aviso;
import com.condominio.models.comunicacao.Edital;
import com.condominio.service.ComunicacaoService;
import javafx.beans.property.SimpleStringProperty;
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

    // Tabela configurada para listar as publicações unificadas como Aviso
    @FXML private TableView<Aviso> tabelaComunicados;
    @FXML private TableColumn<Aviso, String> colTitulo;
    @FXML private TableColumn<Aviso, String> colCategoria;
    @FXML private TableColumn<Aviso, String> colData;
    @FXML private TableColumn<Aviso, String> colAutor;

    @FXML
    public void initialize() {
        // Popula os ComboBoxes
        cmbCategoria.getItems().addAll("Aviso", "Edital");
        if (cmbFiltrar != null) {
            cmbFiltrar.getItems().addAll("Todos", "Aviso", "Edital");
            cmbFiltrar.setValue("Todos");
        }

        // Mapeamento básico das colunas da tabela
        colTitulo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTitulo())
        );

        // Identifica o tipo com base em uma marcação que colocaremos no objeto adaptado
        colCategoria.setCellValueFactory(cellData -> {
            Aviso item = cellData.getValue();
            String categoria = "Aviso";
            if (item.getMensagem() != null && item.getMensagem().startsWith("[EDITAL] ")) {
                categoria = "Edital";
            }
            return new SimpleStringProperty(categoria);
        });

        colAutor.setCellValueFactory(cellData ->
                new SimpleStringProperty("Administração")
        );

        colData.setCellValueFactory(cellData -> {
            var dataHora = cellData.getValue().getDataCriacao();
            if (dataHora != null) {
                return new SimpleStringProperty(dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            }
            return new SimpleStringProperty("");
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

    // CORREÇÃO: Limpa a tabela e faz o mapeamento seguro de Edital para Aviso
    private void atualizarTabela() {
        tabelaComunicados.getItems().clear();

        // 1. Adiciona os Avisos normais
        if (service.obterAvisos() != null) {
            tabelaComunicados.getItems().addAll(service.obterAvisos());
        }

        // 2. Transforma dinamicamente a sua classe Edital em formato compatível com a tabela
        try {
            if (service.obterEditais() != null) {
                for (Edital ed : service.obterEditais()) {
                    Aviso adaptado = new Aviso();
                    adaptado.setTitulo(ed.getTitulo());
                    // Usamos um prefixo oculto para a coluna da categoria saber que este registro é um Edital
                    adaptado.setMensagem("[EDITAL] " + ed.getDescricao());
                    adaptado.setDataCriacao(ed.getDataPublicacao());

                    tabelaComunicados.getItems().add(adaptado);
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar editais: " + e.getMessage());
        }
    }

    // CORREÇÃO: Filtro inteligente adaptado para as duas fontes de dados distintas
    @FXML
    private void filtrarPublicacoes(ActionEvent event) {
        String filtro = cmbFiltrar.getValue();
        if (filtro == null || filtro.equals("Todos")) {
            atualizarTabela();
            return;
        }

        tabelaComunicados.getItems().clear();

        if (filtro.equalsIgnoreCase("Aviso")) {
            if (service.obterAvisos() != null) {
                tabelaComunicados.getItems().addAll(service.obterAvisos());
            }
        } else if (filtro.equalsIgnoreCase("Edital")) {
            try {
                if (service.obterEditais() != null) {
                    for (Edital ed : service.obterEditais()) {
                        Aviso adaptado = new Aviso();
                        adaptado.setTitulo(ed.getTitulo());
                        adaptado.setMensagem("[EDITAL] " + ed.getDescricao());
                        adaptado.setDataCriacao(ed.getDataPublicacao());
                        tabelaComunicados.getItems().add(adaptado);
                    }
                }
            } catch (Exception e) {
                System.out.println("Erro ao filtrar editais: " + e.getMessage());
            }
        }
    }

    @FXML
    private void limparFormulario() {
        txtTitulo.clear();
        txtMensagem.clear();
        cmbCategoria.getSelectionModel().clearSelection();
    }

    public void limparForm(ActionEvent actionEvent) {
        limparFormulario();
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
}