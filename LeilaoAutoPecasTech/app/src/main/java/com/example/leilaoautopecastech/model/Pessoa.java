package com.example.leilaoautopecastech.model;


import com.google.firebase.database.Exclude;

public class Pessoa {

    public String idUsuario;
    private String idImg;
    private String email;
    private String senha;


    public Pessoa () {

    }

    @Exclude
    public String getIdImg() {
        return idImg;
    }

    public void setIdImg(String idImg) {
        this.idImg = idImg;
    }

    @Exclude
    public String getidUsuario() {
        return idUsuario;
    }
    public void setidUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

}
