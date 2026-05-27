package com.condominio.models.area;

import java.util.ArrayList;
import java.util.List;

public class Agenda {

    private int id;
    private List<Reserva> reservas;

    public Agenda(int id) {
        this.id = id;
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

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void setReservas(List<Reserva> reservas) {
        this.reservas = reservas;
    }
}