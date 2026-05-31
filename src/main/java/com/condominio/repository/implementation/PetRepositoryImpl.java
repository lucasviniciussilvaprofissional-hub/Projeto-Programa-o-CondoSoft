package com.condominio.repository.implementation;

import com.condominio.models.moradia.Pet;
import com.condominio.repository.interfaces.IPetRepository;

import java.util.ArrayList;
import java.util.List;

public class PetRepositoryImpl implements IPetRepository {

    private static final List<Pet> pets = new ArrayList<>();

    @Override
    public void salvar(Pet pet) {
        pets.add(pet);
    }

    @Override
    public List<Pet> listar() {
        return new ArrayList<>(pets);
    }

    @Override
    public List<Pet> listarPorUnidade(int unidadeId) {
        List<Pet> resultado = new ArrayList<>();
        for (Pet p : pets) {
            if (p.getUnidade() != null && p.getUnidade().getId() == unidadeId) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    @Override
    public Pet buscarPorId(int id) {
        for (Pet p : pets) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public void atualizar(Pet pet) {
        for (int i = 0; i < pets.size(); i++) {
            if (pets.get(i).getId() == pet.getId()) {
                pets.set(i, pet);
                return;
            }
        }
    }

    @Override
    public void remover(int id) {
        pets.removeIf(p -> p.getId() == id);
    }
}
