package com.example.leilaoautopecastech.model;

import com.example.leilaoautopecastech.config.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;


public class PessoaFisica extends Pessoa {

    private String nome;


    public PessoaFisica () {

    }
    public void salvarPessoaFisica(){
        DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference usuariosRef = firebaseRef.child("usuarios").child(getidUsuario());

        usuariosRef.setValue(this);

    }

    public void atualizarPerfilPF(){
        DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference usuariosRef = firebaseRef.child("usuarios").child(getidUsuario());


        Map<String, Object> valoresUsuario = converterParaMap();
        usuariosRef.updateChildren( valoresUsuario );

    }

    public Map<String, Object> converterParaMap(){

        HashMap<String,Object> usuarioMap = new HashMap<>();
        usuarioMap.put("email" , getEmail());
        usuarioMap.put("nome" , getNome());
        usuarioMap.put("idUsuario" , getidUsuario());
        usuarioMap.put("idImg" , getIdImg());
        usuarioMap.put("tipo" , getTipo());
        return usuarioMap;

    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


}
