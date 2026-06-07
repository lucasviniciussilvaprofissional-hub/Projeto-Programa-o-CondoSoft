package com.condominio.repository.implementation;

import com.condominio.enums.StatusReserva;
import com.condominio.models.area.Reserva;
import com.condominio.repository.interfaces.IReservaRepository;

import java.util.ArrayList;
import java.util.List;

public class ReservaRepositoryImpl implements IReservaRepository {

    // static mantém dados vivos entre trocas de tela
    private static final List<Reserva> reservas = new ArrayList<>();

    @Override public void salvar(Reserva r)     { reservas.add(r); }
    @Override public List<Reserva> listar()     { return new ArrayList<>(reservas); }
    @Override public void remover(int id)       { reservas.removeIf(r -> r.getId() == id); }

    @Override
    public Reserva buscarPorId(int id) {
        for (Reserva r : reservas) if (r.getId() == id) return r;
        return null;
    }

    @Override
    public void atualizar(Reserva reserva) {
        for (int i = 0; i < reservas.size(); i++)
            if (reservas.get(i).getId() == reserva.getId()) { reservas.set(i, reserva); return; }
    }

    @Override
    public List<Reserva> listarPorUnidade(int unidadeId) {
        List<Reserva> res = new ArrayList<>();
        for (Reserva r : reservas)
            if (r.getUnidade() != null && r.getUnidade().getId() == unidadeId) res.add(r);
        return res;
    }

    @Override
    public List<Reserva> listarPorArea(String nomeArea) {
        List<Reserva> res = new ArrayList<>();
        for (Reserva r : reservas)
            if (r.getArea() != null && r.getArea().getNome().equalsIgnoreCase(nomeArea)) res.add(r);
        return res;
    }

    @Override
    public List<Reserva> listarPorStatus(StatusReserva status) {
        List<Reserva> res = new ArrayList<>();
        for (Reserva r : reservas) if (r.getStatus() == status) res.add(r);
        return res;
    }

    @Override
    public List<Reserva> listarPorMorador(int moradorId) {
        List<Reserva> res = new ArrayList<>();
        for (Reserva r : reservas)
            if (r.getResponsavel() != null && r.getResponsavel().getId() == moradorId) res.add(r);
        return res;
    }
}
