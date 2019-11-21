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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leilaoautopecastech.R;
import com.example.leilaoautopecastech.activity.CadastrarAnuncios;
import com.example.leilaoautopecastech.activity.adapter.AdapterAnuncios;
import com.example.leilaoautopecastech.config.ConfigFirebase;
import com.example.leilaoautopecastech.model.Anuncio;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class meus_anunciosFragment extends Fragment {


    private RecyclerView recyclerAnuncios;
    private List<Anuncio> anuncios = new ArrayList<>();
    private AdapterAnuncios adapterAnuncios;
    private DatabaseReference anuncioUserRef;

    public View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.root = inflater.inflate(R.layout.fragment_meus_anuncios, container, false);

        anuncioUserRef = ConfigFirebase.getFirebaseDatabase()
                .child("meus_anuncios")
                .child( ConfigFirebase.getIdUsuario());


        inicializarComponentes();

        //reciclyview
        RecyclerView recyclerAnuncios  = (RecyclerView) root.findViewById(R.id.RecyclerAnuncios);
        recyclerAnuncios.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAnuncios.setHasFixedSize(true);
        adapterAnuncios = new AdapterAnuncios(anuncios, getContext());
        recyclerAnuncios.setAdapter( adapterAnuncios );


        //recuperarAnuncios
        anuncioUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                anuncios.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    anuncios.add (ds.getValue(Anuncio.class));
                }

                Collections.reverse( anuncios);
                adapterAnuncios.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

   public void inicializarComponentes() {
        RecyclerView recyclerView  = (RecyclerView) root.findViewById(R.id.RecyclerAnuncios);

    }






}