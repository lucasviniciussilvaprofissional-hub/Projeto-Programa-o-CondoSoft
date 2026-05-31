package com.condominio.repository.interfaces;

import com.condominio.models.moradia.Veiculo;
import java.util.List;

public interface IVeiculoRepository {

    void salvar(Veiculo veiculo);

    List<Veiculo> listar();

    List<Veiculo> listarPorUnidade(int unidadeId);

    Veiculo buscarPorId(int id);

    Veiculo buscarPorPlaca(String placa);

    void atualizar(Veiculo veiculo);

    void remover(int id);
}
