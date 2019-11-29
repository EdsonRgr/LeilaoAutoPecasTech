package com.example.leilaoautopecastech.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.leilaoautopecastech.R;
import com.example.leilaoautopecastech.Slider.Slider;
import com.example.leilaoautopecastech.activity.adapter.AdapterAnuncios;
import com.example.leilaoautopecastech.config.ConfigFirebase;
import com.example.leilaoautopecastech.helper.UserFirebase;
import com.example.leilaoautopecastech.model.Anuncio;
import com.example.leilaoautopecastech.model.PessoaFisica;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dmax.dialog.SpotsDialog;

import static com.example.leilaoautopecastech.helper.UserFirebase.getUsuatioAtual;

public class Navigation_Drawer extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private String filtroMarca = "";
    private DatabaseReference anunciosPublicosRef;
    private android.app.AlertDialog dialog;
    private List<Anuncio> listaAnuncios = new ArrayList<>();
    private AdapterAnuncios adapterAnuncios;
    private PessoaFisica usuarioLogado;


    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        usuarioLogado = UserFirebase.getDadodsUsuarioLogado();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        TextView NomePerfil = (TextView) headerView.findViewById(R.id.userName);
        TextView EmailPerfil = (TextView) headerView.findViewById(R.id.userEmail);


       redirecionaUsuario();


        FirebaseUser usuarioPerfil = getUsuatioAtual();
        NomePerfil.setText( usuarioPerfil.getDisplayName());
        EmailPerfil.setText( usuarioPerfil.getEmail());


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(


                R.id.nav_home, R.id.nav_meusanuncios, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_perfil)



                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull
                    NavDestination destination, @Nullable Bundle arguments) {


                if(destination.getId() == R.id.nav_home){
                    Toast.makeText(Navigation_Drawer.this, "feed!", Toast.LENGTH_SHORT).show();

                }

                if(destination.getId() == R.id.nav_meusanuncios){
                    Toast.makeText(Navigation_Drawer.this, "Meus Anuncios", Toast.LENGTH_SHORT).show();
                }

                if(destination.getId() == R.id.nav_perfil){
                    Toast.makeText(Navigation_Drawer.this, "PERFIL", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void hideItem()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_meusanuncios).setVisible(false);
    }



    ///////////
    public void redirecionaUsuario(){

        DatabaseReference usuarioRef = ConfigFirebase.getFirebaseDatabase()
                .child("PessoaFisica")
                .child(getIdentificadorUsuario());

        usuarioRef.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                PessoaFisica pessoaFisica = dataSnapshot.getValue(PessoaFisica.class);

                String tipoUsuario = pessoaFisica.getTipo();

                if(tipoUsuario.equals("pessoaFisica")){
                    hideItem();
                    Toast.makeText(Navigation_Drawer.this, "Testando", Toast.LENGTH_SHORT).show();

                }else{


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static String getIdentificadorUsuario(){
        return getUsuatioAtual().getUid();

    }

    //////////////





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation__drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public boolean sair (MenuItem item){

        AlertDialog.Builder builder = new AlertDialog.Builder(Navigation_Drawer.this);
        builder.setTitle("Deseja sair ?");
        builder.setMessage(" Pressione Sim para sair");
        builder.setCancelable(true);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Slider.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Toast.makeText(Navigation_Drawer.this, "Saindo", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return true;
    }






}


