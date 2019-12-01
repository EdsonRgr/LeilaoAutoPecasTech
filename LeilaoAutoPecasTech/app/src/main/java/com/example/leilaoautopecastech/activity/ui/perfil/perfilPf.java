package com.example.leilaoautopecastech.activity.ui.perfil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leilaoautopecastech.R;
import com.example.leilaoautopecastech.activity.LoginActivity;
import com.example.leilaoautopecastech.activity.Navigation_Drawer;
import com.example.leilaoautopecastech.config.ConfigFirebase;
import com.example.leilaoautopecastech.helper.UserPFFirebase;
import com.example.leilaoautopecastech.helper.UsuarioFirebase;
import com.example.leilaoautopecastech.model.PessoaFisica;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class perfilPf extends AppCompatActivity {

    private CircleImageView imageEditarPerfil;
    private TextView textAlterarFoto, editEmailPerfil;
    private TextInputEditText editNomePerfil;
    private Button buttonEdit;

    private PessoaFisica userLogado;
    private StorageReference storageRef;
    private String identificadorUser;
    private android.app.AlertDialog dialog;

    private static final int SELECAO_GALERIA = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_pf);
        Toolbar toolbar = findViewById(R.id.toolbarP);
        toolbar.setTitle("Editar Perfil - PF");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userLogado = UserPFFirebase.getDadodsUsuarioLogadoPF();


        // configuraçoes init
        inicializarComponentesPF();
        storageRef = ConfigFirebase.getFirebaseStorage();
        identificadorUser = UserPFFirebase.getIdentificadorUsuario();
        //recupera dados
        FirebaseUser usuarioPerfil = UserPFFirebase.getUsuatioAtual();
        editNomePerfil.setText(usuarioPerfil.getDisplayName());
        editEmailPerfil.setText(usuarioPerfil.getEmail());

        Uri url = usuarioPerfil.getPhotoUrl();
        if(url!= null){
            Picasso.get().load(url).into(imageEditarPerfil);
        }else{
            imageEditarPerfil.setImageResource(R.drawable.user);
        }


        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new SpotsDialog.Builder()
                        .setContext( perfilPf.this )
                        .setMessage("Atualizando dados")
                        .setCancelable( false )
                        .setTheme(R.style.Custom)
                        .build();
                dialog.show();

                String nomeAtualizado = editNomePerfil.getText().toString();

                if(!nomeAtualizado.isEmpty()){

                    UserPFFirebase.updateNomeUser( nomeAtualizado );
                    // atualiza no banco
                    userLogado.setNome( nomeAtualizado );
                    userLogado.atualizarPerfilPF();
                    Toast.makeText(perfilPf.this, "Nome Atualizado", Toast.LENGTH_SHORT).show();
                    voltarTelaPrincipal();

                }else{
                    dialog.dismiss();
                    Toast.makeText(perfilPf.this, "Preencha o campo", Toast.LENGTH_SHORT).show();

                }

            }
        });

        //alterar foto
        textAlterarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media .EXTERNAL_CONTENT_URI);
                if( i.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(i, SELECAO_GALERIA );
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Bitmap imagem = null;
            try{
                //Selecao da galeria
                switch ( requestCode ){
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada );
                        break;
                }

                if(imagem != null ){
                //configura img na tela
                    imageEditarPerfil.setImageBitmap(imagem);
                //recupera dados da imagem
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 90 , baos);
                    byte[] dadosImagem = baos.toByteArray();

                    // salvar img no firebase
                    StorageReference imagemRef = storageRef
                            .child("imagens")
                            .child("perfil")
                            .child(identificadorUser + ".jpeg") ;

                    UploadTask uploadTask = imagemRef.putBytes( dadosImagem );
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {


                            Toast.makeText(perfilPf.this,
                                    "Erro ao fazer o upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            //Recuperar local da foto
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uri.isComplete());
                            Uri url = uri.getResult();

                            atualizarFotoPF( url );

                            Toast.makeText(perfilPf.this,
                                    "Sucesso ao fazer o upload da imagem",
                                    Toast.LENGTH_SHORT).show();
                            Log.i("TesteImg",url.toString());
                        }
                    });
                }

        }catch (Exception e){
            e.printStackTrace();
            }
        }

    }

    private void atualizarFotoPF(Uri url){
        //atualizar foto no perfil
        UserPFFirebase.updatePhotoUser( url );
        //
        userLogado.setIdImg( url.toString() );
        userLogado.atualizarPerfilPF();
        Toast.makeText(this, "Foto atualizada", Toast.LENGTH_SHORT).show();
    }

    public void inicializarComponentesPF(){
        imageEditarPerfil = findViewById(R.id.editImagePerfilPF);
        textAlterarFoto = findViewById(R.id.editAlterarFotoPF);
        editNomePerfil = findViewById(R.id.editAlterarNomePF);
        editEmailPerfil = findViewById(R.id.textEmeilPF);
        buttonEdit = findViewById(R.id.buttonEdit);


    }


    public void voltarTelaPrincipal(){

        Intent intent = new Intent(perfilPf.this, Navigation_Drawer.class);
        startActivity(intent);

        dialog.dismiss();
        this.finish();
    }

}
