package com.condominio.models.area;

public class TaxaLimpeza
{
    private int id;
    private double valor;
    private Reserva reserva;
    private boolean paga;

    public TaxaLimpeza(int id, double valor, Reserva reserva, boolean paga)
    {
        this.id = id;
        this.valor = valor;
        this.reserva = reserva;
        this.paga = paga;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPaga(boolean paga) {
        this.paga = paga;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public double getValor() {
        return valor;
    }

    public Reserva getReserva() {
        return reserva;
    }
}
