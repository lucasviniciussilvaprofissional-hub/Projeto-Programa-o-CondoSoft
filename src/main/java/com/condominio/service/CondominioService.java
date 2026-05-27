package com.condominio.service;

import com.condominio.enums.StatusBoleto;
import com.condominio.models.area.Reserva;
import com.condominio.models.finance.Boleto;
import com.condominio.models.moradia.Morador;
import com.condominio.models.moradia.Unidade;


import java.time.LocalDate;
import java.util.List;

public class CondominioService {

    private List<Unidade> unidades;
    private List<Morador> moradores;
    private List<Reserva> reservas;
    private List<Boleto> boletos;

    public CondominioService(List<Unidade> unidades,
                             List<Morador> moradores,
                             List<Reserva> reservas,
                             List<Boleto> boletos) {

        this.unidades = unidades;
        this.moradores = moradores;
        this.reservas = reservas;
        this.boletos = boletos;
    }


    public void adicionarUnidade(Unidade unidade) {

        unidades.add(unidade);
    }


    public void adicionarMorador(Morador morador,
                                 Unidade unidade) {

        moradores.add(morador);

        unidade.adicionarMorador(morador);
    }


    public void realizarReserva(Reserva reserva) {

        reservas.add(reserva);
    }



    public void registrarPagamento(Boleto boleto) {

        boleto.setStatus(StatusBoleto.valueOf("PAGO"));
    }

    public Boleto gerarBoleto(Unidade unidade) {

        if (unidade == null) {
            throw new IllegalArgumentException("Unidade não pode ser nula");
        }

        int id = unidade.getId();

        String codigoBarras = "BOL" + unidade.getId();

        float valor = 350.0f;

        String competencia = LocalDate.now().getYear() + "-" +
                String.format("%02d", LocalDate.now().getMonthValue());

        LocalDate vencimento = LocalDate.now().plusDays(10);

        return new Boleto(
                id,
                codigoBarras,
                valor,
                competencia,
                vencimento,
                StatusBoleto.PENDENTE
        );
    }


    public List<Unidade> listarUnidades() {

        return unidades;
    }


    public List<Morador> listarMoradores() {

        return moradores;
    }


    public List<Reserva> listarReservas() {

        return reservas;
    }


    public List<Boleto> listarBoletos() {

        return boletos;
    }

}