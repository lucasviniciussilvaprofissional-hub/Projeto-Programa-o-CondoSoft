package com.condominio.controller.unidade;

import com.condominio.models.moradia.Morador;
import com.condominio.repository.implementation.MoradorRepositoryImpl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ListarMoradoresController {

    @FXML
    private TableView<Morador> tabelaMoradores;

    @FXML
    private TextField txtBusca;

    private final MoradorRepositoryImpl repository =
            new MoradorRepositoryImpl();

    private ObservableList<Morador> moradores;

    @FXML
    public void initialize() {

        moradores = FXCollections.observableArrayList(
                repository.listar()
        );

        tabelaMoradores.setItems(moradores);

        configurarTabela();
    }

    private void configurarTabela() {

        // Exemplo:
        //
        // colNome.setCellValueFactory(
        //      new PropertyValueFactory<>("nome")
        // );
        //
        // colCpf.setCellValueFactory(
        //      new PropertyValueFactory<>("cpf")
        // );
    }

    /* ==========================
       FILTROS
    ========================== */

    public void filtrarMoradores(ActionEvent event) {
        aplicarFiltro();
    }

    public void filtrarMoradores(KeyEvent event) {
        aplicarFiltro();
    }

    private void aplicarFiltro() {

        String texto = txtBusca.getText().toLowerCase();

        ObservableList<Morador> filtrados =
                FXCollections.observableArrayList();

        for (Morador m : repository.listar()) {

            if (m.getNome().toLowerCase().contains(texto)) {
                filtrados.add(m);
            }
        }

        tabelaMoradores.setItems(filtrados);
    }

    public void limparFiltros(ActionEvent event) {

        txtBusca.clear();

        tabelaMoradores.setItems(
                FXCollections.observableArrayList(
                        repository.listar()
                )
        );
    }

    /* ==========================
       CRUD
    ========================== */

    public void abrirCadastrarMorador(ActionEvent event) {
        trocarTela(event,
                "/com/condominio/unidade/cadastrar-morador-view.fxml");
    }

    public void abrirDetalhesMorador(ActionEvent event) {

        Morador selecionado =
                tabelaMoradores.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            return;
        }

        System.out.println(
                "Morador selecionado: "
                        + selecionado.getNome()
        );
    }

    public void editarMorador(ActionEvent event) {

        Morador selecionado =
                tabelaMoradores.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            return;
        }

        System.out.println(
                "Editar morador: "
                        + selecionado.getNome()
        );
    }

    public void removerMorador(ActionEvent event) {

        Morador selecionado =
                tabelaMoradores.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            return;
        }

        repository.remover(
                selecionado.getId()
        );

        tabelaMoradores.setItems(
                FXCollections.observableArrayList(
                        repository.listar()
                )
        );
    }

    /* ==========================
       NAVEGAÇÃO
    ========================== */

    public void voltarHome(MouseEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    public void voltarUnidades(MouseEvent event) {
        trocarTela(event, "/com/condominio/unidade/unidade-view.fxml");
    }

    public void voltarDetalhesUnidade(ActionEvent event) {
        trocarTela(event,
                "/com/condominio/unidade/detalhes-unidade-view.fxml");
    }

    public void voltarDetalhesUnidade(MouseEvent event) {
        trocarTela(event,
                "/com/condominio/unidade/detalhes-unidade-view.fxml");
    }

    /* ==========================
       TROCA DE TELA
    ========================== */

    private void trocarTela(ActionEvent event,
                            String caminhoFXML) {

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
            e.printStackTrace();
        }
    }

    private void trocarTela(MouseEvent event,
                            String caminhoFXML) {

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
            e.printStackTrace();
        }
    }
}