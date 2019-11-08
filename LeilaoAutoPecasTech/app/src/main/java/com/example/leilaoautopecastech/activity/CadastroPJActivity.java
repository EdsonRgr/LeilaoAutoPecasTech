package com.example.leilaoautopecastech.activity;

<<<<<<< HEAD
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leilaoautopecastech.R;
import com.example.leilaoautopecastech.config.ValidaCNPJ;
import com.example.leilaoautopecastech.config.configFirebase;
import com.example.leilaoautopecastech.model.PessoaFisica;
import com.example.leilaoautopecastech.model.PessoaJuridica;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;


public class CadastroPJActivity extends AppCompatActivity {

    private EditText campoNomeF , campoEmail , campoTelefone , campoEndereco , campoCNPJ , campoSenha;
    private FirebaseAuth autenticacao;

=======
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.leilaoautopecastech.R;

public class CadastroPJActivity extends AppCompatActivity {

>>>>>>> 058679d123fff3942d04ee81a5a8003addc82644
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pj);
<<<<<<< HEAD
        campoNomeF = findViewById(R.id.campoNomeF);
        campoEmail = findViewById(R.id.campoEmail);
        campoTelefone = findViewById(R.id.campoTelefone);
        campoEndereco = findViewById(R.id.campoEndereco);
        campoCNPJ = findViewById(R.id.campoCNPJ);
        campoSenha = findViewById(R.id.campoSenha);
    }

    public void validarUsuarioPJ (View view){
        String textoNomeF = campoNomeF.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoTelefone = campoTelefone.getText().toString();
        String textoEndereco = campoEndereco.getText().toString();
        String textoCNPJ = campoCNPJ.getText().toString();
        String textoSenha = campoSenha.getText().toString();

        if(!textoNomeF.isEmpty() && !textoEmail.isEmpty() && !textoTelefone.isEmpty() && !textoEndereco.isEmpty() && !textoCNPJ.isEmpty() && !textoSenha.isEmpty() ){

            if ( ValidaCNPJ.isCNPJ(textoCNPJ) == true){
                PessoaJuridica pessoaJuridica = new PessoaJuridica();
                pessoaJuridica.setNomeF(textoNomeF);
                pessoaJuridica.setEmail(textoEmail);
                pessoaJuridica.setTelefone(textoTelefone);
                pessoaJuridica.setEndereco(textoEndereco);
                pessoaJuridica.setCNPJ(textoCNPJ);
                pessoaJuridica.setSenha(textoSenha);
                cadastrarUsuarioPJ(pessoaJuridica);
            } else {Toast.makeText(CadastroPJActivity.this , "CNPJ invalido" , Toast.LENGTH_SHORT).show();}
        }else{
            Toast.makeText(CadastroPJActivity.this , "Preencha todos os dados" , Toast.LENGTH_SHORT).show();
        }


    }

    public  void cadastrarUsuarioPJ ( final PessoaJuridica pessoaJuridica){
        autenticacao = configFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                pessoaJuridica.getEmail(), pessoaJuridica.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Toast.makeText(CadastroPJActivity.this,"Sucesso ao cadastrar Usuário !",
                            Toast.LENGTH_SHORT).show();

                    finish();

                }else{
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Por favor digite um email válido";
                    }catch (FirebaseAuthUserCollisionException e){
                        excecao = "Esta conta já foi cadastrada";
                    }catch (Exception e){
                        excecao="Erro ao cadastrar Usuário" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroPJActivity.this,
                            excecao,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

=======
    }
>>>>>>> 058679d123fff3942d04ee81a5a8003addc82644
}
