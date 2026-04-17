package com.condominio.model;

import java.util.ArrayList;
import java.util.List;

public class Unidade {
    private int id;
    private String bloco;
    private String status;
    private double fracaoIdeal;
    private List<Morador> moradores;
    private List<Veiculo> veiculos;
    private List<Pet> pets;

    public Unidade(int id, String bloco, String status, double fracaoIdeal)
    {
        this.id = id;
        this.bloco = bloco;
        this.status = status;
        this.fracaoIdeal = fracaoIdeal;
        this.moradores = new ArrayList<>();
        this.veiculos = new ArrayList<>();
        this.pets = new ArrayList<>();
    }

    //Setters-------------------------------------------------------------------------------------------------

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
        this.moradores.add(morador);
    }

    public void  adicionarVeiculo(Veiculo veiculo)
    {
        this.veiculos.add(veiculo);
    }
    public void adicionarPet(Pet pet)
    {
        this.pets.add(pet);
    }
}
