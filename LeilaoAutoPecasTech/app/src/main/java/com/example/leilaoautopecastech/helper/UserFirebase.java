package com.example.leilaoautopecastech.helper;


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

public class UserFirebase {

    public static FirebaseUser getUsuatioAtual(){
        FirebaseAuth usuario = ConfigFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
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

    public static PessoaJuridica getDadodsUsuarioLogadoPJ(){

        FirebaseUser firebaseUser = getUsuatioAtual();

        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        pessoaJuridica.setEmail( firebaseUser.getEmail());
        pessoaJuridica.setNomeF( firebaseUser.getDisplayName());
        pessoaJuridica.setidUsuario(firebaseUser.getUid());

        if(firebaseUser.getPhotoUrl() == null){
            pessoaJuridica.setIdImg("");
        }else{
            pessoaJuridica.setIdImg( firebaseUser.getPhotoUrl().toString() );
        }

        return pessoaJuridica;
    }

    public static PessoaFisica getDadodsUsuarioLogado(){

        FirebaseUser firebaseUser = getUsuatioAtual();

        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        pessoaJuridica.setEmail( firebaseUser.getEmail());
        pessoaJuridica.setNomeF( firebaseUser.getDisplayName());
        pessoaJuridica.setidUsuario(firebaseUser.getUid());

        PessoaFisica pessoaFisica = new PessoaFisica();
        pessoaFisica.setEmail( firebaseUser.getEmail());
        pessoaFisica.setNome( firebaseUser.getDisplayName());
        pessoaFisica.setidUsuario(firebaseUser.getUid());

        if(firebaseUser.getPhotoUrl() == null){
            pessoaFisica.setIdImg("");
        }else{
            pessoaFisica.setIdImg( firebaseUser.getPhotoUrl().toString() );
        }

        return pessoaFisica;
    }

/*
    public static void redirecionaUsuario(){

        DatabaseReference usuarioRef = ConfigFirebase.getFirebaseDatabase()
                .child("PessoaFisica")
                .child(getIdentificadorUsuario());

        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                PessoaFisica pessoaFisica = dataSnapshot.getValue(PessoaFisica.class);

                String tipoUsuario = pessoaFisica.getTipo();

                if(tipoUsuario.equals("pessoaFisica")){
                    hideItem();

                }else{

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static String getIdentificadorUsuario(){
        return getUsuatioAtual().getUid();

    }

*/
}
