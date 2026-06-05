package com.condominio.repository.implementation;

import com.condominio.enums.StatusUnidade;
import com.condominio.enums.TipoUnidade;
import com.condominio.models.moradia.Unidade;
import com.condominio.repository.interfaces.IUnidadeRepository;

import java.util.ArrayList;
import java.util.List;

public class UnidadeRepositoryImpl implements IUnidadeRepository {

    private static final List<Unidade> unidades = new ArrayList<>();

    public UnidadeRepositoryImpl() {

        if (unidades.isEmpty()) {
            popularDadosTeste();
        }
    }
    private void popularDadosTeste() {

        salvar(new Unidade(1, "A", StatusUnidade.OCUPADA, 2.5, 101, TipoUnidade.APARTAMENTO, "65m²", 4, "ADIMPLENTE"));
        salvar(new Unidade(2, "A", StatusUnidade.OCUPADA, 2.5, 102, TipoUnidade.APARTAMENTO, "70m²", 4, "ADIMPLENTE"));
        salvar(new Unidade(3, "A", StatusUnidade.OCUPADA, 2.5, 103, TipoUnidade.APARTAMENTO, "75m²", 5, "ADIMPLENTE"));
        salvar(new Unidade(4, "A", StatusUnidade.OCUPADA, 2.5, 104, TipoUnidade.APARTAMENTO, "80m²", 5, "INADIMPLENTE"));
        salvar(new Unidade(5, "A", StatusUnidade.OCUPADA, 2.5, 105, TipoUnidade.APARTAMENTO, "90m²", 6, "ADIMPLENTE"));

        salvar(new Unidade(6, "A", StatusUnidade.OCUPADA, 2.5, 201, TipoUnidade.APARTAMENTO, "65m²", 4, "ADIMPLENTE"));
        salvar(new Unidade(7, "A", StatusUnidade.OCUPADA, 2.5, 202, TipoUnidade.APARTAMENTO, "70m²", 4, "ADIMPLENTE"));
        salvar(new Unidade(8, "A", StatusUnidade.OCUPADA, 2.5, 203, TipoUnidade.APARTAMENTO, "75m²", 5, "INADIMPLENTE"));
        salvar(new Unidade(9, "A", StatusUnidade.OCUPADA, 2.5, 204, TipoUnidade.APARTAMENTO, "80m²", 5, "ADIMPLENTE"));
        salvar(new Unidade(10, "A", StatusUnidade.OCUPADA, 2.5, 205, TipoUnidade.APARTAMENTO, "90m²", 6, "ADIMPLENTE"));

        salvar(new Unidade(11, "B", StatusUnidade.OCUPADA, 2.5, 101, TipoUnidade.APARTAMENTO, "65m²", 4, "ADIMPLENTE"));
        salvar(new Unidade(12, "B", StatusUnidade.OCUPADA, 2.5, 102, TipoUnidade.APARTAMENTO, "70m²", 4, "ADIMPLENTE"));
        salvar(new Unidade(13, "B", StatusUnidade.DISPONIVEL, 2.5, 103, TipoUnidade.APARTAMENTO, "75m²", 5, "ADIMPLENTE"));
        salvar(new Unidade(14, "B", StatusUnidade.OCUPADA, 2.5, 104, TipoUnidade.APARTAMENTO, "80m²", 5, "INADIMPLENTE"));
        salvar(new Unidade(15, "B", StatusUnidade.OCUPADA, 2.5, 105, TipoUnidade.APARTAMENTO, "90m²", 6, "ADIMPLENTE"));

        salvar(new Unidade(16, "B", StatusUnidade.OCUPADA, 2.5, 201, TipoUnidade.APARTAMENTO, "65m²", 4, "ADIMPLENTE"));
        salvar(new Unidade(17, "B", StatusUnidade.DISPONIVEL, 2.5, 202, TipoUnidade.APARTAMENTO, "70m²", 4, "ADIMPLENTE"));
        salvar(new Unidade(18, "B", StatusUnidade.OCUPADA, 2.5, 203, TipoUnidade.APARTAMENTO, "75m²", 5, "INADIMPLENTE"));
        salvar(new Unidade(19, "B", StatusUnidade.OCUPADA, 2.5, 204, TipoUnidade.APARTAMENTO, "80m²", 5, "ADIMPLENTE"));
        salvar(new Unidade(20, "B", StatusUnidade.DISPONIVEL, 2.5, 205, TipoUnidade.APARTAMENTO, "90m²", 6, "ADIMPLENTE"));
    }

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
        for (Unidade u : unidades) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    @Override
    public void atualizar(Unidade unidade) {
        for (int i = 0; i < unidades.size(); i++) {
            if (unidades.get(i).getId() == unidade.getId()) {
                unidades.set(i, unidade);
                return;
            }
        }
    }

    @Override
    public void remover(int id) {
        unidades.removeIf(u -> u.getId() == id);
    }
}