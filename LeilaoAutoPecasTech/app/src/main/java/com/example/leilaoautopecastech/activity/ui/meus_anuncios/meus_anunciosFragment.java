package com.example.leilaoautopecastech.activity.ui.meus_anuncios;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
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

import dmax.dialog.SpotsDialog;


public class meus_anunciosFragment extends Fragment {


    private RecyclerView recyclerAnuncios;
    private List<Anuncio> anuncios = new ArrayList<>();
    private Anuncio mAnuncio;
    private AdapterAnuncios adapterAnuncios;
    private DatabaseReference anuncioUserRef;
    private AlertDialog dialog;

    public View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.root = inflater.inflate(R.layout.fragment_meus_anuncios, container, false);

        anuncioUserRef = ConfigFirebase.getFirebaseDatabase()
                .child("meus_anuncios")
                .child( ConfigFirebase.getIdUsuario());








        //reciclyview
        RecyclerView recyclerAnuncios  = (RecyclerView) root.findViewById(R.id.RecyclerAnuncios);
        recyclerAnuncios.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAnuncios.setHasFixedSize(true);
        adapterAnuncios = new AdapterAnuncios(anuncios, getContext());
        recyclerAnuncios.setAdapter( adapterAnuncios );


        //recuperarAnuncios

        dialog = new SpotsDialog.Builder()
                .setContext( getContext() )
                .setMessage("Carregando Anuncios")
                .setCancelable( false )
                .build();
        dialog.show();


        anuncioUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                anuncios.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Anuncio anuncio = ds.getValue( Anuncio.class);
                    anuncios.add (anuncio);
                }

                Collections.reverse( anuncios);
                adapterAnuncios.notifyDataSetChanged();


                    dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //swipe
        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START ;
                return makeMovementFlags(dragFlags, swipeFlags );


            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluir( viewHolder );
            }
        };

        new ItemTouchHelper( itemTouch).attachToRecyclerView( recyclerAnuncios );


        FloatingActionButton fab = this.root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(root.getContext(), CadastrarAnuncios.class));
            }
        });

        return root;


    }
    

    public void excluir(final RecyclerView.ViewHolder viewHolder ){
        AlertDialog.Builder alerDialog = new AlertDialog.Builder(getContext());

        alerDialog.setTitle("Excluir Anuncio");
        alerDialog.setMessage("Voce tem certeza que deseja excluir esse anuncio ?");
        alerDialog.setCancelable(false);

        alerDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    int position = viewHolder.getAdapterPosition();
                    mAnuncio = anuncios.get( position );
                    mAnuncio.excluirAnuncio();

                    adapterAnuncios.notifyDataSetChanged();

            }
        });

        alerDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText( getContext(),"Cancelado", Toast.LENGTH_SHORT).show();

                adapterAnuncios.notifyDataSetChanged();


            }
        });

        AlertDialog alert = alerDialog.create();
        alert.show();


    }


}