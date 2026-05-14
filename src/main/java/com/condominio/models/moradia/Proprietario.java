package com.condominio.models.moradia;

public class Proprietario extends Morador
{
    public Proprietario(int id, String nome, String cpf, String telefone, Unidade unidade)
    {
        super(id,nome,cpf,telefone,unidade);
    }
}
