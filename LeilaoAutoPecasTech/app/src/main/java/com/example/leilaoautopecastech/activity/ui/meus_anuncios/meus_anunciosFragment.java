package com.example.leilaoautopecastech.activity.ui.meus_anuncios;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.leilaoautopecastech.R;
import com.example.leilaoautopecastech.activity.CadastrarAnuncios;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class meus_anunciosFragment extends Fragment {

    private meus_anunciosViewModel meusanunciosViewModel;
    public View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        meusanunciosViewModel =
                ViewModelProviders.of(this).get(meus_anunciosViewModel.class);
        this.root = inflater.inflate(R.layout.fragment_meus_anuncios, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        meusanunciosViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        FloatingActionButton fab = this.root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(root.getContext(), CadastrarAnuncios.class));
            }
        });

        return root;
    }
}