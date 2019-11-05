package com.example.leilaoautopecastech.model;



public class Pessoa {

    private int Uid;
    private int idImg;
    private String email;
    private String senha;


    public Pessoa () {

    }

    public int getidImg() {
        return idImg;
    }

    public void setidImg(int idImg) {
        this.idImg = idImg;
    }

    public int getUid() {
        return Uid;
    }

    public void setUid(int Uid) {
        this.Uid = Uid;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
