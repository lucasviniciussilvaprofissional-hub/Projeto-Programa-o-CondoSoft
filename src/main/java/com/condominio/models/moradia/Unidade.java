package com.condominio.models.moradia;

import com.condominio.enums.StatusUnidade;
import com.condominio.enums.TipoUnidade;

import java.util.ArrayList;
import java.util.List;

public class Unidade {

    private int id;
    private int numero;
    private String bloco;
    private TipoUnidade tipo;
    private StatusUnidade status;
    private String metragem;
    private double fracaoIdeal;
    private String situacaoFinanceira;
    private int capacidade;

    private List<Morador> moradores;
    private List<Veiculo> veiculos;
    private List<Pet> pets;

    public Unidade(int id,
                   String bloco,
                   StatusUnidade status,
                   double fracaoIdeal,
                   int numero,
                   TipoUnidade tipo,
                   String metragem,
                   int capacidade,
                   String situacaoFinanceira) {

        setId(id);
        setNumero(numero);
        setTipo(tipo);
        setMetragem(metragem);
        setBloco(bloco);
        setStatus(status);
        setFracaoIdeal(fracaoIdeal);
        setCapacidade(capacidade);
        setSituacaoFinanceira(situacaoFinanceira);

        this.moradores = new ArrayList<>();
        this.veiculos = new ArrayList<>();
        this.pets = new ArrayList<>();
    }

    // =========================================================================
    // CONSTRUTOR ALTERNATIVO (Para uso simplificado na tela de Ocorrências)
    // =========================================================================
    public Unidade(String textoTela) {
        this.id = (int) (System.currentTimeMillis() & 0xfffffff);
        this.bloco = "Bloco Geral";

        // Pega o primeiro status disponível no Enum
        this.status = StatusUnidade.values().length > 0 ? StatusUnidade.values()[0] : null;
        this.fracaoIdeal = 1.0;

        // Filtra apenas números do texto digitado ou selecionado
        int numeroExtraido = 1;
        try {
            String apenasNumeros = textoTela.replaceAll("[^0-9]", "");
            if (!apenasNumeros.isEmpty()) {
                numeroExtraido = Integer.parseInt(apenasNumeros);
            }
            if (numeroExtraido <= 0) numeroExtraido = 1;
        } catch (Exception e) {
            numeroExtraido = 1;
        }
        this.numero = numeroExtraido;

        // Pega o primeiro tipo disponível no Enum
        this.tipo = TipoUnidade.values().length > 0 ? TipoUnidade.values()[0] : null;
        this.metragem = "Padrão";
        this.capacidade = 4;
        this.situacaoFinanceira = "Regular";

        this.moradores = new ArrayList<>();
        this.veiculos = new ArrayList<>();
        this.pets = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Nº " + this.numero + " (" + this.bloco + ")";
    }

    // =========================================================================
    // SETTERS
    // =========================================================================
    public void setCapacidade(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("Capacidade inválida.");
        this.capacidade = capacity;
    }

    public void setSituacaoFinanceira(String situacaoFinanceira) {
        if (situacaoFinanceira == null || situacaoFinanceira.isEmpty()) throw new IllegalArgumentException("Situação financeira inválida.");
        this.situacaoFinanceira = situacaoFinanceira;
    }

    public void setTipo(TipoUnidade tipo) {
        if (tipo == null) throw new IllegalArgumentException("Tipo inválido.");
        this.tipo = tipo;
    }

    public void setNumero(int numero) {
        if (numero <= 0) throw new IllegalArgumentException("Número inválido.");
        this.numero = numero;
    }

    public void setMetragem(String metragem) {
        if (metragem == null || metragem.isEmpty()) throw new IllegalArgumentException("Metragem inválida.");
        this.metragem = metragem;
    }

    public void setId(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID inválido.");
        this.id = id;
    }

