package com.condominio.repository.interfaces;

import com.condominio.models.finance.Boleto;
import java.util.List;

public interface IBoletoRepository {

    void salvar(Boleto boleto);

    List<Boleto> listar();

    Boleto buscarPorId(int id);

    void atualizar(Boleto boleto);

    void remover(int id);
}