package com.example.leilaoautopecastech.model;

public class PessoaJuridica extends Pessoa {

    public String NomeF;
    public String Telefone;
    public String Endereco;
    public String CNPJ;


    public PessoaJuridica() {

    }

    public String getNomeF() {
        return NomeF;
    }

    public void setNomeF(String nomeF) {
        NomeF = nomeF;
    }

    public String getTelefone() {
        return Telefone;
    }

    public void setTelefone(String telefone) {
        Telefone = telefone;
    }

    public String getEndereco() {
        return Endereco;
    }

    public void setEndereco(String endereco) {
        Endereco = endereco;
    }

    public String getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }
}