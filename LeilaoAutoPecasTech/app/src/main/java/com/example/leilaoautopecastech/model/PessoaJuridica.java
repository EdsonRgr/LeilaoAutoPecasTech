package com.example.leilaoautopecastech.model;

import com.example.leilaoautopecastech.config.ConfigFirebase;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class PessoaJuridica extends Pessoa {

    private String NomeF;
    private String Telefone;
    private String Endereco;
    private String CNPJ;


    public PessoaJuridica() {

    }
    public void salvarPessoaJuridica(){
        DatabaseReference firebase = ConfigFirebase.getFirebaseDatabase();
       firebase.child("usuarios")
                .child(this.getidUsuario())
                .setValue(this);

    }

    public void atualizarPerfilPJ(){
        DatabaseReference firebaseRef = ConfigFirebase.getFirebaseDatabase();
        DatabaseReference usuariosRef = firebaseRef.child("usuarios").child(getidUsuario());

        Map<String, Object> valoresUsuario = converterParaMap();
        usuariosRef.updateChildren( valoresUsuario );

    }

    public Map<String, Object> converterParaMap(){

        HashMap<String,Object> usuarioMap = new HashMap<>();
        usuarioMap.put("email" , getEmail());
        usuarioMap.put("nome" , getNomeF());
        usuarioMap.put("idUsuario" , getidUsuario());
        usuarioMap.put("idImg" , getIdImg());
        usuarioMap.put("tipo" , getTipo());
        usuarioMap.put("cnpj", getCNPJ());
        usuarioMap.put("endereco", getEndereco());
        usuarioMap.put("telefone", getTelefone());
        return usuarioMap;

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




