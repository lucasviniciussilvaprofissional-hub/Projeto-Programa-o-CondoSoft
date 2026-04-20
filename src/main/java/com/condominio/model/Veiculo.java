package com.condominio.model;

public class Veiculo
{
    private int id;
    private String placa;
    private String modelo;
    private String marca;
    private String cor;


    //Setters------------------------------------------------------------------------------------------------


    public void setId(int id) {
        this.id = id;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }


    //Getters---------------------------------------------------------------------------------------------

    public int getId() {
        return id;
    }

    public String getCor() {
        return cor;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getPlaca() {
        return placa;
    }

}
