package com.condominio.controller.reserva;

import com.condominio.models.area.AreaComum;
import com.condominio.models.area.Reserva;
import com.condominio.models.moradia.Morador;
import com.condominio.models.moradia.Unidade;
import com.condominio.enums.StatusReserva;
import com.condominio.repository.implementation.ReservaRepositoryImpl;
import com.condominio.repository.implementation.UnidadeRepositoryImpl;
import com.condominio.repository.interfaces.IReservaRepository;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

public class ReservaController {

    @FXML private ComboBox<String> cbAreaComum;
    @FXML private ComboBox<Unidade> cbUnidade; // Alterado para receber o objeto Unidade
    @FXML private ComboBox<Morador> cbMorador; // Alterado para receber o objeto Morador
    @FXML private DatePicker dpData;
    @FXML private TextField txtHoraInicio;
    @FXML private TextField txtHoraFim;

    private final IReservaRepository repository = new ReservaRepositoryImpl();
    private final UnidadeRepositoryImpl unidadeRepo = new UnidadeRepositoryImpl();
    private static int geradorId = 1;

    @FXML
    public void initialize() {
        // 1. Popula as Áreas Comuns
        if (cbAreaComum != null) {
            cbAreaComum.getItems().clear();
            cbAreaComum.getItems().addAll("Salão de Festas", "Churrasqueira", "Quadra Poliesportiva", "Espaço Gourmet");
        }

        // 2. Configura a exibição e popula a lista ordenada de Unidades
        configurarFormatacaoComboBoxes();
        carregarUnidadesOrdenadas();
    }

    private void configurarFormatacaoComboBoxes() {
        // Configura para mostrar "Apto X - Bloco Y" no ComboBox de Unidades
        cbUnidade.setConverter(new StringConverter<Unidade>() {
            @Override
            public String toString(Unidade u) {
                return u == null ? "" : "Apto " + u.getNumero() + " - Bloco " + u.getBloco();
            }
            @Override
            public Unidade fromString(String string) { return null; }
        });

        // Configura para mostrar apenas o nome do morador no ComboBox de Moradores
        cbMorador.setConverter(new StringConverter<Morador>() {
            @Override
            public String toString(Morador m) {
                return m == null ? "" : m.getNome();
            }
            @Override
            public Morador fromString(String string) { return null; }
        });
    }

    private void carregarUnidadesOrdenadas() {
        // Busca do banco de dados e ordena pelo número do apartamento
        List<Unidade> unidades = unidadeRepo.listar();
        unidades.sort(Comparator.comparing(Unidade::getNumero));
        cbUnidade.setItems(FXCollections.observableArrayList(unidades));
    }

    @FXML
    public void aoSelecionarUnidade(ActionEvent event) {
        Unidade unidadeSelecionada = cbUnidade.getValue();

        if (unidadeSelecionada != null) {
            // Limpa a seleção anterior de morador para evitar conflitos
            cbMorador.setValue(null);

            // Puxa e preenche dinamicamente os moradores pertencentes estritamente a esta residência
            List<Morador> moradoresDaCasa = unidadeSelecionada.getMoradores();
            cbMorador.setItems(FXCollections.observableArrayList(moradoresDaCasa));

            if(moradoresDaCasa.isEmpty()) {
                cbMorador.setPromptText("Nenhum morador nesta unidade");
            } else {
                cbMorador.setPromptText("Selecione um morador...");
            }
        }
    }

    @FXML
    public void abrirSelecaoUnidade(ActionEvent event) {
        // Mantido vazio conforme solicitado para evitar quebras com o botão antigo do seu FXML
        System.out.println("Botão antigo 'Selecionar Unidade' clicado (Lógica descontinuada).");
    }

    @FXML
    private void salvarReserva(ActionEvent event) {
        try {
            // Validação atualizada contendo os objetos de Unidade e Morador selecionados nas listas
            if (cbAreaComum.getValue() == null || cbUnidade.getValue() == null ||
                    cbMorador.getValue() == null || dpData.getValue() == null ||
                    txtHoraInicio.getText().isEmpty() || txtHoraFim.getText().isEmpty()) {

                exibirAviso("Campos Incompletos", "Por favor, preencha todos os dados.");
                return;
            }

            LocalDate dataEscolhida = dpData.getValue();
            LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText());
            LocalTime horaFim = LocalTime.parse(txtHoraFim.getText());

            LocalDateTime inicio = LocalDateTime.of(dataEscolhida, horaInicio);
            LocalDateTime fim = LocalDateTime.of(dataEscolhida, horaFim);

            AreaComum areaSelecionada = new AreaComum(cbAreaComum.getValue(), 50);

            // Resgata os objetos reais diretamente dos seletores visuais
            Unidade unidadeReal = cbUnidade.getValue();
            Morador moradorReal = cbMorador.getValue();

            // Instancia a classe Reserva com os dados coletados da tela
            Reserva novaReserva = new Reserva(
                    geradorId++,
                    areaSelecionada,
                    unidadeReal,
                    moradorReal,
                    inicio,
                    fim,
                    1,
                    StatusReserva.ATIVA
            );

            // Salva no Repositório
            repository.salvar(novaReserva);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso!");
            alert.setHeaderText(null);
            alert.setContentText("Sua reserva foi agendada e salva com sucesso!");
            alert.showAndWait();

            voltarHome(event);

        } catch (Exception e) {
            exibirAviso("Erro no Formulário", "Verifique se a hora está no formato correto (ex: 14:00).\nErro: " + e.getMessage());
        }
    }

    @FXML
    private void voltarHome(ActionEvent event) {
        String[] caminhosHome = {
                "/com/condominio/home-view.fxml",
                "/com/condominio/unidade/home-view.fxml",
                "/home-view.fxml"
        };

        URL urlHome = null;
        for (String caminho : caminhosHome) {
            urlHome = getClass().getResource(caminho);
            if (urlHome != null) break;
        }

        try {
            if (urlHome == null) throw new IOException("Home não encontrada.");
            Parent root = FXMLLoader.load(urlHome);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("CondoSoft - Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void exibirAviso(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}