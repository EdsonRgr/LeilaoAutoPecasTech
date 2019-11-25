package com.example.leilaoautopecastech.activity.ui.home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leilaoautopecastech.R;
import com.example.leilaoautopecastech.activity.adapter.AdapterAnuncios;
import com.example.leilaoautopecastech.config.ConfigFirebase;
import com.example.leilaoautopecastech.model.Anuncio;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class HomeFragment extends Fragment {

    private List<Anuncio> listaAnuncios = new ArrayList<>();
    private Button buttonMarca, buttonModelo;
    private AdapterAnuncios adapterAnuncios;
    private DatabaseReference anunciosPublicosRef;
    private AlertDialog dialog;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        anunciosPublicosRef = ConfigFirebase.getFirebaseDatabase()
                .child("anuncios");


        //reciclyview
        RecyclerView recyclerAnunciosPublicos  = (RecyclerView) root.findViewById(R.id.recyclerAnunciosPublicos);

        recyclerAnunciosPublicos.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAnunciosPublicos.setHasFixedSize(true);
        adapterAnuncios = new AdapterAnuncios(listaAnuncios, getContext());
        recyclerAnunciosPublicos.setAdapter( adapterAnuncios );
        //


        //recuparAnucios
        dialog = new SpotsDialog.Builder()
                .setContext( getContext() )
                .setMessage("Carregando Anuncios")
                .setCancelable( false )
                .build();
        dialog.show();
        listaAnuncios.clear();
        anunciosPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot marcas: dataSnapshot.getChildren()){
                    for(DataSnapshot modelos: marcas.getChildren()){
                        for (DataSnapshot categorias: modelos.getChildren()){
                            for(DataSnapshot pecas: categorias.getChildren()){
                                for (DataSnapshot anuncios: pecas.getChildren()){

                                    Anuncio anuncio = anuncios.getValue(Anuncio.class);
                                    listaAnuncios.add(anuncio);


                                }
                            }
                        }
                    }
                }

                Collections.reverse( listaAnuncios );
                adapterAnuncios.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        return root;
    }




}