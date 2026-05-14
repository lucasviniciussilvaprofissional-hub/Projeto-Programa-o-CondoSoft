package com.condominio.models.moradia;

public class Dependente extends Morador
{
    private Morador responsavel;
    public Dependente(int id, String nome, String cpf, String telefone, Unidade unidade, Morador responsavel, String email)
    {
        super(id,nome,cpf,telefone,unidade,email);

        if (responsavel == null)
        {
            throw new IllegalArgumentException("Não pode ter dependente sem responsavel");
        }
        this.responsavel = responsavel;
    }
}
