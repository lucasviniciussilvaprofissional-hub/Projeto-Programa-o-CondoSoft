package com.condominio.controller.unidade;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class GerenciarMoradoresUnidadeController {

    @FXML
    private Label lblBadgeUnidade;

    @FXML
    private Label lblTotalMoradores;

    @FXML
    private Label lblQtdProprietarios;

    @FXML
    private Label lblQtdInquilinos;

    @FXML
    private Label lblQtdDependentes;

    @FXML
    private Label lblSelecaoInfo;

    @FXML
    private TextField txtBusca;

    @FXML
    private ComboBox<String> cmbFiltroTipo;

    @FXML
    private ComboBox<String> cmbFiltroStatus;

    @FXML
    private TableView<Object> tabelaMoradores;

    @FXML
    private TableColumn<Object, String> colTipo;

    @FXML
    private TableColumn<Object, String> colNome;

    @FXML
    private TableColumn<Object, String> colCpf;

    @FXML
    private TableColumn<Object, String> colTelefone;

    @FXML
    private TableColumn<Object, String> colEmail;

    @FXML
    private TableColumn<Object, String> colExtra;

    @FXML
    private TableColumn<Object, String> colStatus;

    @FXML
    private TableColumn<Object, String> colAcoes;

    @FXML
    public void initialize() {

        cmbFiltroTipo.setItems(
                FXCollections.observableArrayList(
                        "Todos",
                        "Proprietário",
                        "Inquilino",
                        "Dependente"
                )
        );

        cmbFiltroStatus.setItems(
                FXCollections.observableArrayList(
                        "Todos",
                        "Ativo",
                        "Inativo"
                )
        );

        atualizarContadores();
    }

    private void atualizarContadores() {

        lblTotalMoradores.setText(
                "0 moradores nesta unidade"
        );

        lblQtdProprietarios.setText("0");
        lblQtdInquilinos.setText("0");
        lblQtdDependentes.setText("0");
    }

    // ==========================
    // NAVEGAÇÃO
    // ==========================

    public void voltarHome(MouseEvent event) {

        trocarTela(
                event,
                "/com/condominio/home-view.fxml"
        );
    }

    public void voltarUnidades(MouseEvent event) {

        trocarTela(
                event,
                "/com/condominio/unidade/unidade-view.fxml"
        );
    }

    public void voltarDetalhesUnidade(ActionEvent event) {

        trocarTela(
                event,
                "/com/condominio/unidade/detalhes-unidade-view.fxml"
        );
    }

    public void voltarDetalhesUnidade(MouseEvent event) {

        trocarTela(
                event,
                "/com/condominio/unidade/detalhes-unidade-view.fxml"
        );
    }

    public void abrirCadastrarMorador(ActionEvent event) {

        trocarTela(
                event,
                "/com/condominio/cadastrar-morador-view.fxml"
        );
    }

    // ==========================
    // FILTROS
    // ==========================

    public void filtrarMoradores(KeyEvent event) {

        System.out.println(
                "Buscar: " + txtBusca.getText()
        );
    }

    public void filtrarMoradores(ActionEvent event) {

        System.out.println(
                "Filtro alterado"
        );
    }

    public void limparFiltros(ActionEvent event) {

        txtBusca.clear();

        cmbFiltroTipo.getSelectionModel()
                .clearSelection();

        cmbFiltroStatus.getSelectionModel()
                .clearSelection();
    }

    // ==========================
    // AÇÕES
    // ==========================

    public void verDetalhesMorador(ActionEvent event) {

        System.out.println(
                "Ver detalhes do morador"
        );
    }

    public void editarMorador(ActionEvent event) {

        System.out.println(
                "Editar morador"
        );
    }

    public void confirmarRemocaoMorador(
            ActionEvent event
    ) {

        System.out.println(
                "Remover morador"
        );
    }

    // ==========================
    // UTIL
    // ==========================

    private void trocarTela(
            javafx.event.Event event,
            String caminhoFXML
    ) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    caminhoFXML
                            )
                    );

            Parent root = loader.load();

            Stage stage =
                    (Stage) ((Node)
                            event.getSource())
                            .getScene()
                            .getWindow();

            stage.setScene(
                    new Scene(root)
            );

            stage.show();

        } catch (IOException e) {

            e.printStackTrace();

            System.out.println(
                    "Erro ao abrir: "
                            + caminhoFXML
            );
        }
    }
}