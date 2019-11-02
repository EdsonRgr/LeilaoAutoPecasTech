package com.example.leilaoautopecastech.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leilaoautopecastech.MainActivity;
import com.example.leilaoautopecastech.R;
import com.example.leilaoautopecastech.config.configFirebase;
import com.example.leilaoautopecastech.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoEmail = findViewById(R.id.campoLoginEmail);
        campoSenha = findViewById(R.id.campoLoginSenha);
        autenticacao = configFirebase.getFirebaseAutenticacao();

    }

    public void logarUsuario(Usuario usuario){

        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()

        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if( task.isSuccessful() ){
                    entrarTelaPrincipal();
                }else{
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        excecao = "Usuario nao esta cadastrado.";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "E-mail e senha nao correspondem a um usuario cadastrado";
                    }catch (Exception e){
                        excecao="Erro " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this,
                            excecao,Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void validarAutenticacaoUsuario (View view){

        String email = campoEmail.getText().toString();
        String senha = campoSenha.getText().toString();

            if ( !email.isEmpty()){
                if(!senha.isEmpty()){

                    Usuario usuario = new Usuario();
                    usuario.setEmail( email );
                    usuario.setSenha( senha );

                    logarUsuario( usuario );

                }else{

                    Toast.makeText(LoginActivity.this,
                            "Preencha a Senha",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(LoginActivity.this,
                        "Preencha o Email !",
                        Toast.LENGTH_SHORT).show();
            }
    }

    public void entrarTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);

    }

}
