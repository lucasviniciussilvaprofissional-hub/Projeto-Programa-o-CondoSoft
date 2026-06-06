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
}