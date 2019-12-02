package com.example.leilaoautopecastech.activity.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.leilaoautopecastech.R;
import com.example.leilaoautopecastech.activity.DetalhesAnuncio;
import com.example.leilaoautopecastech.activity.Navigation_Drawer;
import com.example.leilaoautopecastech.activity.adapter.AdapterAnuncios;
import com.example.leilaoautopecastech.config.ConfigFirebase;
import com.example.leilaoautopecastech.helper.RecyclerItemClickListener;
import com.example.leilaoautopecastech.model.Anuncio;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class HomeFragment extends Fragment {

    private List<Anuncio> listaAnuncios = new ArrayList<>();
    private Button buttonMarca, buttonModelo, btnCarregar;
    private AdapterAnuncios adapterAnuncios;
    private DatabaseReference anunciosPublicosRef;
    private AlertDialog dialog;
    private String filtroMarca = "";
    private String filtroModelo = "";
    private boolean filtrandoPorModelo = false;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        anunciosPublicosRef = ConfigFirebase.getFirebaseDatabase()
                .child("anuncios");


        //reciclerview
        RecyclerView recyclerAnunciosPublicos  = (RecyclerView) root.findViewById(R.id.recyclerAnunciosPublicos);

        recyclerAnunciosPublicos.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAnunciosPublicos.setHasFixedSize(true);
        adapterAnuncios = new AdapterAnuncios(listaAnuncios, getContext());
        recyclerAnunciosPublicos.setAdapter( adapterAnuncios );
        recyclerAnunciosPublicos.smoothScrollToPosition(0);

        recyclerAnunciosPublicos.addOnItemTouchListener(
                 new RecyclerItemClickListener(
                         getContext(),
                         recyclerAnunciosPublicos,
                         new RecyclerItemClickListener.OnItemClickListener() {
                             @Override
                             public void onItemClick(View view, int position) {
                                 Anuncio anunciSelecionado = listaAnuncios.get(position);
                                 Intent i = new Intent(getActivity(), DetalhesAnuncio.class);
                                 i.putExtra("AnuncioSelecionado" , anunciSelecionado);
                                 startActivity( i );
                             }

                             @Override
                             public void onLongItemClick(View view, int position) {

                             }

                             @Override
                             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                             }
                         }
                 )

        );

        //



        //recuparAnucios
        dialog = new SpotsDialog.Builder()
                .setContext( getContext() )
                .setMessage("Carregando Anuncios")
                .setCancelable( false )
                .setTheme(R.style.Custom)
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
                                    listaAnuncios.add(0, anuncio);


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


        buttonMarca = root.findViewById(R.id.buttonMarca);
        buttonMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrarPorMarca();
            }
        });

        buttonModelo = root.findViewById(R.id.buttonModelo);
        buttonModelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filtrarPorModelo();
            }
        });
        btnCarregar = root.findViewById(R.id.recarregar);
        btnCarregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Navigation_Drawer.class);
                startActivity(intent);
            }
        });



        return root;
    }


//////////////filtrarpelosbotoes

    public void filtrarPorMarca(){

        android.app.AlertDialog.Builder dialogMarca = new android.app.AlertDialog.Builder(getContext());
        dialogMarca.setTitle("Selecione uma marca");

        //Configura o spinner
        View viewSpinner = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
        //

        final Spinner spinnerMarca = viewSpinner.findViewById(R.id.spinnerFiltro);

        //spinner de marcas
        String[] marca = getResources().getStringArray(R.array.marca);
        ArrayAdapter<String> adapterMarca = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item,
                marca
        );
        adapterMarca.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinnerMarca.setAdapter( adapterMarca );
        //

        dialogMarca.setView( viewSpinner );

        dialogMarca.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filtroMarca = spinnerMarca.getSelectedItem().toString();
                Log.d("filtro", "filtro :" + filtroMarca);
                recuperarAnunciosPorMarca();
                filtrandoPorModelo = true;

            }
        });

        dialogMarca.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        android.app.AlertDialog dialog = dialogMarca.create();
        dialog.show();

    }

    public void filtrarPorModelo(){

        if( filtrandoPorModelo == true){
            android.app.AlertDialog.Builder dialogMarca = new android.app.AlertDialog.Builder(getContext());
            dialogMarca.setTitle("Selecione uma Modelo");

            //Configura o spinner
            View viewSpinner = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
            //

            final Spinner spinnerModelo = viewSpinner.findViewById(R.id.spinnerFiltro);

            //spinner de modelos
            String[] marca = getResources().getStringArray(R.array.modelo);
            ArrayAdapter<String> adapterMarca = new ArrayAdapter<String>(
                    getContext(), android.R.layout.simple_spinner_item,
                    marca
            );
            adapterMarca.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
            spinnerModelo.setAdapter( adapterMarca );
            //

            dialogMarca.setView( viewSpinner );

            dialogMarca.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    filtroModelo = spinnerModelo.getSelectedItem().toString();
                    recuperarAnunciosPorModelo();



                }
            });

            dialogMarca.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            android.app.AlertDialog dialog = dialogMarca.create();
            dialog.show();

        }else{
            Toast.makeText(getContext(),"Escolha primeiro a Marca de um veiculo!",Toast.LENGTH_SHORT).show();
        }


    }



    public void recuperarAnunciosPorMarca(){

        dialog = new SpotsDialog.Builder()
                .setContext( getContext() )
                .setMessage("Carregando Anuncios")
                .setCancelable( false )
                .build();
        dialog.show();

        anunciosPublicosRef = ConfigFirebase.getFirebaseDatabase()
                .child("anuncios")
                .child(filtroMarca);

        anunciosPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaAnuncios.clear();
                for(DataSnapshot modelos: dataSnapshot.getChildren()){
                    for (DataSnapshot categorias: modelos.getChildren()){
                        for(DataSnapshot pecas: categorias.getChildren()){
                            for (DataSnapshot anuncios: pecas.getChildren()){

                                Anuncio anuncio = anuncios.getValue(Anuncio.class);
                                listaAnuncios.add(anuncio);
                            }
                        }
                    }
                }
                Collections.reverse(listaAnuncios);
                adapterAnuncios.notifyDataSetChanged();
                dialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void recuperarAnunciosPorModelo(){

        dialog = new SpotsDialog.Builder()
                .setContext( getContext() )
                .setMessage("Carregando Anuncios")
                .setCancelable( false )
                .build();
        dialog.show();

        anunciosPublicosRef = ConfigFirebase.getFirebaseDatabase()
                .child("anuncios")
                .child(filtroMarca)
                .child( filtroModelo );

        anunciosPublicosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaAnuncios.clear();
                for (DataSnapshot categorias :dataSnapshot.getChildren()){
                    for(DataSnapshot pecas: categorias.getChildren()){
                        for (DataSnapshot anuncios: pecas.getChildren()){

                            Anuncio anuncio = anuncios.getValue(Anuncio.class);
                            listaAnuncios.add(anuncio);
                        }
                    }
                }
                Collections.reverse(listaAnuncios);
                adapterAnuncios.notifyDataSetChanged();
                dialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }





}