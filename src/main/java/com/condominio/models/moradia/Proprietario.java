package com.condominio.models.moradia;
import com.condominio.enums.StatusMorador;

public class Proprietario extends Morador
{
    public Proprietario(int id, String nome, String cpf, String telefone, Unidade unidade, String email, StatusMorador status)
    {
        super(id,nome,cpf,telefone,unidade, email, status);
    }
}
