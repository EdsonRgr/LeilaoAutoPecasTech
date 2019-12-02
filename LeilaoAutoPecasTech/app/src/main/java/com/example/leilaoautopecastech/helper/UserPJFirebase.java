package com.example.leilaoautopecastech.helper;


import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.leilaoautopecastech.config.ConfigFirebase;
import com.example.leilaoautopecastech.model.PessoaFisica;
import com.example.leilaoautopecastech.model.PessoaJuridica;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserPJFirebase {

    public static FirebaseUser getUsuatioAtual() {
        FirebaseAuth usuario = ConfigFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }
    public static String getIdentificadorUsuario(){
        return getUsuatioAtual().getUid();
    }

    public static void updateNomeUserPj(String NomeF) {
        try {
            FirebaseUser userLogado = getUsuatioAtual();
            UserProfileChangeRequest perfil = new UserProfileChangeRequest
                    .Builder()
                    .setDisplayName(NomeF)
                    .build();
            userLogado.updateProfile(perfil).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.d("Perfil", "Erro ao atualizar o nome do perfil");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updatePhotoUserPJ(Uri url){
        try{
            FirebaseUser userLogado = getUsuatioAtual();

            UserProfileChangeRequest perfil = new UserProfileChangeRequest
                    .Builder()
                    .setPhotoUri( url )
                    .build();
            userLogado.updateProfile( perfil ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if ( !task.isSuccessful()){

                        Log.d("Perfil", "Erro ao atualizar a foto de perfil");

                    }
                }
            });

        }catch ( Exception e){
            e.printStackTrace();
        }

    }


        public static PessoaJuridica getDadodsUsuarioLogadoPJ(){

            FirebaseUser firebaseUser = getUsuatioAtual();

            PessoaJuridica pessoaJuridica = new PessoaJuridica();
            pessoaJuridica.setEmail(firebaseUser.getEmail());
            pessoaJuridica.setNomeF(firebaseUser.getDisplayName());
            pessoaJuridica.setidUsuario(firebaseUser.getUid());
            pessoaJuridica.setTelefone(firebaseUser.getPhoneNumber());
            pessoaJuridica.setEndereco(firebaseUser.zzd());
            pessoaJuridica.setCNPJ(firebaseUser.zzd());
            pessoaJuridica.setTipo("pessoaJuridica");

            if (firebaseUser.getPhotoUrl() == null) {
                pessoaJuridica.setIdImg("");
            } else {
                pessoaJuridica.setIdImg(firebaseUser.getPhotoUrl().toString());
            }

            return pessoaJuridica;
        }

    }
