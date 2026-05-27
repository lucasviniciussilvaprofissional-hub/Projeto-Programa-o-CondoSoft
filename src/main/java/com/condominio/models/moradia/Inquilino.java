package com.condominio.models.moradia;
import  com.condominio.enums.StatusMorador;

import com.condominio.enums.StatusContrato;

import java.time.LocalDateTime;

public class Inquilino extends Morador
{
    private LocalDateTime dataInicioContrato;
    private StatusContrato contrato;

    public Inquilino(int id, String nome, String cpf, String telefone, Unidade unidade, String email, LocalDateTime dataInicioContrato, StatusMorador status)
    {
        super(id,nome,cpf,telefone,unidade,email, status);

        this.dataInicioContrato = dataInicioContrato;
    }

}
