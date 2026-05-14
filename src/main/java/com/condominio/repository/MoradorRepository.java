package com.condominio.repository;

import com.condominio.models.moradia.Morador;
import java.util.List;

public interface MoradorRepository {

    void salvar(Morador morador);

    List<Morador> listar();

    Morador buscarPorId(int id);

    Morador buscarPorCpf(String cpf);

    void atualizar(Morador morador);

    void remover(int id);
}