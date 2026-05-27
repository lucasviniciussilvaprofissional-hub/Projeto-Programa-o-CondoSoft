package com.condominio.models.moradia;
import  com.condominio.enums.StatusMorador;

public class Dependente extends Morador
{
    private Morador responsavel;
    public Dependente(int id, String nome, String cpf, String telefone, Unidade unidade, Morador responsavel, String email, StatusMorador status)
    {
        super(id,nome,cpf,telefone,unidade,email, status);

        if (responsavel == null)
        {
            throw new IllegalArgumentException("Não pode ter dependente sem responsavel");
        }
        this.responsavel = responsavel;
    }
}
