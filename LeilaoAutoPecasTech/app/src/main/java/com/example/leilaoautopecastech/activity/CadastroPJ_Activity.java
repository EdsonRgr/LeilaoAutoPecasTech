package com.example.leilaoautopecastech.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.leilaoautopecastech.R;
import com.example.leilaoautopecastech.config.ValidaCNPJ;
import com.example.leilaoautopecastech.config.ConfigFirebase;
import com.example.leilaoautopecastech.helper.UserPFFirebase;
import com.example.leilaoautopecastech.helper.UserPJFirebase;
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
import com.santalu.maskedittext.MaskEditText;

import dmax.dialog.SpotsDialog;


public class CadastroPJ_Activity extends AppCompatActivity {

    private EditText campoNomeF , campoEmail ,  campoEndereco , campoSenha;
    private MaskEditText campoTelefone, campoCNPJ;
    private FirebaseAuth autenticacao;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private String tipoUser = "pessoaJuridica";
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
        String fone = "";
        String textoNomeF = campoNomeF.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoTelefone = campoTelefone.getText().toString();
        if(campoTelefone.getRawText() !=null){
            fone = campoTelefone.getRawText().toString();
        }
        String textoEndereco = campoEndereco.getText().toString();
        String textoCNPJ = campoCNPJ.getRawText().toString();
        String textoSenha = campoSenha.getText().toString();

        if(!textoNomeF.isEmpty() && textoNomeF.length() >= 10 ){
            if(!textoEmail.isEmpty()){
                if(!textoTelefone.isEmpty() && fone.length() >=11 ){
                    if(!textoEndereco.isEmpty()){
                        if(!textoCNPJ.isEmpty()){
                            if(!textoSenha.isEmpty()){

                                if ( ValidaCNPJ.isCNPJ(textoCNPJ) == true){
                                    PessoaJuridica pessoaJuridica = new PessoaJuridica();
                                    pessoaJuridica.setNomeF(textoNomeF);
                                    pessoaJuridica.setEmail(textoEmail);
                                    pessoaJuridica.setTelefone(textoTelefone);
                                    pessoaJuridica.setEndereco(textoEndereco);
                                    pessoaJuridica.setCNPJ(textoCNPJ);
                                    pessoaJuridica.setSenha(textoSenha);
                                    pessoaJuridica.setTipo(tipoUser);

                                    dialog = new SpotsDialog.Builder()
                                            .setContext( this )
                                            .setMessage("Perae que esta cadastrando")
                                            .setCancelable( false )
                                            .setTheme(R.style.Custom)
                                            .build();
                                    dialog.show();

                                    cadastrarUsuarioPJ(pessoaJuridica);


                                } else {

                                    menssagemErro("CNPJ invalido");
                                }


                            }else{
                                menssagemErro("Preencha o campo senha");
                            }
                        }else{
                            menssagemErro("Preencha o campo CNPJ");
                        }
                    }else{
                        menssagemErro("Preencha o campo Endereço");
                    }
                }else{
                    menssagemErro("Preencha o campo telefone");
                }
            }else{
                menssagemErro("Preencha o Email corretamente");
            }
        }else{
            menssagemErro("Preencha seu Nome corretamente");
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


                    String idUsuario = task.getResult().getUser().getUid();
                    pessoaJuridica.setidUsuario( idUsuario );
                    pessoaJuridica.salvarPessoaJuridica();

                    UserPJFirebase.updateNomeUserPj(pessoaJuridica.getNomeF());
                    UserPJFirebase.updateTelUserPj(pessoaJuridica.getTelefone());

                //    UserPJFirebase.updateNomeUser(pessoaJuridica.getEndereco());
                   // UserPJFirebase.updateNomeUser(pessoaJuridica.getTelefone());

                    Toast.makeText(CadastroPJ_Activity.this,"Sucesso ao cadastrar Pessoa Juridica !",
                            Toast.LENGTH_SHORT).show();



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
                    Toast.makeText(CadastroPJ_Activity.this,
                            excecao,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void menssagemErro(String mensagem){
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }
    

    }


