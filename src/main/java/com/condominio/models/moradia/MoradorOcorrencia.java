package com.condominio.models.moradia;

import com.condominio.enums.StatusMorador;

public class MoradorOcorrencia extends Morador {

    public MoradorOcorrencia(String nomeMorador) {
        super(
                (int) (System.currentTimeMillis() & 0xfffffff), // ID único válido
                nomeMorador,                                    // Nome vindo do campo de texto
                "00000000000",                                  // CPF fictício válido (11 dígitos)
                "0000-0000",                                    // Telefone padrão
                null,                                           // Unidade inicial
                "nao-informado@condominio.com",                 // E-mail padrão válido
                StatusMorador.values().length > 0 ? StatusMorador.values()[0] : null // Primeiro status do Enum
        );
    }
}