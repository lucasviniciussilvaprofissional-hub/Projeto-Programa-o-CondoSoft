package com.condominio.models.moradia;

import com.condominio.enums.StatusMorador;

public class Morador {

    protected int id;
    protected String nome;
    protected String cpf;
    protected String telefone;
    protected Unidade unidade;
    protected String email;
    protected StatusMorador status;
    protected String descricao;

    // =========================
    // CONSTRUTOR
    // =========================

    public Morador(int id,
                   String nome,
                   String cpf,
                   String telefone,
                   Unidade unidade,
                   String email,
                   StatusMorador status) {

        setId(id);
        setNome(nome);
        setCpf(cpf);
        setTelefone(telefone);
        setUnidade(unidade);
        setEmail(email);
        setStatus(status);

    }

    // =========================
    // SETTERS
    // =========================


    public void setEmail(String email) {

        if (email == null || email.isEmpty()) {

            throw new IllegalArgumentException("Email inválido.");

        }

        this.email = email;

    }

    public void setUnidade(Unidade unidade) {

        if (unidade == null) {

            throw new IllegalArgumentException("Unidade inválida.");

        }

        this.unidade = unidade;

    }

    public void setNome(String nome) {

        if (nome == null || nome.isEmpty()) {

            throw new IllegalArgumentException("Nome inválido.");

        }

        this.nome = nome;

    }

    public void setId(int id) {

        if (id <= 0) {

            throw new IllegalArgumentException("ID inválido.");

        }

        this.id = id;

    }

    public void setCpf(String cpf) {

        if (cpf == null || cpf.length() != 11) {

            throw new IllegalArgumentException("CPF inválido.");

        }

        this.cpf = cpf;

    }

    public void setTelefone(String telefone) {

        if (telefone == null || telefone.isEmpty()) {

            throw new IllegalArgumentException("Telefone inválido.");

        }

        this.telefone = telefone;

    }

    public void setStatus(StatusMorador status) {

        if (status == null) {

            throw new IllegalArgumentException("Status inválido.");

        }

        this.status = status;

    }

    // =========================
    // GETTERS
    // =========================


    public String getEmail() {
        return email;
    }

    public Unidade getUnidade() {
        return unidade;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public StatusMorador getStatus() {
        return status;
    }

    // =========================
    // REGRAS DE NEGÓCIO
    // =========================

    public boolean moradorAtivo() {

        return status == StatusMorador.ATIVO;

    }

    public void bloquearMorador() {

        status = StatusMorador.BLOQUEADO;

    }

    public void ativarMorador() {

        status = StatusMorador.ATIVO;

    }

    public void removerUnidade() {

        this.unidade = null;

    }

}