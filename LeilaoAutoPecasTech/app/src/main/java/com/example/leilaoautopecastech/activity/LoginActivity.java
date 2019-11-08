package com.example.leilaoautopecastech.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leilaoautopecastech.R;
import com.example.leilaoautopecastech.config.configFirebase;
import com.example.leilaoautopecastech.model.PessoaFisica;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

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

    public void logarUsuario(PessoaFisica pessoaFisica){

        autenticacao.signInWithEmailAndPassword(
                pessoaFisica.getEmail(), pessoaFisica.getSenha()

        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if( task.isSuccessful() ){
                    entrarTelaPrincipal();
                    Toast.makeText(LoginActivity.this,
                            "Seja Bem-Vindo!!",Toast.LENGTH_SHORT).show();
                }else{
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        excecao = "PessoaFisica nao esta cadastrado.";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "E-mail e senha nao correspondem a um pessoaFisica cadastrado";
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

                    PessoaFisica pessoaFisica = new PessoaFisica();
                    pessoaFisica.setEmail( email );
                    pessoaFisica.setSenha( senha );

                    logarUsuario(pessoaFisica);

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
        Intent intent = new Intent(LoginActivity.this, Navigation_Drawer.class);
        startActivity(intent);

    }

}
