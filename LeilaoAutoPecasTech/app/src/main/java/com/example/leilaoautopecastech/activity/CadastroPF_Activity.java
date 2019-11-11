package com.example.leilaoautopecastech.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leilaoautopecastech.R;
import com.example.leilaoautopecastech.config.ConfigFirebase;
import com.example.leilaoautopecastech.helper.Base64Custom;
import com.example.leilaoautopecastech.model.PessoaFisica;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroPF_Activity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private FirebaseAuth autenticacao;

    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pf_);
        campoNome = findViewById(R.id.campoNome);
        campoEmail = findViewById(R.id.campoEmail);
        campoSenha = findViewById(R.id.campoSenha);

    }
    public void validarUsuario(View view) {

        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();


        if (!textoNome.isEmpty() &&
                !textoEmail.isEmpty() &&
                !textoSenha.isEmpty()) {

            PessoaFisica pessoaFisica = new PessoaFisica();
            pessoaFisica.setNome(textoNome);
            pessoaFisica.setEmail(textoEmail);
            pessoaFisica.setSenha(textoSenha);
            cadastrarUsuario (pessoaFisica);

        } else {
            Toast.makeText(CadastroPF_Activity.this, "preencha todos os dados !", Toast.LENGTH_SHORT).show();
        }

    }

    public  void cadastrarUsuario( final PessoaFisica pessoaFisica){ autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(pessoaFisica.getEmail(), pessoaFisica.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){


                    Toast.makeText(CadastroPF_Activity.this,"Sucesso ao cadastrar Usuário !",
                            Toast.LENGTH_SHORT).show();

                    String idUsuario = Base64Custom.codificarBase64( pessoaFisica.getEmail());
                    pessoaFisica.setidUsuario( idUsuario );
                    Log.i("testando", task.getResult().getUser().getUid());
                    pessoaFisica.salvarPessoaFisica();

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
                    Toast.makeText(CadastroPF_Activity.this,
                            excecao,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
