package com.condominio.models.area;


public class Convidado
{
    private int id;
    private String nome;
    private String documento;

    public Convidado(int id, String nome, String documento)
    {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
    }

    //Getters

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDocumento() {
        return documento;
    }
}
