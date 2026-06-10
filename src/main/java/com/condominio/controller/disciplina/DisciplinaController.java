package com.condominio.controller.disciplina;

import com.condominio.enums.StatusOcorrencia;
import com.condominio.enums.TipoOcorrencia;
import com.condominio.models.disciplina.Ocorrencia;
import com.condominio.models.moradia.Morador;
import com.condominio.repository.implementation.MoradorRepositoryImpl;
import com.condominio.repository.implementation.OcorrenciaRepositoryImpl;
import com.condominio.repository.interfaces.IOcorrenciaRepository;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DisciplinaController {

    // ────────────────────────────────────────────────────────────────────────
    // FXML Componentes
    // ────────────────────────────────────────────────────────────────────────
    @FXML private ComboBox<String> cbUnidade; // Populado via Repositório de Moradores
    @FXML private ComboBox<String> cmbTipoOcorrencia;
    @FXML private TextField txtMorador; // Apenas exibe ou recebe o texto do nome desejado
    @FXML private TextArea txtDescricao;

    @FXML private Button btnVoltarHome;
    @FXML private Button btnRegistrarOcorrencia;

    // Contadores
    @FXML private Label lblTotalOcorrencias;
    @FXML private Label lblAdvertencias;
    @FXML private Label lblMultas;

    // Tabela Histórica
    @FXML private TableView<Ocorrencia> tabelaOcorrencias;
    @FXML private TableColumn<Ocorrencia, String> colUnidade;
    @FXML private TableColumn<Ocorrencia, String> colMorador;
    @FXML private TableColumn<Ocorrencia, String> colTipo;
    @FXML private TableColumn<Ocorrencia, String> colData;
    @FXML private TableColumn<Ocorrencia, String> colStatus;

    // ────────────────────────────────────────────────────────────────────────
    // Repositories
    // ────────────────────────────────────────────────────────────────────────
    private final IOcorrenciaRepository repository = new OcorrenciaRepositoryImpl();
    private final MoradorRepositoryImpl moradorRepo = new MoradorRepositoryImpl();

    // ────────────────────────────────────────────────────────────────────────
    // Inicialização
    // ────────────────────────────────────────────────────────────────────────
    @FXML
    public void initialize() {
        // 1. Popula Tipos de Ocorrência
        cmbTipoOcorrencia.getItems().clear();
        cmbTipoOcorrencia.getItems().addAll(
                "Advertencia",
                "Barulho Excessivo",
                "Uso Indevido de Area Comum",
                "Infracao de Regimento",
                "Multa"
        );

        // 2. Popula Unidades a partir dos Moradores Cadastrados (Igual ao NovaReservaController)
        cbUnidade.getItems().clear();
        for (Morador m : moradorRepo.listar()) {
            if (m.getUnidade() != null) {
                cbUnidade.getItems().add(labelMorador(m));
            }
        }
        if (cbUnidade.getItems().isEmpty()) {
            cbUnidade.getItems().add("(Nenhum morador cadastrado)");
        }

        // Listener opcional: Se quiser autocompletar o campo do Morador ao escolher a Unidade
        cbUnidade.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null && !novo.startsWith("(")) {
                String nomeMorador = novo.split(" - ")[0].trim();
                txtMorador.setText(nomeMorador);
            }
        });

        // 3. Inicializa componentes de visualização de dados
        configurarColunas();
        atualizarContadores();
        atualizarTabela();
    }

    private String labelMorador(Morador m) {
        return m.getNome() + " - Apto " + m.getUnidade().getNumero() + " / " + m.getUnidade().getBloco();
    }

    private void configurarColunas() {
        colUnidade.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNomeUnidadeFormatado())
        );

        colMorador.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNomeMoradorFormatado())
        );

        colTipo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTipo() != null ? cellData.getValue().getTipo().toString() : "")
        );

        colStatus.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus() != null ? cellData.getValue().getStatus().toString() : "")
        );

        colData.setCellValueFactory(cellData -> {
            LocalDateTime data = cellData.getValue().getDataCriacao();
            if (data != null) {
                return new SimpleStringProperty(data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            }
            return new SimpleStringProperty("");
        });
    }

    // ────────────────────────────────────────────────────────────────────────
    // Regras de Negócio e Gravação
    // ────────────────────────────────────────────────────────────────────────
    @FXML
    private void registrarOcorrencia() {
        String unidadeSelecionada = cbUnidade.getValue();
        String nomeMorador = txtMorador.getText();
        String tipoStr = cmbTipoOcorrencia.getValue();
        String descricao = txtDescricao.getText();

        if (unidadeSelecionada == null || unidadeSelecionada.startsWith("(")
                || nomeMorador == null || nomeMorador.isBlank()
                || tipoStr == null || descricao == null || descricao.isBlank()) {
            alerta("Preencha Unidade, Morador envolvido, Tipo e Descricao.");
            return;
        }

        // Resgata o Morador do repositório correspondente à Label selecionada (Sem instanciar novos)
        Morador responsavel = moradorByLabel(unidadeSelecionada);
        if (responsavel == null) {
            alerta("Morador cadastrado nao encontrado para esta unidade.");
            return;
        }

        // Mapeamento seguro de String limpa para Enum real do sistema
        TipoOcorrencia tipo = TipoOcorrencia.OUTROS;
        if (tipoStr.contains("Advert")) tipo = TipoOcorrencia.ADVERTENCIA;
        else if (tipoStr.contains("Barulho")) tipo = TipoOcorrencia.BARULHO_EXCESSIVO;
        else if (tipoStr.contains("Area")) tipo = TipoOcorrencia.USO_INDEVIDO_AREA_COMUM;
        else if (tipoStr.contains("Regimento")) tipo = TipoOcorrencia.INFRACAO_REGIMENTO;
        else if (tipoStr.contains("Multa")) tipo = TipoOcorrencia.MULTA;

        int novoId = repository.listar().size() + 1;

        // Instancia a Ocorrencia vinculando as referências encontradas nos Repositories
        Ocorrencia ocorrencia = new Ocorrencia(
                novoId,
                "Ocorrencia: " + tipoStr,
                descricao,
                tipo,
                StatusOcorrencia.ABERTA,
                responsavel,
                responsavel.getUnidade(),
                LocalDateTime.now()
        );

        repository.salvar(ocorrencia);

        atualizarTabela();
        atualizarContadores();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText("Ocorrencia registrada com sucesso!");
        alert.showAndWait();

        limparFormulario();
    }

    private void atualizarTabela() {
        tabelaOcorrencias.getItems().clear();
        tabelaOcorrencias.getItems().addAll(repository.listar());
    }

    private void atualizarContadores() {
        int total = repository.listar().size();
        int advertencias = 0;
        int multas = 0;

        for (Ocorrencia o : repository.listar()) {
            if (o.getTipo() != null) {
                String nomeEnum = o.getTipo().name().toLowerCase();
                if (nomeEnum.contains("advert")) advertencias++;
                if (nomeEnum.contains("multa")) multas++;
            }
        }

        lblTotalOcorrencias.setText(String.valueOf(total));
        lblAdvertencias.setText(String.valueOf(advertencias));
        lblMultas.setText(String.valueOf(multas));
    }

    @FXML
    private void limparFormulario() {
        txtMorador.clear();
        txtDescricao.clear();
        cbUnidade.getSelectionModel().clearSelection();
        cmbTipoOcorrencia.getSelectionModel().clearSelection();
    }

    private Morador moradorByLabel(String label) {
        for (Morador m : moradorRepo.listar()) {
            if (m.getUnidade() != null && labelMorador(m).equals(label)) return m;
        }
        return null;
    }

    private void alerta(String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait();
    }

    // ────────────────────────────────────────────────────────────────────────
    // Navegação Receptiva (Suporta MouseEvent e ActionEvent)
    // ────────────────────────────────────────────────────────────────────────
    @FXML private void voltarHome(ActionEvent event) { trocarTela(event, "/com/condominio/home-view.fxml"); }
    @FXML private void voltarHomeMouse(MouseEvent event) { trocarTela(event, "/com/condominio/home-view.fxml"); }

    private void trocarTela(javafx.event.Event event, String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private void filtrarOcorrencias() {}
    @FXML private void visualizarOcorrencia() {}
    @FXML private void aplicarMulta() {}
}