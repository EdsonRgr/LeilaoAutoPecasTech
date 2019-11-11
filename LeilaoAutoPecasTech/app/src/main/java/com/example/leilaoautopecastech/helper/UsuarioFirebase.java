package com.example.leilaoautopecastech.helper;

import com.example.leilaoautopecastech.config.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsuarioFirebase {
    public static String getIdentificadorUsuario(){

        FirebaseAuth usuario = ConfigFirebase.getFirebaseAutenticacao();
        String email = usuario.getCurrentUser().getEmail();
        String identificadorUsuario = Base64Custom.codificarBase64( email );

        return identificadorUsuario;
    }

    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfigFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();

    }



}
