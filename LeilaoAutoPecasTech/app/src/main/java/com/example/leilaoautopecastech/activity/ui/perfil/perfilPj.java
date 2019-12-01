package com.example.leilaoautopecastech.activity.ui.perfil;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.leilaoautopecastech.R;
import com.example.leilaoautopecastech.helper.UserPFFirebase;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.santalu.maskedittext.MaskEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class perfilPj extends AppCompatActivity {

    private CircleImageView imageEditarPerfil;
    private MaskEditText editTelefonePerfil;
    private TextView textAlterarFoto, editEmailPerfil, textCNPJPerfil,editNomeFPerfil;
    private TextInputEditText  editEnderecoPerfil ;
    private Button buttonEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_pj);
        Toolbar toolbar = findViewById(R.id.toolbarP);
        toolbar.setTitle("Editar Perfil - PJ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inicializarComponentesPJ();

        FirebaseUser usuarioPerfil = UserPFFirebase.getUsuatioAtual();
        editNomeFPerfil.setText(usuarioPerfil.getDisplayName());
        editTelefonePerfil.setText(usuarioPerfil.getPhoneNumber());

        editEmailPerfil.setText(usuarioPerfil.getEmail());


    }

    public void inicializarComponentesPJ(){
        imageEditarPerfil = findViewById(R.id.editImagePerfilPJ);
        textAlterarFoto = findViewById(R.id.editAlterarFotoPJ);
        editNomeFPerfil = findViewById(R.id.editAlterarNomeF);
        editEnderecoPerfil = findViewById(R.id.editAlterarEndereco);
        editTelefonePerfil = findViewById(R.id.editAlterarTelefone);
        editEmailPerfil = findViewById(R.id.textEmailPJ);
        textCNPJPerfil = findViewById(R.id.textCNPJ);

        buttonEdit = findViewById(R.id.buttonEdit);


    }

}
