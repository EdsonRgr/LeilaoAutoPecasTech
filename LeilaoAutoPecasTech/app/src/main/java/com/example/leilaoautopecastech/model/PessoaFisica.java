package com.example.leilaoautopecastech.model;

import com.example.leilaoautopecastech.config.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;


public class PessoaFisica extends Pessoa {

    private String nome;


    public PessoaFisica () {

    }
    public void salvarPessoaFisica(){
        DatabaseReference firebase = ConfigFirebase.getFirebaseDatabase();
        firebase.child("PessoaFisica")
                .child(this.getidUsuario())
                .setValue( this );

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


}
