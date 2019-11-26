package com.example.leilaoautopecastech.model;

import com.example.leilaoautopecastech.config.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Anuncio implements Serializable {
    private String key;
    private String idAnuncio;
    private String marcas;
    private String modelos;
    private String categorias;
    private String pecas;
    private String titulo;
    private String valor;
    private String telefone;
    private String fone;
    private String descricao;
    private List<String> fotos = new ArrayList<>();


    //construtor
    public Anuncio(){
        DatabaseReference anuncioRef = ConfigFirebase.getFirebaseDatabase()
                .child("meus_anuncios");

        setIdAnuncio( anuncioRef.push().getKey() );

    }

    public void salvar (){
        //alterar dps
        String idUsuario = ConfigFirebase.getIdUsuario();
        //
        DatabaseReference anuncioRef = ConfigFirebase.getFirebaseDatabase()
                .child("meus_anuncios");

        anuncioRef.child(idUsuario)
                .child(getIdAnuncio())
                .setValue(this);


        salvarAnuncioPublico();

    }

    public void salvarAnuncioPublico (){

        DatabaseReference anuncioRef = ConfigFirebase.getFirebaseDatabase()
                .child("anuncios");

        //alterar dps
        anuncioRef.child( getMarcas() )
                .child(getModelos())
                .child(getCategorias())
                .child(getPecas())
                .child(getIdAnuncio())
                .setValue(this);
    }


    public void excluirAnuncio() {
        String idUsuario = ConfigFirebase.getIdUsuario();
        //
        DatabaseReference anuncioRef = ConfigFirebase.getFirebaseDatabase()
                .child("meus_anuncios")
                .child(idUsuario)
                .child(getIdAnuncio());
        anuncioRef.removeValue();
        excluirAnuncioPublicos();

    }

    public void excluirAnuncioPublicos() {

        DatabaseReference anuncioRef = ConfigFirebase.getFirebaseDatabase()
                .child("anuncios")
                .child( getMarcas() )
                .child(getModelos())
                .child(getCategorias())
                .child(getPecas())
                .child(getIdAnuncio());
        anuncioRef.removeValue();


    }






    @Exclude
    public String getFone() {
        return fone;
    }
    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getIdAnuncio() {
        return idAnuncio;
    }

    public void setIdAnuncio(String idAnuncio) {
        this.idAnuncio = idAnuncio;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }

    public String getMarcas() {
        return marcas;
    }

    public void setMarcas(String marcas) {
        this.marcas = marcas;
    }

    public String getModelos() {
        return modelos;
    }

    public void setModelos(String modelos) {
        this.modelos = modelos;
    }

    public String getCategorias() {
        return categorias;
    }

    public void setCategorias(String categorias) {
        this.categorias = categorias;
    }

    public String getPecas() {
        return pecas;
    }

    public void setPecas(String pecas) {
        this.pecas = pecas;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


}
