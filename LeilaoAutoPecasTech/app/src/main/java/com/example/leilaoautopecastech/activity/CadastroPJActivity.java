package com.example.leilaoautopecastech.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leilaoautopecastech.R;
import com.example.leilaoautopecastech.config.ValidaCNPJ;
import com.example.leilaoautopecastech.config.ConfigFirebase;
import com.example.leilaoautopecastech.helper.Base64Custom;
import com.example.leilaoautopecastech.model.PessoaJuridica;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dmax.dialog.SpotsDialog;


public class CadastroPJActivity extends AppCompatActivity {

    private EditText campoNomeF , campoEmail , campoTelefone , campoEndereco , campoCNPJ , campoSenha;
    private FirebaseAuth autenticacao;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private String tipoUser = "PJ";
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pj);

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

                dialog = new SpotsDialog.Builder()
                        .setContext( this )
                        .setMessage("Perae que esta cadastrando")
                        .setCancelable( false )
                        .setTheme(R.style.Custom)
                        .build();
                dialog.show();

                pessoaJuridica.setTipo(tipoUser);

                cadastrarUsuarioPJ(pessoaJuridica);
            } else {Toast.makeText(CadastroPJActivity.this , "CNPJ invalido" , Toast.LENGTH_SHORT).show();}
        }else{
            Toast.makeText(CadastroPJActivity.this , "Preencha todos os dados" , Toast.LENGTH_SHORT).show();
        }


    }

    public  void cadastrarUsuarioPJ ( final PessoaJuridica pessoaJuridica){
        autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                pessoaJuridica.getEmail(), pessoaJuridica.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    Toast.makeText(CadastroPJActivity.this,"Sucesso ao cadastrar Usuário !",
                            Toast.LENGTH_SHORT).show();

                    String idUsuario = task.getResult().getUser().getUid();
                    pessoaJuridica.setidUsuario( idUsuario );
                   // Log.i("testando", task.getResult().getUser().getUid());
                    pessoaJuridica.salvarPessoaJuridica();

                    dialog.dismiss();
                    finish();

                }else{
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        dialog.dismiss();
                        excecao = "Digite uma senha mais forte";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        dialog.dismiss();
                        excecao = "Por favor digite um email válido";
                    }catch (FirebaseAuthUserCollisionException e){
                        dialog.dismiss();
                        excecao = "Esta conta já foi cadastrada";
                    }catch (Exception e){
                        dialog.dismiss();
                        excecao="Erro ao cadastrar Usuário" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroPJActivity.this,
                            excecao,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    }


