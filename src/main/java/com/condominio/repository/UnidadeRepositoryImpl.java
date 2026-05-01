package com.condominio.repository;

import com.condominio.model.Unidade;
import java.util.ArrayList;
import java.util.List;

public class UnidadeRepositoryImpl implements UnidadeRepository {

    private final List<Unidade> unidades = new ArrayList<>();

    @Override
    public void salvar(Unidade unidade) {
        unidades.add(unidade);
    }

    @Override
    public List<Unidade> listar() {
        return new ArrayList<>(unidades);
    }

    @Override
    public Unidade buscarPorId(int id) {
        for (Unidade u: unidades){
            if(u.getId() == id){
                return u;
            }
        }
        return null;
    }

    @Override
    public void atualizar(Unidade unidade) {
    for (int i= 0; i< unidades.size();i++) {
        if(unidades.get(i).getId() == unidade.getId()){
            unidades.set(i,unidade);
            return;
        }
    }
    }

    @Override
    public void remover(int id) {
        unidades.removeIf(u-> u.getId()==id);
    }
}
