package com.condominio.repository.implementation;

import com.condominio.models.moradia.Veiculo;
import com.condominio.repository.interfaces.IVeiculoRepository;

import java.util.ArrayList;
import java.util.List;

public class VeiculoRepositoryImpl implements IVeiculoRepository {

    private static final List<Veiculo> veiculos = new ArrayList<>();

    @Override
    public void salvar(Veiculo veiculo) {
        veiculos.add(veiculo);
    }

    @Override
    public List<Veiculo> listar() {
        return new ArrayList<>(veiculos);
    }

    @Override
    public List<Veiculo> listarPorUnidade(int unidadeId) {
        List<Veiculo> resultado = new ArrayList<>();
        for (Veiculo v : veiculos) {
            if (v.getUnidade() != null && v.getUnidade().getId() == unidadeId) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    @Override
    public Veiculo buscarPorId(int id) {
        for (Veiculo v : veiculos) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }

    @Override
    public Veiculo buscarPorPlaca(String placa) {
        for (Veiculo v : veiculos) {
            if (v.getPlaca().equalsIgnoreCase(placa)) {
                return v;
            }
        }
        return null;
    }

    @Override
    public void atualizar(Veiculo veiculo) {
        for (int i = 0; i < veiculos.size(); i++) {
            if (veiculos.get(i).getId() == veiculo.getId()) {
                veiculos.set(i, veiculo);
                return;
            }
        }
    }

    @Override
    public void remover(int id) {
        veiculos.removeIf(v -> v.getId() == id);
    }
}
