package com.condominio.repository.implementation;

import com.condominio.models.finance.Inadimplencia;
import com.condominio.repository.interfaces.IInadimplenciaRepository;

import java.util.ArrayList;
import java.util.List;

public class InadimplenciaRepositoryImpl
        implements IInadimplenciaRepository {

    private static final List<Inadimplencia> inadimplencias =
            new ArrayList<>();

    @Override
    public void salvar(Inadimplencia inadimplencia) {
        inadimplencias.add(inadimplencia);
    }

    @Override
    public List<Inadimplencia> listar() {
        return new ArrayList<>(inadimplencias);
    }

    @Override
    public Inadimplencia buscarPorId(int id) {

        for (Inadimplencia i : inadimplencias) {

            if (i.getId() == id) {
                return i;
            }
        }

        return null;
    }

    @Override
    public void atualizar(Inadimplencia inadimplencia) {

        for (int i = 0; i < inadimplencias.size(); i++) {

            if (inadimplencias.get(i).getId()
                    == inadimplencia.getId()) {

                inadimplencias.set(i, inadimplencia);
                return;
            }
        }
    }

    @Override
    public void remover(int id) {

        inadimplencias.removeIf(
                i -> i.getId() == id
        );
    }
}