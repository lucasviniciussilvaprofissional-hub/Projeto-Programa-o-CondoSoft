package com.condominio.repository.interfaces;

import com.condominio.models.area.Reserva;
import com.condominio.models.finance.Despesa;

import java.util.List;

public interface IReservaRepository {

    void salvar(Reserva reserva);

    List<Reserva> listar();

    Reserva buscarPorId(int id);

    void atualizar(Reserva reserva);

    void remover(int id);

    interface IDespesaRepository {

        void salvar(Despesa despesa);

        List<Despesa> listar();

        Despesa buscarPorId(int id);

        List<Despesa> buscarPorStatus(String status);

        List<Despesa> buscarPorTipo(String tipo);

        void atualizar(Despesa despesa);

        void remover(int id);
    }
}
