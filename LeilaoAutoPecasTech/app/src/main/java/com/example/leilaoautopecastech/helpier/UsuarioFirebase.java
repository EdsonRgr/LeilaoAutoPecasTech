package com.example.leilaoautopecastech.helpier;

import com.example.leilaoautopecastech.config.configFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class UsuarioFirebase {
    public static String getIdentificadorUsuario(){

        FirebaseAuth usuario = configFirebase.getFirebaseAutenticacao();
        String email = usuario.getCurrentUser().getEmail();
        String identificadorUsuario = Base64Custom.codificarBase64( email );

        return identificadorUsuario;

    }
}
