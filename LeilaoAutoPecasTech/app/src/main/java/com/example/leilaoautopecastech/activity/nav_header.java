package com.example.leilaoautopecastech.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.leilaoautopecastech.R;
import com.example.leilaoautopecastech.helper.UserFirebase;
import com.example.leilaoautopecastech.model.PessoaFisica;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;


import de.hdodenhof.circleimageview.CircleImageView;

public class nav_header extends AppCompatActivity {

    private CircleImageView imagePerfil;
    private TextView nomePerfil, emailPerfil;
    private PessoaFisica usuarioLogado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_navigation__drawer);

        View header = ((NavigationView)findViewById(R.id.nav_view)).getHeaderView(0);


        usuarioLogado = UserFirebase.getDadodsUsuarioLogado();


        imagePerfil = header.findViewById(R.id.imagePerfil);
        nomePerfil = header.findViewById(R.id.userName);
        emailPerfil = header.findViewById(R.id.userEmail);

        FirebaseUser usuarioPerfil = UserFirebase.getUsuatioAtual();
        nomePerfil.setText(usuarioPerfil.getDisplayName());
        emailPerfil.setText(usuarioPerfil.getEmail());


    }
}