    public void setBloco(String bloco) {
        if (bloco == null || bloco.isEmpty()) throw new IllegalArgumentException("Bloco inválido.");
        this.bloco = bloco;
    }

    public void setStatus(StatusUnidade status) {
        if (status == null) throw new IllegalArgumentException("Status inválido.");
        this.status = status;
    }

    public void setFracaoIdeal(double fracaoIdeal) {
        if (fracaoIdeal <= 0) throw new IllegalArgumentException("Fração ideal inválida.");
        this.fracaoIdeal = fracaoIdeal;
    }

    public void setMoradores(List<Morador> moradores) { this.moradores = moradores; }
    public void setPets(List<Pet> pets) { this.pets = pets; }
    public void setVeiculos(List<Veiculo> veiculos) { this.veiculos = veiculos; }

    // =========================================================================
    // GETTERS
    // =========================================================================
    public int getCapacidade() { return capacidade; }
    public String getSituacaoFinanceira() { return situacaoFinanceira; }
    public String getMetragem() { return metragem; }
    public TipoUnidade getTipo() { return tipo; }
    public int getNumero() { return numero; }
    public int getId() { return id; }
    public String getBloco() { return bloco; }
    public StatusUnidade getStatus() { return status; }
    public double getFracaoIdeal() { return fracaoIdeal; }
    public List<Morador> getMoradores() { return moradores; }
    public List<Pet> getPets() { return pets; }
    public List<Veiculo> getVeiculos() { return veiculos; }

    // =========================================================================
    // REGRAS DE NEGÓCIO
    // =========================================================================
    public void adicionarMorador(Morador morador) {
        if (morador == null) throw new IllegalArgumentException("Morador inválido.");
        if (moradores.size() >= capacidade) throw new IllegalArgumentException("Capacidade máxima da unidade atingida.");
        if (!moradores.contains(morador)) {
            moradores.add(morador);
            morador.setUnidade(this);
        }
    }


    public void removerMorador(Morador morador) {
        if (morador == null) throw new IllegalArgumentException("Morador inválido.");
        if (!moradores.contains(morador)) throw new IllegalArgumentException("Morador não encontrado.");
        moradores.remove(morador);
    }

    public void adicionarVeiculo(Veiculo veiculo) {
        if (veiculo == null) throw new IllegalArgumentException("Veículo inválido.");
        if (veiculos.size() >= 2) throw new IllegalArgumentException("Limite de veículos atingido.");
        if (!veiculos.contains(veiculo)) {
            veiculos.add(veiculo);
            veiculo.setUnidade(this);
        }
    }

    public void removerVeiculo(Veiculo veiculo) {
        if (veiculo == null) throw new IllegalArgumentException("Veículo inválido.");
        if (!veiculos.contains(veiculo)) throw new IllegalArgumentException("Veículo não encontrado.");
        veiculos.remove(veiculo);
    }

    public void adicionarPet(Pet pet) {
        if (pet == null) throw new IllegalArgumentException("Pet inválido.");
        if (pets.size() >= 3) throw new IllegalArgumentException("Limite de pets atingido.");
        if (!pets.contains(pet)) {
            pets.add(pet);
            pet.setUnidade(this);
        }
    }

    public void removerPet(Pet pet) {
        if (pet == null) throw new IllegalArgumentException("Pet inválido.");
        if (!pets.contains(pet)) throw new IllegalArgumentException("Pet não encontrado.");
        pets.remove(pet);
    }

    public boolean unidadeOcupada() { return !moradores.isEmpty(); }

    public void liberarUnidade() {
        if (!moradores.isEmpty()) throw new IllegalArgumentException("Ainda existem moradores na unidade.");
        status = StatusUnidade.values().length > 1 ? StatusUnidade.values()[1] : status;
    }

    public int quantidadeMoradores() { return moradores.size(); }
    public int quantidadeVeiculos() { return veiculos.size(); }
    public int quantidadePets() { return pets.size(); }
}