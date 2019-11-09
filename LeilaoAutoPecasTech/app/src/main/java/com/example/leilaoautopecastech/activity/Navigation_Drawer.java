package com.example.leilaoautopecastech.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.leilaoautopecastech.R;
import com.example.leilaoautopecastech.Slider.Slider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Toast;

public class Navigation_Drawer extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;


    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(


                R.id.nav_home, R.id.nav_meusanuncios, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_perfil, R.id.nav_sair)



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
                    Toast.makeText(Navigation_Drawer.this, "TESTANDO", Toast.LENGTH_SHORT).show();
                }

                if(destination.getId() == R.id.nav_meusanuncios){
                    Toast.makeText(Navigation_Drawer.this, "MeusAnuncios", Toast.LENGTH_SHORT).show();
                }

                if(destination.getId() == R.id.nav_perfil){
                    Toast.makeText(Navigation_Drawer.this, "PERFIL", Toast.LENGTH_SHORT).show();
                }
                if(destination.getId() == R.id.nav_sair){
                    sair();
                }

            }
        });

    }

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


    private void sair(){
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(getApplicationContext(), Slider.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        Toast.makeText(Navigation_Drawer.this, "Saindo", Toast.LENGTH_SHORT).show();



    }
//public void onBackPressed(){
    //DrawerLayout drawerLayout = (DrawerLayout findViewById(R.id.drawer_layout))

//}

}


