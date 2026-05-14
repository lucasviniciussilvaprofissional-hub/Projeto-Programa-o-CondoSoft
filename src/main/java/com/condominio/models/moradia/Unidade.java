package com.condominio.models.moradia;

import java.util.ArrayList;
import java.util.List;

public class Unidade {
    private int id;
    private int numero;
    private String bloco;
    private String tipo;
    private String status;
    private String metragem;
    private double fracaoIdeal;
    private String situacaoFinanceira;
    private int capacidade;
    private List<Morador> moradores;
    private List<Veiculo> veiculos;
    private List<Pet> pets;


    public Unidade(int id, String bloco, String status, double fracaoIdeal, int numero,  String tipo, String metragem, int capacidade, String situacaoFinanceira)
    {
        this.id = id;
        this.numero = numero;
        this.tipo = tipo;
        this.metragem = metragem;
        this.bloco = bloco;
        this.status = status;
        this.fracaoIdeal = fracaoIdeal;
        this.moradores = new ArrayList<>();
        this.veiculos = new ArrayList<>();
        this.pets = new ArrayList<>();
        this.capacidade = capacidade;
        this.situacaoFinanceira = situacaoFinanceira;
    }

    //Setters-------------------------------------------------------------------------------------------------


    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public void setSituacaoFinanceira(String situacaoFinanceira) {
        this.situacaoFinanceira = situacaoFinanceira;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setMetragem(String metragem) {
        this.metragem = metragem;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBloco(String bloco) {
        this.bloco = bloco;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setFracaoIdeal(double fracaoIdeal) {
        this.fracaoIdeal = fracaoIdeal;
    }

    public void setMoradores(List<Morador> moradores) {
        this.moradores = moradores;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public void setVeiculos(List<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }
    //Getters---------------------------------------------------------------------------------------------


    public int getCapacidade() {
        return capacidade;
    }

    public String getSituacaoFinanceira() {
        return situacaoFinanceira;
    }

    public String getMetragem() {
        return metragem;
    }

    public String getTipo() {
        return tipo;
    }

    public int getNumero() {
        return numero;
    }

    public int getId() {
        return id;
    }

    public String getBloco() {
        return bloco;
    }

    public String getStatus() {
        return status;
    }

    public double getFracaoIdeal() {
        return fracaoIdeal;
    }

    public List<Morador> getMoradores() {
        return moradores;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    //Métodos--------------------------------------------------------------------------------------------

    public void adicionarMorador(Morador morador)
    {
        if (morador == null)
        {
            return;
        };
        if (!moradores.contains(morador))
        {
            moradores.add(morador);
            morador.setUnidade(this);
        }
        this.moradores.add(morador);
    }

    public void  adicionarVeiculo(Veiculo veiculo)
    {
        if (veiculo == null) return;

        if (!veiculos.contains(veiculo)) {
            veiculos.add(veiculo);
            veiculo.setUnidade(this);
        }
    }
    public void adicionarPet(Pet pet)
    {
        if (pet == null) return;

        if (!pets.contains(pet)) {
            pets.add(pet);
            pet.setUnidade(this);
    }
}
}
