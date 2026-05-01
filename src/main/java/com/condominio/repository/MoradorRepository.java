package com.condominio.repository;

import com.condominio.model.Morador;
import java.util.List;

public interface MoradorRepository {

 void salvar(Morador morador);
 List<Morador> listar();

 Morador buscarPorId(int id);
 Morador buscaporCpf(String Cpf);

 void atualizar(Morador morador);
 void remover(int id);

    Morador buscarPorCpf(String cpf);

    void TUlizR(Morador morador);
}
