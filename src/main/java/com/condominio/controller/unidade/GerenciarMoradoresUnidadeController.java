package com.condominio.controller.unidade;

import com.condominio.models.moradia.Dependente;
import com.condominio.models.moradia.Inquilino;
import com.condominio.models.moradia.Morador;
import com.condominio.models.moradia.Proprietario;
import com.condominio.models.moradia.Unidade;
import com.condominio.repository.implementation.MoradorRepositoryImpl;
import com.condominio.repository.implementation.UnidadeRepositoryImpl;

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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GerenciarMoradoresUnidadeController {

    // ───── estado global ───────────────────────────────────────────────────
    private static Unidade unidadeSelecionadaGlobal = null;

    public static void setUnidadeSelecionadaGlobal(Unidade unidade) {
        unidadeSelecionadaGlobal = unidade;
    }

    public static Unidade getUnidadeSelecionadaGlobal() {
        return unidadeSelecionadaGlobal;
    }

    // ───── FXML ────────────────────────────────────────────────────────────
    @FXML private Label lblBadgeUnidade;
    @FXML private Label lblTotalMoradores;
    @FXML private Label lblQtdProprietarios;
    @FXML private Label lblQtdInquilinos;
    @FXML private Label lblQtdDependentes;
    @FXML private Label lblSelecaoInfo;

    @FXML private TextField         txtBusca;
    @FXML private ComboBox<String>  cmbFiltroTipo;
    @FXML private ComboBox<String>  cmbFiltroStatus;

    @FXML private TableView<Morador>           tabelaMoradores;
    @FXML private TableColumn<Morador, String> colTipo;
    @FXML private TableColumn<Morador, String> colNome;
    @FXML private TableColumn<Morador, String> colCpf;
    @FXML private TableColumn<Morador, String> colTelefone;
    @FXML private TableColumn<Morador, String> colEmail;
    @FXML private TableColumn<Morador, String> colExtra;
    @FXML private TableColumn<Morador, String> colStatus;
    @FXML private TableColumn<Morador, String> colAcoes;

    // ───── repositórios ────────────────────────────────────────────────────
    private final MoradorRepositoryImpl moradorRepo = new MoradorRepositoryImpl();
    private final UnidadeRepositoryImpl unidadeRepo  = new UnidadeRepositoryImpl();

    private ObservableList<Morador> listaObservavel;

    // ───── inicialização ───────────────────────────────────────────────────
    @FXML
    public void initialize() {
        configurarColunas();
        atualizarBadgeUnidade();
        carregarTabela();
        configurarSelecao();
    }


    private void configurarColunas() {
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(tipoMorador(c.getValue())));
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
        colCpf.setCellValueFactory(c -> {
            String cpf = c.getValue().getCpf();
            // máscara básica: 000.000.000-00
            if (cpf != null && cpf.length() == 11) {
                cpf = cpf.substring(0,3) + "." + cpf.substring(3,6)
                    + "." + cpf.substring(6,9) + "-" + cpf.substring(9);
            }
            return new SimpleStringProperty(cpf != null ? cpf : "");
        });
        colTelefone.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTelefone()));
        colEmail.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));
        colExtra.setCellValueFactory(c -> new SimpleStringProperty(infoExtra(c.getValue())));
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getStatus() != null ? c.getValue().getStatus().name() : ""));
        colAcoes.setCellValueFactory(c -> new SimpleStringProperty("—"));
    }

    private String tipoMorador(Morador m) {
        if (m instanceof Proprietario) return "Proprietário";
        if (m instanceof Inquilino)   return "Inquilino";
        if (m instanceof Dependente)  return "Dependente";
        return "—";
    }

    private String infoExtra(Morador m) {
        if (m instanceof Dependente) {
            return "Dependente";
        }
        return "—";
    }

    private void atualizarBadgeUnidade() {
        if (unidadeSelecionadaGlobal != null) {
            lblBadgeUnidade.setText(
                    "Apto " + unidadeSelecionadaGlobal.getNumero()
                    + " – " + unidadeSelecionadaGlobal.getBloco());
        }
    }

    private void carregarTabela() {
        List<Morador> dados = obterMoradoresDaUnidade();
        listaObservavel = FXCollections.observableArrayList(dados);
        tabelaMoradores.setItems(listaObservavel);
        atualizarContadores(dados);
    }

    private List<Morador> obterMoradoresDaUnidade() {
        if (unidadeSelecionadaGlobal != null) {
            return new ArrayList<>(unidadeSelecionadaGlobal.getMoradores());
        }
        return moradorRepo.listar();
    }

    private void atualizarContadores(List<Morador> dados) {
        long props = dados.stream().filter(m -> m instanceof Proprietario).count();
        long inqs  = dados.stream().filter(m -> m instanceof Inquilino).count();
        long deps  = dados.stream().filter(m -> m instanceof Dependente).count();

        lblTotalMoradores.setText(dados.size() + " morador(es) nesta unidade");
        lblQtdProprietarios.setText(String.valueOf(props));
        lblQtdInquilinos.setText(String.valueOf(inqs));
        lblQtdDependentes.setText(String.valueOf(deps));
    }

    private void configurarSelecao() {
        tabelaMoradores.getSelectionModel().selectedItemProperty()
                .addListener((obs, old, novo) -> {
                    if (novo != null) {
                        lblSelecaoInfo.setText(
                                "Selecionado: " + novo.getNome()
                                + " (" + tipoMorador(novo) + ")");
                    } else {
                        lblSelecaoInfo.setText("Selecione um morador para gerenciá-lo");
                    }
                });
    }

    // ───── filtros ─────────────────────────────────────────────────────────
    @FXML
    public void filtrarMoradores(KeyEvent event) {
        aplicarFiltro();
    }

    @FXML
    public void filtrarMoradores(ActionEvent event) {
        aplicarFiltro();
    }

    private void aplicarFiltro() {
        String texto      = txtBusca.getText().toLowerCase();
        String tipoFiltro = cmbFiltroTipo.getValue();
        String statusFiltro = cmbFiltroStatus.getValue();

        List<Morador> base = obterMoradoresDaUnidade();
        ObservableList<Morador> filtrados = FXCollections.observableArrayList();

        for (Morador m : base) {
            boolean nomeOuCpf = m.getNome().toLowerCase().contains(texto)
                    || m.getCpf().contains(texto);

            boolean tipoOk = tipoFiltro == null || tipoFiltro.equals("Todos")
                    || (tipoFiltro.equals("Proprietário") && m instanceof Proprietario)
                    || (tipoFiltro.equals("Inquilino")   && m instanceof Inquilino)
                    || (tipoFiltro.equals("Dependente")  && m instanceof Dependente);

            boolean statusOk = statusFiltro == null || statusFiltro.equals("Todos")
                    || (m.getStatus() != null && m.getStatus().name().equals(statusFiltro));

            if (nomeOuCpf && tipoOk && statusOk) filtrados.add(m);
        }

        tabelaMoradores.setItems(filtrados);
    }

    @FXML
    public void limparFiltros(ActionEvent event) {
        txtBusca.clear();
        cmbFiltroTipo.getSelectionModel().selectFirst();
        cmbFiltroStatus.getSelectionModel().selectFirst();
        carregarTabela();
    }

    // ───── ações ───────────────────────────────────────────────────────────
    @FXML
    public void verDetalhesMorador(ActionEvent event) {
        Morador selecionado = tabelaMoradores.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAviso("Selecione um morador para ver os detalhes.");
            return;
        }
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Detalhes do Morador");
        info.setHeaderText(selecionado.getNome());
        info.setContentText(
                "Tipo: " + tipoMorador(selecionado) + "\n"
                + "CPF: " + selecionado.getCpf() + "\n"
                + "Telefone: " + selecionado.getTelefone() + "\n"
                + "E-mail: " + selecionado.getEmail() + "\n"
                + "Status: " + (selecionado.getStatus() != null ? selecionado.getStatus().name() : "—"));
        info.showAndWait();
    }

    @FXML
    public void editarMorador(ActionEvent event) {
        Morador selecionado = tabelaMoradores.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAviso("Selecione um morador para editar.");
            return;
        }
        // passa unidade e abre cadastro em modo edição
        CadastrarMoradorController.setUnidadeSelecionadaGlobal(unidadeSelecionadaGlobal);
        trocarTela(event, "/com/condominio/unidade/cadastrar-morador-view.fxml");
    }

    @FXML
    public void confirmarRemocaoMorador(ActionEvent event) {
        Morador selecionado = tabelaMoradores.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAviso("Selecione um morador para remover.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Remover Morador");
        confirmacao.setHeaderText(null);
        confirmacao.setContentText(
                "Deseja remover " + selecionado.getNome() + " desta unidade?");

        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                moradorRepo.remover(selecionado.getId());

                if (unidadeSelecionadaGlobal != null) {
                    try {
                        unidadeSelecionadaGlobal.removerMorador(selecionado);
                        unidadeRepo.atualizar(unidadeSelecionadaGlobal);
                    } catch (Exception ignored) { /* já removido */ }
                }

                carregarTabela();
            }
        });
    }

    // ───── navegação ───────────────────────────────────────────────────────
    @FXML
    public void abrirCadastrarMorador(ActionEvent event) {
        CadastrarMoradorController.setUnidadeSelecionadaGlobal(unidadeSelecionadaGlobal);
        trocarTela(event, "/com/condominio/unidade/cadastrar-morador-view.fxml");
    }

    @FXML
    public void voltarHome(MouseEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    @FXML
    public void voltarUnidades(MouseEvent event) {
        trocarTela(event, "/com/condominio/unidade/unidade-view.fxml");
    }

    @FXML
    public void voltarDetalhesUnidade(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/condominio/unidade/detalhes-unidade-view.fxml"
                    )
            );

            Parent root = loader.load();

            DetalhesUnidadeController controller =
                    loader.getController();

            controller.setUnidade(unidadeSelecionadaGlobal);

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

    @FXML
    public void voltarDetalhesUnidadeBreadCrumb(MouseEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/com/condominio/unidade/detalhes-unidade-view.fxml"
                    )
            );

            Parent root = loader.load();

            DetalhesUnidadeController controller =
                    loader.getController();

            controller.setUnidade(unidadeSelecionadaGlobal);

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

    // ───── utilidades ──────────────────────────────────────────────────────
    private void mostrarAviso(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void trocarTela(javafx.event.Event event, String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro ao abrir: " + fxml);
        }
    }
}
