package com.condominio.controller.unidade;

import com.condominio.models.moradia.Unidade;
import com.condominio.models.moradia.Veiculo;
import com.condominio.repository.implementation.UnidadeRepositoryImpl;
import com.condominio.repository.implementation.VeiculoRepositoryImpl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;


public class CadastrarVeiculoController {

    // ───── estado global ───────────────────────────────────────────────────
    /** null = novo cadastro; não-null = edição */
    private static Veiculo veiculoEmEdicao = null;

    public static void setVeiculoEmEdicao(Veiculo v) {
        veiculoEmEdicao = v;
    }

    // ───── FXML ────────────────────────────────────────────────────────────
    @FXML private Label    lblUnidadeContexto;
    @FXML private TextField txtModelo;
    @FXML private TextField txtPlaca;
    @FXML private TextField txtCor;
    @FXML private TextField txtUnidade;

    @FXML private Label previewModelo;
    @FXML private Label previewPlaca;
    @FXML private Label previewCor;

    // ───── repositórios ────────────────────────────────────────────────────
    private final VeiculoRepositoryImpl veiculoRepo = new VeiculoRepositoryImpl();
    private final UnidadeRepositoryImpl unidadeRepo  = new UnidadeRepositoryImpl();

    // ───── inicialização ───────────────────────────────────────────────────
    @FXML
    public void initialize() {
        Unidade unidade = VeiculoController.getUnidadeSelecionadaGlobal();

        if (unidade != null) {
            txtUnidade.setText(String.valueOf(unidade.getNumero()));
            lblUnidadeContexto.setText(
                    "Apto " + unidade.getNumero() + " – " + unidade.getBloco());
        }

        // modo edição: preenche os campos
        if (veiculoEmEdicao != null) {
            txtModelo.setText(veiculoEmEdicao.getModelo());
            txtPlaca.setText(veiculoEmEdicao.getPlaca());
            txtCor.setText(veiculoEmEdicao.getCor());
            atualizarPreviewInterno();
        }

        configurarPreviewListeners();
    }

    private void configurarPreviewListeners() {
        txtModelo.setOnKeyReleased(e -> atualizarPreviewInterno());
        txtPlaca.setOnKeyReleased(e  -> atualizarPreviewInterno());
        txtCor.setOnKeyReleased(e    -> atualizarPreviewInterno());
    }

    private void atualizarPreviewInterno() {
        previewModelo.setText(
                txtModelo.getText().isEmpty() ? "Modelo não preenchido" : txtModelo.getText());
        previewPlaca.setText(
                txtPlaca.getText().isEmpty() ? "—" : txtPlaca.getText().toUpperCase());
        previewCor.setText(
                txtCor.getText().isEmpty() ? "—" : txtCor.getText());
    }

    // ───── salvar ──────────────────────────────────────────────────────────
    @FXML
    public void salvarVeiculo(ActionEvent event) {

        if (txtModelo.getText().isEmpty()
                || txtPlaca.getText().isEmpty()
                || txtCor.getText().isEmpty()) {
            mostrarAlerta("Erro", "Campos obrigatórios",
                    "Preencha Modelo, Placa e Cor.",
                    Alert.AlertType.ERROR);
            return;
        }

        Unidade unidade = VeiculoController.getUnidadeSelecionadaGlobal();
        if (unidade == null) {
            mostrarAlerta("Erro", "Unidade não encontrada",
                    "Nenhuma unidade selecionada.",
                    Alert.AlertType.ERROR);
            return;
        }

        try {
            if (veiculoEmEdicao != null) {
                // ── edição ──
                veiculoEmEdicao.setModelo(txtModelo.getText());
                veiculoEmEdicao.setPlaca(txtPlaca.getText());
                veiculoEmEdicao.setCor(txtCor.getText());
                veiculoRepo.atualizar(veiculoEmEdicao);

                mostrarAlerta("Sucesso", null, "Veículo atualizado!",
                        Alert.AlertType.INFORMATION);

            } else {
                // ── novo ──
                int novoId = veiculoRepo.listar().size() + 1;
                Veiculo veiculo = new Veiculo(
                        novoId,
                        txtModelo.getText(),
                        txtPlaca.getText(),
                        txtCor.getText(),
                        unidade);

                // Regra de negócio: máx 2 veículos por unidade
                unidade.adicionarVeiculo(veiculo);
                veiculoRepo.salvar(veiculo);
                unidadeRepo.atualizar(unidade);

                mostrarAlerta("Sucesso", null,
                        "Veículo cadastrado na Unidade " + unidade.getNumero() + "!",
                        Alert.AlertType.INFORMATION);
            }

            limparFormulario(null);
            veiculoEmEdicao = null;

        } catch (IllegalArgumentException e) {
            mostrarAlerta("Erro", "Falha ao salvar", e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void limparFormulario(ActionEvent event) {
        txtModelo.clear();
        txtPlaca.clear();
        txtCor.clear();
        atualizarPreviewInterno();
    }

    // ───── navegação ───────────────────────────────────────────────────────
    @FXML
    public void voltarVeiculos(ActionEvent event) {
        trocarTela(event, "/com/condominio/unidade/veiculo-view.fxml");
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

            controller.setUnidade(
                    GerenciarMoradoresUnidadeController.getUnidadeSelecionadaGlobal()
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

            controller.setUnidade(
                    GerenciarMoradoresUnidadeController.getUnidadeSelecionadaGlobal()
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



    // ───── utilidades ──────────────────────────────────────────────────────
    private void mostrarAlerta(String titulo, String header,
                               String conteudo, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(conteudo);
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
