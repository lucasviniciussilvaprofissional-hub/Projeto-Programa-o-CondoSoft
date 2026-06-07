package com.condominio.models.area;

import com.condominio.models.moradia.Morador;
import com.condominio.models.moradia.Unidade;
import com.condominio.enums.StatusReserva;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Reserva {

    private int id;
    private AreaComum area;
    private Unidade unidade;
    private Morador responsavel;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private int quantidadePessoas;
    private List<Convidado> convidados;
    private StatusReserva status;

    public Reserva(int id, AreaComum area, Unidade unidade, Morador responsavel,
                   LocalDateTime dataInicio, LocalDateTime dataFim, int quantidadePessoas, StatusReserva status) {
        this.id = id;
        this.area = area;
        this.unidade = unidade;
        this.responsavel = responsavel;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.quantidadePessoas = quantidadePessoas;
        this.status = status;
        this.convidados = new ArrayList<>();
    }

    // =========================================================================
    // MÉTODOS DE REGRAS DE NEGÓCIO (EXIGIDOS PELO RESERVASERVICE)
    // =========================================================================

    /**
     * REQ09 - Adiciona um convidado à lista de acesso da reserva e incrementa
     * a contagem de pessoas atual da reserva.
     */
    public void adicionarConvidado(int idConvidado, String nome, String documento) {
        if (this.status != StatusReserva.ATIVA) {
            throw new IllegalStateException("Não é possível adicionar convidados a uma reserva que não está ATIVA.");
        }

        Convidado novoConvidado = new Convidado(idConvidado, nome, documento);
        this.convidados.add(novoConvidado);

        // Incrementa a quantidade de pessoas presentes na reserva para bater com o REQ17 do Service
        this.quantidadePessoas++;
    }

    /**
     * REQ10 - Modifica o status da reserva para FINALIZADA.
     */
    public void finalizarReserva() {
        if (this.status != StatusReserva.ATIVA) {
            throw new IllegalStateException("Apenas reservas com status ATIVA podem ser finalizadas.");
        }
        this.status = StatusReserva.FINALIZADA;
    }

    /**
     * Modifica o status da reserva para CANCELADA.
     */
    public void cancelarReserva() {
        if (this.status == StatusReserva.FINALIZADA) {
            throw new IllegalStateException("Não é possível cancelar uma reserva que já foi finalizada.");
        }
        this.status = StatusReserva.CANCELADA;
    }

    // =========================================================================
    // MÉTODOS OBRIGATÓRIOS PARA O JAVAFX EXIBIR O TEXTO NA TABELA
    // =========================================================================

    public String getStringInicio() {
        if (this.dataInicio == null) return "";
        return this.dataInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getNomeArea() {
        return (this.area != null) ? this.area.getNome() : "Não definida";
    }

    public String getNomeResponsavel() {
        return (this.responsavel != null) ? this.responsavel.getNome() : "Não informado";
    }

    public String getStringFim() {
        if (this.dataFim == null) return "";
        return this.dataFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getStringStatus() {
        return (this.status != null) ? this.status.toString() : "ATIVA";
    }

    public int getNumeroUnidade() {
        return (this.unidade != null) ? this.unidade.getNumero() : 0;
    }

    // =========================================================================
    // GETTERS E SETTERS TRADICIONAIS
    // =========================================================================

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public AreaComum getArea() { return area; }
    public void setArea(AreaComum area) { this.area = area; }
    public Unidade getUnidade() { return unidade; }
    public void setUnidade(Unidade unidade) { this.unidade = unidade; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }
    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }
    public int getQuantidadePessoas() { return quantidadePessoas; }
    public void setQuantidadePessoas(int quantidadePessoas) { this.quantidadePessoas = quantidadePessoas; }
    public Morador getResponsavel() { return responsavel; }
    public void setResponsavel(Morador responsavel) { this.responsavel = responsavel; }
    public StatusReserva getStatus() { return status; }
    public void setStatus(StatusReserva status) { this.status = status; }
    public List<Convidado> getConvidados() { return convidados; }
    public void setConvidados(List<Convidado> convidados) { this.convidados = convidados; }
}