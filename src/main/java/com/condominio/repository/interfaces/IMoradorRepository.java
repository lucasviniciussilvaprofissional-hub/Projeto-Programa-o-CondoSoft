package com.condominio.repository.interfaces;

import com.condominio.models.moradia.Morador;
import java.util.List;

public interface IMoradorRepository {

    void salvar(Morador morador);

    List<Morador> listar();

    Morador buscarPorId(int id);

    Morador buscarPorCpf(String cpf);

    void atualizar(Morador morador);

    void remover(int id);
}