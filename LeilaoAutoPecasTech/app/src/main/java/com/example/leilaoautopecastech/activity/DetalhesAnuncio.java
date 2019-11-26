package com.example.leilaoautopecastech.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leilaoautopecastech.R;
import com.example.leilaoautopecastech.model.Anuncio;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;


public class DetalhesAnuncio extends AppCompatActivity {

    private CarouselView carouselView;
    private TextView titulo, valor, categoria, peca, descricao;
    private Anuncio anuncioSelecionado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_anuncio);

        //Configurar toolbar


        inicializarComponentes();


        //recupera o anuncio
        anuncioSelecionado =(Anuncio) getIntent().getSerializableExtra("AnuncioSelecionado");

        if(anuncioSelecionado != null ){

            titulo.setText( anuncioSelecionado.getTitulo());
            valor.setText(anuncioSelecionado.getValor());
            categoria.setText(anuncioSelecionado.getCategorias());
            peca.setText(anuncioSelecionado.getPecas());
            descricao.setText(anuncioSelecionado.getDescricao());

            ImageListener imageListener = new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {

                    String urlString = anuncioSelecionado.getFotos().get( position) ;
                    Picasso.get().load(urlString).into(imageView);

                }
            };

            carouselView.setPageCount( anuncioSelecionado.getFotos().size() );
            carouselView.setImageListener( imageListener );






        }


    }


    public void entrarEmContato(View view){

/*
* Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("http://api.whatsapp.com/send?phone" + anuncioSelecionado.getTelefone()+ "Bom dia, boa tarde, boa noite"));
        startActivity( i );
*
*
* */


        Intent i = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", anuncioSelecionado.getTelefone(),null));
        startActivity( i );

    }

    private void inicializarComponentes(){
        carouselView = findViewById(R.id.carouselView);
        titulo = findViewById(R.id.detalheTitulo);
        valor =  findViewById(R.id.detalheValor);
        categoria =  findViewById(R.id.detalheCategoria);
        peca = findViewById(R.id.detalhePeca);
        descricao = findViewById(R.id.detalheDescricao);



    }
}
