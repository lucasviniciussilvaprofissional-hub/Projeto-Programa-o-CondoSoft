package com.condominio.area;

import com.condominio.model.Morador;
import com.condominio.model.Unidade;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Reserva
{
    private int id;
    private AreaComum area;
    private Unidade unidade;
    private Morador responsavel;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private int quantidadePessoas;
    private List<Convidado> convidados;

    public Reserva(int id, AreaComum area, Unidade unidade, Morador responsavel,
                   LocalDateTime dataInicio, LocalDateTime dataFim, int quantidadePessoas) {

        this.id = id;
        this.area = area;
        this.unidade = unidade;
        this.responsavel = responsavel;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.quantidadePessoas = quantidadePessoas;
        this.convidados = new ArrayList<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setArea(AreaComum area) {
        this.area = area;
    }

    public void setUnidade(Unidade unidade) {
        this.unidade = unidade;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public void setQuantidadePessoas(int quantidadePessoas) {
        this.quantidadePessoas = quantidadePessoas;
    }

    public void setResponsavel(Morador responsavel) {
        this.responsavel = responsavel;
    }

    //Getters


    public Unidade getUnidade() {
        return unidade;
    }

    public int getId() {
        return id;
    }

    public AreaComum getArea() {
        return area;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public int getQuantidadePessoas() {
        return quantidadePessoas;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public Morador getResponsavel() {
        return responsavel;
    }

    public List<Convidado> getConvidados() {
        return convidados;
    }

    public void adicionarConvidado(int id, String nome, String documento) {
        Convidado convidado = new Convidado(id, nome, documento);
        this.convidados.add(convidado);
    }
}
