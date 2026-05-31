package com.condominio.repository.interfaces;

import com.condominio.models.moradia.Pet;
import java.util.List;

public interface IPetRepository {

    void salvar(Pet pet);

    List<Pet> listar();

    List<Pet> listarPorUnidade(int unidadeId);

    Pet buscarPorId(int id);

    void atualizar(Pet pet);

    void remover(int id);
}
