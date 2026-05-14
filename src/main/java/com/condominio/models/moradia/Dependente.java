package com.condominio.models.moradia;

public class Dependente extends Morador
{
    private Morador responsavel;
    public Dependente(int id, String nome, String cpf, String telefone, Unidade unidade, Morador responsavel)
    {
        super(id,nome,cpf,telefone,unidade);

        if (responsavel == null)
        {
            throw new IllegalArgumentException("Não pode ter dependente sem responsavel");
        }
        this.responsavel = responsavel;
    }
}
