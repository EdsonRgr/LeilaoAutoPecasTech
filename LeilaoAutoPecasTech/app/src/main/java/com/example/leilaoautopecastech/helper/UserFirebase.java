package com.example.leilaoautopecastech.helper;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.leilaoautopecastech.config.ConfigFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseTooManyRequestsException;
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

}