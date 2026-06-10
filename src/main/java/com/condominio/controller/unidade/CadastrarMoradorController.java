package com.condominio.controller.unidade;

import com.condominio.models.moradia.Morador;
import com.condominio.models.moradia.Unidade;
import com.condominio.models.moradia.Proprietario;
import com.condominio.models.moradia.Inquilino;
import com.condominio.models.moradia.Dependente;
import com.condominio.enums.StatusMorador;
import com.condominio.service.CondominioService;

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
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CadastrarMoradorController {

    private static Unidade unidadeSelecionadaGlobal = null;

    // Método para os outros controllers setarem a unidade antes de abrir a tela, se necessário
    public static void setUnidadeSelecionadaGlobal(Unidade unidade) {
        unidadeSelecionadaGlobal = unidade;
    }

    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtCpf;
    @FXML
    private TextField txtTelefone;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtUnidade;
    @FXML
    private TextArea txtDescricao;

    @FXML
    private ComboBox<String> cmbStatus;
    @FXML
    private ComboBox<String> cmbStatusContrato;
    @FXML
    private ComboBox<String> cmbResponsavel;
    @FXML
    private ComboBox<String> cmbParentesco;

    @FXML
    private DatePicker dpDataInicioContrato;

    @FXML
    private VBox cardProprietario;
    @FXML
    private VBox cardInquilino;
    @FXML
    private VBox cardDependente;
    @FXML
    private VBox secaoInquilino;
    @FXML
    private VBox secaoDependente;

    private String tipoSelecionado = "PROPRIETARIO";

    // Instância do serviço do sistema
    private final CondominioService condominioService = new CondominioService(
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()
    );

    @FXML
    public void initialize() {
        cmbStatus.getItems().addAll("Ativo", "Inativo");
        cmbStatusContrato.getItems().addAll("Ativo", "Encerrado", "Pendente");
        cmbParentesco.getItems().addAll("Filho(a)", "Cônjuge", "Pai", "Mãe", "Irmão(a)", "Outro");

        // Se a unidade veio guardada da tela anterior, preenche e trava o campo
        if (unidadeSelecionadaGlobal != null) {
            txtUnidade.setText(String.valueOf(unidadeSelecionadaGlobal.getNumero()));
            txtUnidade.setEditable(false);
            txtUnidade.setStyle("-fx-background-color: #F1F5F9; -fx-text-fill: #64748B;");
        }

        selecionarProprietario(null);
    }

    @FXML
    public void salvarMorador(ActionEvent event) {
        // Valida apenas os dados do morador. Não força criar unidade.
        if (txtNome.getText().isEmpty() || txtCpf.getText().isEmpty() || cmbStatus.getValue() == null) {
            exibirAlerta("Erro", "Campos obrigatórios", "Por favor, preencha Nome, CPF e Status.", Alert.AlertType.ERROR);
            return;
        }

        try {
            String nome = txtNome.getText();
            String cpf = txtCpf.getText().replaceAll("[^0-9]", "");
            String telefone = txtTelefone.getText();
            String email = txtEmail.getText();
            StatusMorador statusMorador = StatusMorador.valueOf(cmbStatus.getValue().toUpperCase());

            // 1. PEGAR A INFORMAÇÃO DA UNIDADE EXISTENTE (Não cria uma nova)
            Unidade unidade = unidadeSelecionadaGlobal;

            // Se por acaso a global sumiu, tenta buscar no banco/serviço pelo número que está escrito no campo
            if (unidade == null && !txtUnidade.getText().isEmpty()) {
                String numeroBusca = txtUnidade.getText();
                unidade = condominioService.listarUnidades().stream()
                        .filter(u -> String.valueOf(u.getNumero()).equals(numeroBusca) || String.valueOf(u.getId()).equals(numeroBusca))
                        .findFirst()
                        .orElse(null);
            }

            // Se mesmo assim não achar a unidade no sistema, avisa o usuário
            if (unidade == null) {
                exibirAlerta("Erro", "Unidade não encontrada", "Não foi possível vincular o morador porque a Unidade correspondente não está carregada no sistema.", Alert.AlertType.ERROR);
                return;
            }

            int idNovoMorador = condominioService.listarMoradores().size() + 1;
            Morador morador;

            // 2. ADERIR ÀS ABAS/CARDS SELECIONADOS
            switch (tipoSelecionado) {
                case "PROPRIETARIO":
                    morador = new Proprietario(idNovoMorador, nome, cpf, telefone, unidade, email, statusMorador);
                    break;

                case "INQUILINO":
                    LocalDateTime dataContrato = dpDataInicioContrato.getValue() != null
                            ? dpDataInicioContrato.getValue().atStartOfDay()
                            : LocalDateTime.now();
                    morador = new Inquilino(idNovoMorador, nome, cpf, telefone, unidade, email, dataContrato, statusMorador);
                    break;

                case "DEPENDENTE":
                    String cpfResponsavel = cmbResponsavel.getValue();
                    if (cpfResponsavel == null || cpfResponsavel.isEmpty()) {
                        exibirAlerta("Erro", "Responsável Ausente", "Selecione o CPF do responsável.", Alert.AlertType.ERROR);
                        return;
                    }
                    Morador responsavel = condominioService.listarMoradores().stream()
                            .filter(m -> m.getCpf().equals(cpfResponsavel.replaceAll("[^0-9]", "")))
                            .findFirst().orElse(null);

                    morador = new Dependente(idNovoMorador, nome, cpf, telefone, unidade, responsavel, email, statusMorador);
                    break;

                default:
                    return;
            }

            // 3. SALVA APENAS O MORADOR NO REPOSITÓRIO VINCULADO À UNIDADE ACHADA
            condominioService.adicionarMorador(morador, unidade);

            exibirAlerta("Sucesso", null, "Morador saved e vinculado à Unidade " + unidade.getNumero() + "!", Alert.AlertType.INFORMATION);
            limparCampos();

        } catch (Exception e) {
            exibirAlerta("Erro", "Falha ao salvar", e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void exibirAlerta(String titulo, String cabecalho, String conteudo, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecalho);
        alert.setContentText(conteudo);
        alert.showAndWait();
    }

    // ==========================
    // SELEÇÃO DE ABAS (CONTROLE VISUAL)
    // ==========================
    @FXML
    public void selecionarProprietario(MouseEvent event) {
        tipoSelecionado = "PROPRIETARIO";
        cardProprietario.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-border-color: #2563EB; -fx-border-radius: 14; -fx-border-width: 2; -fx-padding: 22 16; -fx-cursor: hand;");
        cardInquilino.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-border-color: #E2E8F0; -fx-border-radius: 14; -fx-border-width: 2; -fx-padding: 22 16; -fx-cursor: hand;");
        cardDependente.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-border-color: #E2E8F0; -fx-border-radius: 14; -fx-border-width: 2; -fx-padding: 22 16; -fx-cursor: hand;");
        secaoInquilino.setVisible(false);
        secaoInquilino.setManaged(false);
        secaoDependente.setVisible(false);
        secaoDependente.setManaged(false);
    }

    @FXML
    public void selecionarInquilino(MouseEvent event) {
        tipoSelecionado = "INQUILINO";
        cardProprietario.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-border-color: #E2E8F0; -fx-border-radius: 14; -fx-border-width: 2; -fx-padding: 22 16; -fx-cursor: hand;");
        cardInquilino.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-border-color: #2563EB; -fx-border-radius: 14; -fx-border-width: 2; -fx-padding: 22 16; -fx-cursor: hand;");
        cardDependente.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-border-color: #E2E8F0; -fx-border-radius: 14; -fx-border-width: 2; -fx-padding: 22 16; -fx-cursor: hand;");
        secaoInquilino.setVisible(true);
        secaoInquilino.setManaged(true);
        secaoDependente.setVisible(false);
        secaoDependente.setManaged(false);
    }

    @FXML
    public void selecionarDependente(MouseEvent event) {

        tipoSelecionado = "DEPENDENTE";

        cardProprietario.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-border-color: #E2E8F0; -fx-border-radius: 14; -fx-border-width: 2; -fx-padding: 22 16; -fx-cursor: hand;");
        cardInquilino.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-border-color: #E2E8F0; -fx-border-radius: 14; -fx-border-width: 2; -fx-padding: 22 16; -fx-cursor: hand;");
        cardDependente.setStyle("-fx-background-color: white; -fx-background-radius: 14; -fx-border-color: #2563EB; -fx-border-radius: 14; -fx-border-width: 2; -fx-padding: 22 16; -fx-cursor: hand;");

        secaoDependente.setVisible(true);
        secaoDependente.setManaged(true);

        secaoInquilino.setVisible(false);
        secaoInquilino.setManaged(false);

        // ============================
        // CARREGA RESPONSÁVEIS
        // ============================

        cmbResponsavel.getItems().clear();

        for (Morador morador : condominioService.listarMoradores()) {

            if (unidadeSelecionadaGlobal != null
                    && morador.getUnidade() != null
                    && morador.getUnidade().getId() == unidadeSelecionadaGlobal.getId()) {

                if (!(morador instanceof Dependente)) {
                    cmbResponsavel.getItems().add(
                            morador.getNome() + " - " + morador.getCpf()
                    );
                }
            }
        }
    }

    @FXML
    public void limparFormulario(ActionEvent event) {
        limparCampos();
    }

    private void limparCampos() {
        txtNome.clear();
        txtCpf.clear();
        txtTelefone.clear();
        txtEmail.clear();
        txtDescricao.clear();
        if (unidadeSelecionadaGlobal == null) txtUnidade.clear();
        cmbStatus.setValue(null);
        cmbStatusContrato.setValue(null);
        cmbResponsavel.setValue(null);
        cmbParentesco.setValue(null);
        dpDataInicioContrato.setValue(null);
        selecionarProprietario(null);
    }
// =========================================================================
// NAVEGAÇÃO — Corrigido para voltar exatamente para a página da Unidade
// =========================================================================

    @FXML
    public void voltarTopo(ActionEvent event) {
        trocarTela(event, "/com/condominio/unidade/unidade-view.fxml");
    }

    @FXML
    public void voltarTopoIcone(MouseEvent event) {
        trocarTela(event, "/com/condominio/unidade/unidade-view.fxml");
    }

    @FXML
    public void voltarGerenciarMoradores(ActionEvent event) {
        // Alinhado para voltar para a página de visualização da Unidade
        trocarTela(event, "/com/condominio/unidade/unidade-view.fxml");
    }

    @FXML
    public void voltarHome(MouseEvent event) {
        trocarTela(event, "/com/condominio/home-view.fxml");
    }

    @FXML
    public void voltarUnidades(MouseEvent event) {
        trocarTela(event, "/com/condominios/unidade/unidade-view.fxml");
    }

    @FXML
    public void voltarDetalhes(MouseEvent event) {
        trocarTela(event, "/com/condominio/unidade/unidade-view.fxml");
    }

    private void trocarTela(javafx.event.Event event, String caminhoFXML) {
        try {
            URL url = getClass().getResource(caminhoFXML);

            if (url == null && caminhoFXML.startsWith("/")) {
                url = Thread.currentThread().getContextClassLoader().getResource(caminhoFXML.substring(1));
            }

            if (url == null) {
                throw new IOException("O arquivo FXML de destino não foi localizado em: " + caminhoFXML);
            }

            Parent root = FXMLLoader.load(url);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            System.err.println("[CondoSoft] Falha crítica de redirecionamento: " + e.getMessage());
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Interface");
            alert.setHeaderText("A tela solicitada não pôde ser carregada.");
            alert.setContentText("Caminho afetado: " + caminhoFXML + "\nVerifique se o nome do arquivo fxml na pasta 'unidade' é exatamente unidade-view.fxml");
            alert.showAndWait();
        }
    }
}