package com.example.leilaoautopecastech.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.leilaoautopecastech.R;
import com.example.leilaoautopecastech.config.ConfigFirebase;
import com.example.leilaoautopecastech.helper.Permissoes;
import com.example.leilaoautopecastech.helper.UserPFFirebase;
import com.example.leilaoautopecastech.helper.UserPJFirebase;
import com.example.leilaoautopecastech.model.Anuncio;
import com.example.leilaoautopecastech.model.PessoaJuridica;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.santalu.maskedittext.MaskEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

import static android.provider.MediaStore.*;

public class CadastrarAnuncios extends AppCompatActivity implements View.OnClickListener{



    private android.app.AlertDialog dialog;

    private StorageReference storage;

    private ImageView imagem1,imagem2,imagem3;
    private EditText campoTitulo, campoDescricao,campoNomeEmpresa;
    private CurrencyEditText campoValor;
    private MaskEditText campoTelefone;
    private Anuncio anuncio;
    private Spinner spinnerMarca, spinnerModelo, spinnerCategoria, spinnerPeca;
    private PessoaJuridica userLogado;
    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private List<String> listaFotosRecuperadas = new ArrayList<>();
    private List<String> listaURLFotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_anuncios);

        Toolbar toolbar = findViewById(R.id.toolbarP);
        toolbar.setTitle("Cadastrar anúncio");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);






        storage = ConfigFirebase.getFirebaseStorage();

        Permissoes.validarPermissoes(permissoes, this ,2);

        inicializaComponentes();


        FirebaseUser usuarioLogado = UserPJFirebase.getUsuatioAtual();
        campoNomeEmpresa.setText(usuarioLogado.getDisplayName());


        carregaDadosSpinner();

    }



    public void salvarAnuncio( ){

            dialog = new SpotsDialog.Builder()
                    .setContext( this )
                    .setMessage("Salvando essa fita ae")
                    .setCancelable( false )
                    .setTheme(R.style.Custom)
                    .build();
            dialog.show();


        for(int i =0; i < listaFotosRecuperadas.size(); i++ ){
            String urlImagem = listaFotosRecuperadas.get(i);
            int tamanhoLista = listaFotosRecuperadas.size();
           salvarFotosStorage(urlImagem, tamanhoLista, i );




        }


    }

    private void salvarFotosStorage(String urlString, final int totalfotos, int contador) {
        //no do storage
        final StorageReference imagemAnuncio = storage.child("imagens")
                .child("anuncios").child(anuncio.getIdAnuncio()).child("imagem"+contador);
        //up das fotos
        UploadTask uploadTask = imagemAnuncio.putFile( Uri.parse(urlString));

        Task<Uri> urkTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw task.getException();
                }

                return imagemAnuncio.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri firebaseUrl = task.getResult();
                    String urlConvertida = firebaseUrl.toString();

                    listaURLFotos.add( urlConvertida );
                    if (totalfotos == listaURLFotos.size()){
                        anuncio.setFotos( listaURLFotos );
                        anuncio.salvar();

                        finish();
                        dialog.dismiss();
                    }

                } else {
                    Toast.makeText(CadastrarAnuncios.this, "Falhou", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Toast.makeText(CadastrarAnuncios.this, "" + e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }


    private Anuncio configuraAnuncio(){


        String nomeEmpresa = campoNomeEmpresa.getText().toString();
        String marca = spinnerMarca.getSelectedItem().toString();
        String modelo = spinnerModelo.getSelectedItem().toString();
        String categoria = spinnerCategoria.getSelectedItem().toString();
        String peca = spinnerPeca.getSelectedItem().toString();
        String titulo = campoTitulo.getText().toString();
        String valor = campoValor.getText().toString();
        String telefone = campoTelefone.getText().toString();
        String fone = "";
        if(campoTelefone.getRawText() != null){
            fone = campoTelefone.getRawText().toString();
        }
        String descricao = campoDescricao.getText().toString();

        Anuncio anuncio = new Anuncio();
        anuncio.setMarcas( marca );
        anuncio.setModelos( modelo);
        anuncio.setCategorias(categoria);
        anuncio.setPecas(peca);
        anuncio.setNomeEmpresa(nomeEmpresa);
        anuncio.setTitulo(titulo);
        anuncio.setValor(valor);
        anuncio.setTelefone(telefone);
        anuncio.setFone(fone);
        anuncio.setDescricao(descricao);

        return anuncio;
    }

    public void validarDadosAnuncio(View view){

        anuncio = configuraAnuncio();

        //para validar o valor
        String valor = String.valueOf(campoValor.getRawValue());


        if( listaFotosRecuperadas.size() >= 2 ){
            if ( !anuncio.getMarcas().isEmpty() ){
                if ( !anuncio.getModelos().isEmpty() ){
                    if ( !anuncio.getCategorias().isEmpty() ){
                        if ( !anuncio.getPecas().isEmpty() ){
                            if ( !anuncio.getTitulo().isEmpty() ){
                                if ( !valor.isEmpty() && !valor.equals("0") ){
                                    if ( !anuncio.getTelefone().isEmpty() && anuncio.getFone().length() >= 11 ){
                                        if ( !anuncio.getDescricao().isEmpty() ){
                                            salvarAnuncio();
                                        }else{
                                            mensagemDeErro("Preencha a Descrição ! ");
                                        }

                                    }else{
                                        mensagemDeErro("Preencha corretamente o campo telefone! ");
                                    }

                                }else{
                                    mensagemDeErro("Preencha o campo Valor ! ");
                                }

                            }else{
                                mensagemDeErro("Preencha o campo Titulo ! ");
                            }

                        }else{
                            mensagemDeErro("Preencha o campo Peças ! ");
                        }

                    }else{
                        mensagemDeErro("Preencha o campo Categoria ! ");
                    }

                }else{
                    mensagemDeErro("Preencha o campo Modelo ! ");
                }
            }else{
                mensagemDeErro("Preencha o campo Marca ! ");
            }
        }else {
            mensagemDeErro("Selecione pelo menos 2 fotos");
        }



    }


    private void mensagemDeErro(String mensagem){

        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cadImgAnuncio1:
                escolherImagem(1);
                break;
            case R.id.cadImgAnuncio2:
                escolherImagem(2);
                break;
            case R.id.cadImgAnuncio3:
                escolherImagem(3);
                break;
        }
    }
    private void escolherImagem(int requestCode) {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){

            Uri imagemSelecionada = data.getData();
            String caminhoImagem = imagemSelecionada.toString();

            if ( requestCode == 1 ){
                imagem1.setImageURI( imagemSelecionada );

            }else if (requestCode == 2 ){
                imagem2.setImageURI( imagemSelecionada );

            }else if (requestCode == 3){
                imagem3.setImageURI( imagemSelecionada);

            } listaFotosRecuperadas.add( caminhoImagem );

        }

    }

    private void carregaDadosSpinner(){


        //spinner marca do carro
        String[] marca = getResources().getStringArray(R.array.marca);
        ArrayAdapter<String> adapterMarca = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,marca);
        adapterMarca.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerMarca.setAdapter( adapterMarca );


        //spinner modelo
        String[] modelos = getResources().getStringArray(R.array.modelo);
        ArrayAdapter<String> adapterModelo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,modelos);
        adapterModelo.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerModelo.setAdapter( adapterModelo);

        //spinner categorias
        String[] categorias = getResources().getStringArray(R.array.categoria);
        ArrayAdapter<String> adapterCategorias = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,categorias);
        adapterCategorias.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter( adapterCategorias );

        //spinner Pecas
        String[] pecas = getResources().getStringArray(R.array.pecas);
        ArrayAdapter<String> adapterPecas = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,pecas);
        adapterPecas.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerPeca.setAdapter( adapterPecas );


    }


    private void inicializaComponentes(){
        campoNomeEmpresa = findViewById(R.id.NomeFantasia);
        campoNomeEmpresa.setFocusable(false);
        campoTitulo = findViewById(R.id.TituloCad);
        campoDescricao = findViewById(R.id.DescricaoCad);
        campoValor = findViewById(R.id.ValorCad);
        campoTelefone = findViewById(R.id.TelefoneCad);
        imagem1 = findViewById(R.id.cadImgAnuncio1);
        imagem2 = findViewById(R.id.cadImgAnuncio2);
        imagem3 = findViewById(R.id.cadImgAnuncio3);
        imagem1.setOnClickListener(this);
        imagem2.setOnClickListener(this);
        imagem3.setOnClickListener(this);


        //spinners
        spinnerMarca = findViewById(R.id.spinnerMarca);
        spinnerModelo = findViewById(R.id.spinnerModelo);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        spinnerPeca = findViewById(R.id.spinnerPecas);


        // mascara de moeda em portugues
        Locale local = new Locale ("pt", "BR");
        campoValor.setLocale( local);

    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado: grantResults){
            if(permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }
    }

    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permições");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



}
