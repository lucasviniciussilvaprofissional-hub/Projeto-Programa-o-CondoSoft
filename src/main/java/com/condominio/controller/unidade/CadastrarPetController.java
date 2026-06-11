package com.condominio.controller.unidade;

import com.condominio.enums.TipoPet;
import com.condominio.models.moradia.Pet;
import com.condominio.models.moradia.Unidade;
import com.condominio.repository.implementation.PetRepositoryImpl;
import com.condominio.repository.implementation.UnidadeRepositoryImpl;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class CadastrarPetController {

    // ───── estado global ───────────────────────────────────────────────────
    private static Pet petEmEdicao = null;

    public static void setPetEmEdicao(Pet p) {
        petEmEdicao = p;
    }

    // ───── FXML ────────────────────────────────────────────────────────────
    @FXML private Label        lblUnidadeContexto;
    @FXML private TextField    txtNome;
    @FXML private ComboBox<String> cmbTipoPet;
    @FXML private TextField    txtRaca;
    @FXML private TextField    txtCor;
    @FXML private TextField    txtUnidade;
    @FXML private TextField    txtTipoPetHidden;

    @FXML private Label previewEmoji;
    @FXML private Label previewNome;
    @FXML private Label previewTipo;
    @FXML private Label previewRaca;
    @FXML private Label previewCor;

    @FXML private VBox cardCao;
    @FXML private VBox cardGato;
    @FXML private VBox cardPassaro;
    @FXML private VBox cardRoedor;
    @FXML private VBox cardReptil;
    @FXML private VBox cardOutro;

    // ───── repositórios ────────────────────────────────────────────────────
    private final PetRepositoryImpl    petRepo     = new PetRepositoryImpl();
    private final UnidadeRepositoryImpl unidadeRepo  = new UnidadeRepositoryImpl();

    private TipoPet tipoSelecionado = TipoPet.CACHORRO;

    // ───── inicialização ───────────────────────────────────────────────────
    @FXML
    public void initialize() {
        // Preenche combobox com enum TipoPet
        for (TipoPet t : TipoPet.values()) {
            cmbTipoPet.getItems().add(t.name());
        }

        Unidade unidade = PetController.getUnidadeSelecionadaGlobal();
        if (unidade != null) {
            txtUnidade.setText(String.valueOf(unidade.getNumero()));
            lblUnidadeContexto.setText(
                    "Apto " + unidade.getNumero() + " – " + unidade.getBloco());
        }

        if (petEmEdicao != null) {
            txtNome.setText(petEmEdicao.getNome());
            txtRaca.setText(petEmEdicao.getRaca());
            txtCor.setText(petEmEdicao.getCor());
            tipoSelecionado = petEmEdicao.getTipo();
            cmbTipoPet.setValue(petEmEdicao.getTipo().name());
            destacarCard(tipoSelecionado);
            atualizarPreview();
        } else {
            selecionarCao(null);
        }
    }

    // ───── cards de seleção de tipo ────────────────────────────────────────
    @FXML public void selecionarCao(MouseEvent event) {
        tipoSelecionado = TipoPet.CACHORRO;
        cmbTipoPet.setValue(TipoPet.CACHORRO.name());
        destacarCard(TipoPet.CACHORRO);
        atualizarPreview();
    }

    @FXML public void selecionarGato(MouseEvent event) {
        tipoSelecionado = TipoPet.GATO;
        cmbTipoPet.setValue(TipoPet.GATO.name());
        destacarCard(TipoPet.GATO);
        atualizarPreview();
    }

    @FXML public void selecionarPassaro(MouseEvent event) {
        tipoSelecionado = TipoPet.PASSARO;
        cmbTipoPet.setValue(TipoPet.PASSARO.name());
        destacarCard(TipoPet.PASSARO);
        atualizarPreview();
    }

    @FXML public void selecionarRoedor(MouseEvent event) {
        tipoSelecionado = TipoPet.OUTRO;
        cmbTipoPet.setValue(TipoPet.OUTRO.name());
        destacarCard(null);   // "Roedor" mapeia para OUTRO
        atualizarPreview();
    }

    @FXML public void selecionarReptil(MouseEvent event) {
        tipoSelecionado = TipoPet.OUTRO;
        cmbTipoPet.setValue(TipoPet.OUTRO.name());
        destacarCard(null);
        atualizarPreview();
    }

    @FXML public void selecionarOutro(MouseEvent event) {
        tipoSelecionado = TipoPet.OUTRO;
        cmbTipoPet.setValue(TipoPet.OUTRO.name());
        destacarCard(null);
        atualizarPreview();
    }

    /** Sincroniza seleção do ComboBox → cards e tipo interno */
    @FXML public void sincronizarCards(ActionEvent event) {
        String valor = cmbTipoPet.getValue();
        if (valor == null) return;
        try {
            tipoSelecionado = TipoPet.valueOf(valor);
        } catch (IllegalArgumentException e) {
            tipoSelecionado = TipoPet.OUTRO;
        }
        destacarCard(tipoSelecionado);
        atualizarPreview();
    }

    private void destacarCard(TipoPet tipo) {
        String normal    = "-fx-background-color: white; -fx-background-radius: 14; -fx-border-color: #E2E8F0; -fx-border-radius: 14; -fx-border-width: 2; -fx-padding: 18 16; -fx-cursor: hand;";
        String selecionado = "-fx-background-color: white; -fx-background-radius: 14; -fx-border-color: #059669; -fx-border-radius: 14; -fx-border-width: 2; -fx-padding: 18 16; -fx-cursor: hand; -fx-effect: dropshadow(gaussian, #05966930, 10, 0, 0, 2);";

        cardCao.setStyle(tipo == TipoPet.CACHORRO ? selecionado : normal);
        cardGato.setStyle(tipo == TipoPet.GATO    ? selecionado : normal);
        cardPassaro.setStyle(tipo == TipoPet.PASSARO ? selecionado : normal);
        // Roedor, Réptil e Outro não mapeiam 1:1 — deixamos sem destaque especial
        cardRoedor.setStyle(normal);
        cardReptil.setStyle(normal);
        cardOutro.setStyle(tipo == TipoPet.OUTRO ? selecionado : normal);
    }

    // ───── preview ─────────────────────────────────────────────────────────
    @FXML public void atualizarPreview() {
        String nome  = txtNome.getText();
        String raca  = txtRaca.getText();
        String cor   = txtCor.getText();

        previewNome.setText(nome.isEmpty()  ? "Nome não preenchido" : nome);
        previewRaca.setText("Raça: " + (raca.isEmpty() ? "—" : raca));
        previewCor.setText("Cor: "  + (cor.isEmpty()  ? "—" : cor));
        previewTipo.setText("Tipo: " + (tipoSelecionado != null ? tipoSelecionado.name() : "—"));

        if (previewEmoji != null) {
            previewEmoji.setText(emojiParaTipo(tipoSelecionado));
        }
    }

    private String emojiParaTipo(TipoPet tipo) {
        if (tipo == null) return "🐾";
        switch (tipo) {
            case CACHORRO: return "🐶";
            case GATO:     return "🐱";
            case PASSARO:  return "🐦";
            default:       return "🐾";
        }
    }

    // ───── salvar ──────────────────────────────────────────────────────────
    @FXML
    public void salvarPet(ActionEvent event) {
        if (txtNome.getText().isEmpty()
                || txtRaca.getText().isEmpty()
                || txtCor.getText().isEmpty()) {
            mostrarAlerta("Erro", "Campos obrigatórios",
                    "Preencha Nome, Raça e Cor.",
                    Alert.AlertType.ERROR);
            return;
        }

        Unidade unidade = PetController.getUnidadeSelecionadaGlobal();
        if (unidade == null) {
            mostrarAlerta("Erro", "Unidade não encontrada",
                    "Nenhuma unidade selecionada.",
                    Alert.AlertType.ERROR);
            return;
        }

        try {
            if (petEmEdicao != null) {
                petEmEdicao.setNome(txtNome.getText());
                petEmEdicao.setTipo(tipoSelecionado);
                petEmEdicao.setRaca(txtRaca.getText());
                petEmEdicao.setCor(txtCor.getText());
                petRepo.atualizar(petEmEdicao);

                mostrarAlerta("Sucesso", null, "Pet atualizado!",
                        Alert.AlertType.INFORMATION);

            } else {
                int novoId = petRepo.listar().size() + 1;
                Pet pet = new Pet(
                        novoId,
                        txtNome.getText(),
                        tipoSelecionado,
                        txtRaca.getText(),
                        txtCor.getText());

                unidade.adicionarPet(pet);
                petRepo.salvar(pet);
                unidadeRepo.atualizar(unidade);

                mostrarAlerta("Sucesso", null,
                        "Pet cadastrado na Unidade " + unidade.getNumero() + "!",
                        Alert.AlertType.INFORMATION);
            }

            limparFormulario(null);
            petEmEdicao = null;

        } catch (IllegalArgumentException e) {
            mostrarAlerta("Erro", "Falha ao salvar", e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void limparFormulario(ActionEvent event) {
        txtNome.clear();
        txtRaca.clear();
        txtCor.clear();
        tipoSelecionado = TipoPet.CACHORRO;
        cmbTipoPet.setValue(TipoPet.CACHORRO.name());
        destacarCard(TipoPet.CACHORRO);
        atualizarPreview();
    }

    // ==========================
// NAVEGAÇÃO
// ==========================

    @FXML
    public void voltarPets(ActionEvent event) {
        petEmEdicao = null;
        trocarTela(event, "/com/condominio/unidade/pet-view.fxml");
    }

    @FXML
    public void voltarHome(ActionEvent event) {
        petEmEdicao = null;
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    @FXML
    public void voltarUnidades(ActionEvent event) {
        petEmEdicao = null;
        trocarTela(event, "/com/condominio/unidade/unidade-view.fxml");
    }

// ==========================
// BREADCRUMBS
// ==========================

    @FXML
    public void voltarHome(MouseEvent event) {
        petEmEdicao = null;
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    @FXML
    public void voltarUnidades(MouseEvent event) {
        petEmEdicao = null;
        trocarTela(event, "/com/condominio/unidade/unidade-view.fxml");
    }

    @FXML
    public void voltarDetalhes(MouseEvent event) {
        petEmEdicao = null;
        trocarTela(event, "/com/condominio/unidade/detalhes-unidade-view.fxml");
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
