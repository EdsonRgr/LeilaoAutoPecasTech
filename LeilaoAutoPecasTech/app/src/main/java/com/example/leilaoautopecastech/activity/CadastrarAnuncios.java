package com.example.leilaoautopecastech.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.leilaoautopecastech.R;
import com.example.leilaoautopecastech.helper.Permissoes;
import com.santalu.maskedittext.MaskEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CadastrarAnuncios extends AppCompatActivity implements View.OnClickListener{

    private ImageView imagem1,imagem2,imagem3;
    private EditText campoTitulo, campoDescricao;
    private CurrencyEditText campoValor;
    private MaskEditText campoTelefone;

    private Spinner spinnerMarca, spinnerModelo, spinnerCategoria, spinnerPeca;

    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private List<String> listaFotosRecuperadas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_anuncios);
        Permissoes.validarPermissoes(permissoes, this ,2);
        inicializaComponentes();
        carregaDadosSpinner();

    }
    public void salvarAnuncio(View view ){
        String valor = campoValor.getHintString();
        Log.d("salvar", "salvarAnuncio" + valor);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Activity.RESULT_OK){
            Uri imagemSelecionada = data.getData();
            String caminhoImagem = imagemSelecionada.toString();

            if ( requestCode == 1 ){
                imagem1.setImageURI( imagemSelecionada );

            }else if (requestCode == 2 ){
                imagem2.setImageURI( imagemSelecionada );

            }else if (resultCode == 3){
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
        campoTitulo = findViewById(R.id.TituloCad);
        campoDescricao = findViewById(R.id.DescricaoCad);
        campoValor = findViewById(R.id.ValorCad);
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
