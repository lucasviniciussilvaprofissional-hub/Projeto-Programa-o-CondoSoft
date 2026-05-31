package com.condominio.controller.reserva;

import com.condominio.models.area.AreaComum;
import com.condominio.models.area.Reserva;
import com.condominio.models.moradia.Morador;
import com.condominio.models.moradia.Unidade;
import com.condominio.enums.StatusReserva;
import com.condominio.repository.implementation.ReservaRepositoryImpl;
import com.condominio.repository.interfaces.IReservaRepository;

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

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ReservaController {

    @FXML private ComboBox<String> cbAreaComum;
    @FXML private DatePicker dpData;
    @FXML private TextField txtHoraInicio;
    @FXML private TextField txtHoraFim;

    private final IReservaRepository repository = new ReservaRepositoryImpl();
    private static int geradorId = 1;

    @FXML
    public void initialize() {
        if (cbAreaComum != null) {
            cbAreaComum.getItems().clear();
            cbAreaComum.getItems().addAll("Salão de Festas", "Churrasqueira", "Quadra Poliesportiva", "Espaço Gourmet");
        }
    }

    @FXML
    private void salvarReserva(ActionEvent event) {
        try {
            if (cbAreaComum.getValue() == null || dpData.getValue() == null ||
                    txtHoraInicio.getText().isEmpty() || txtHoraFim.getText().isEmpty()) {

                exibirAviso("Campos Incompletos", "Por favor, preencha todos os dados.");
                return;
            }

            LocalDate dataEscolhida = dpData.getValue();
            LocalTime horaInicio = LocalTime.parse(txtHoraInicio.getText());
            LocalTime horaFim = LocalTime.parse(txtHoraFim.getText());

            LocalDateTime inicio = LocalDateTime.of(dataEscolhida, horaInicio);
            LocalDateTime fim = LocalDateTime.of(dataEscolhida, horaFim);

            // 1. Recupera a Área Comum selecionada
            AreaComum areaSelecionada = new AreaComum(cbAreaComum.getValue(), 50);

            // 2. RECUPERAÇÃO DO MORADOR REAL DO SISTEMA:
            // Substitua as linhas abaixo pela sua classe de Sessão, Login ou busca no Banco de Dados.
            // Exemplo: Morador moradorReal = SessaoSistema.getUsuarioLogado();
            Morador moradorReal = obterMoradorLogado();
            Unidade unidadeReal = obterUnidadeDoMorador(moradorReal);

            if (moradorReal == null || unidadeReal == null) {
                exibirAviso("Erro de Sessão", "Não foi possível identificar o morador logado para realizar o agendamento.");
                return;
            }

            // Instancia a classe Reserva usando os objetos reais do sistema
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

            // Salva no Repositório Oficial
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

    /**
     * Métodos para você conectar com o seu sistema de Login ou DAO
     */
    private Morador obterMoradorLogado() {
        // TODO: Retornar o objeto Morador que está logado no CondoSoft atualmente
        // Exemplo: return LoginController.getMoradorAutenticado();
        return null;
    }

    private Unidade obterUnidadeDoMorador(Morador morador) {
        // TODO: Retornar a unidade vinculada a este morador
        // Exemplo: return unidadeDAO.buscarPorMorador(morador.getId());
        return null;
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