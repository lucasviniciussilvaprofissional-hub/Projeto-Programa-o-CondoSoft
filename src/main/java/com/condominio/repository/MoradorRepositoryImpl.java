package com.condominio.repository;

import java.util.ArrayList;
import java.util.List;
import com.condominio.model.Morador;

public class MoradorRepositoryImpl implements MoradorRepository {
    private List<Morador> moradores = new ArrayList<>();

    @Override
    public void salvar(Morador morador) {
        moradores.add(morador);
    }

    @Override
    public List<Morador> listar() {
        return moradores;
    }

    @Override
    public Morador buscarPorId(int id) {
        for (Morador m : moradores) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    @Override
    public Morador buscaporCpf(String Cpf) {
        return null;
    }

    @Override
    public Morador buscarPorCpf(String cpf) {
        for (Morador m : moradores) {
            if (m.getCpf().equals(cpf)) {
                return m;
            }
        }
        return null;
    }

    @Override
    public void TUlizR(Morador morador) {

    }

    @Override
    public void atualizar(Morador morador){
        for(int i = 0; i< moradores.size();i++){
            if (moradores.get(i).getId() == morador.getId()){
                morador.set(i,morador);
                return;
            }
        }
    }
    @Override
    public void remover(int id){
        moradores.removeIf(m-> m.getId()==id);
    }
}

