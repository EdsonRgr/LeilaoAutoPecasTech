package com.example.leilaoautopecastech.model;

import com.example.leilaoautopecastech.config.ConfigFirebase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

public class PessoaJuridica extends Pessoa {

    private String NomeF;
    private String Telefone;
    private String Endereco;
    private String CNPJ;


    public PessoaJuridica() {

    }
    public void salvarPessoaJuridica(){
        DatabaseReference firebase = ConfigFirebase.getFirebaseDatabase();
       firebase.child("PessoaJuridica")
                .child(this.getidUsuario())
                .setValue(this);

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




