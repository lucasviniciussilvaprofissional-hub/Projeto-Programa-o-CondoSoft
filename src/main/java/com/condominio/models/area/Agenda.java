package com.condominio.models.area;

import java.util.ArrayList;
import java.util.List;

public class Agenda {

    private int id;
    private AreaComum areaComum;
    private List<Reserva> reservas;

    public Agenda(int id, AreaComum areaComum) {
        this.id = id;
        this.areaComum = areaComum;
        this.reservas = new ArrayList<>();
    }

    public void adicionarReserva(Reserva reserva) {
        reservas.add(reserva);
    }

    public void removerReserva(Reserva reserva) {
        reservas.remove(reserva);
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AreaComum getAreaComum() {
        return areaComum;
    }

    public void setAreaComum(AreaComum areaComum) {
        this.areaComum = areaComum;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
}