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
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserPFFirebase {

    public static FirebaseUser getUsuatioAtual(){

        FirebaseAuth usuario = ConfigFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();

    }
    public static String getIdentificadorUsuario(){
        return getUsuatioAtual().getUid();
    }

    public static void updateNomeUser(String nome){
        try{
            FirebaseUser userLogado = getUsuatioAtual();

            UserProfileChangeRequest perfil = new UserProfileChangeRequest
                    .Builder()
                    .setDisplayName( nome )
                    .build();
            userLogado.updateProfile( perfil ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if ( !task.isSuccessful()){

                        Log.d("Perfil", "Erro ao atualizar o nome do perfil");

                    }
                }
            });

        }catch ( Exception e){
            e.printStackTrace();
        }

    }

    public static void updatePhotoUser(Uri url){
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



    public static PessoaFisica getDadodsUsuarioLogadoPF(){

        FirebaseUser firebaseUser = getUsuatioAtual();

        PessoaFisica pessoaFisica = new PessoaFisica();
        pessoaFisica.setEmail( firebaseUser.getEmail());
        pessoaFisica.setNome( firebaseUser.getDisplayName());
        pessoaFisica.setidUsuario(firebaseUser.getUid());
        pessoaFisica.setTipo("pessoaFisica");

        if(firebaseUser.getPhotoUrl() == null){
            pessoaFisica.setIdImg("");
        }else{
            pessoaFisica.setIdImg( firebaseUser.getPhotoUrl().toString() );
        }

        return pessoaFisica;
    }


}
