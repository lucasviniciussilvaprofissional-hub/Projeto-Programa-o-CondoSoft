package com.condominio.repository.interfaces;

import com.condominio.models.area.Reserva;

import java.util.List;

public interface IReservaRepository {

    void salvar(Reserva reserva);

    List<Reserva> listar();

    Reserva buscarPorId(int id);

    void atualizar(Reserva reserva);

    void remover(int id);

}
