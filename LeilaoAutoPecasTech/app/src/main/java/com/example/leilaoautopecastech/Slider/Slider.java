package com.example.leilaoautopecastech.Slider;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.leilaoautopecastech.R;
import com.example.leilaoautopecastech.activity.CadastroPF_Activity;
import com.example.leilaoautopecastech.activity.CadastroPJ_Activity;
import com.example.leilaoautopecastech.activity.LoginActivity;
import com.example.leilaoautopecastech.activity.Navigation_Drawer;
import com.example.leilaoautopecastech.config.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

public class Slider extends IntroActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFullscreen(true);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);



        setButtonNextVisible(false);
        setButtonBackVisible(false);

        addSlide(new FragmentSlide.Builder()
            .background(android.R.color.white)
                .fragment(R.layout.intro_1)
                .build()
        );
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build()
        );
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_3)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false)
                .build()
        );

    }

    @Override
    protected void onStart() {
        super.onStart();
        verificaruserlog();

    }

    public void btCadastrarPF (View view){
            startActivity(new Intent(this, CadastroPF_Activity.class));
    }

    public void btCadastrarPJ (View view){
        startActivity(new Intent(this, CadastroPJ_Activity.class));
    }

    public void btEntrar (View view){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void verificaruserlog (){
        autenticacao = ConfigFirebase.getFirebaseAutenticacao();

        if (autenticacao.getCurrentUser() != null ){
            entrarTelaPrincipal();
        }

    }


    public void entrarTelaPrincipal(){
        Intent intent = new Intent(Slider.this, Navigation_Drawer.class);
        startActivity(intent);

    }
}
