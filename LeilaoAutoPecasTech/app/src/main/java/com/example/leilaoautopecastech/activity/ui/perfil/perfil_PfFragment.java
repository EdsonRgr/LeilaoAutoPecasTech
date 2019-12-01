package com.example.leilaoautopecastech.activity.ui.perfil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.leilaoautopecastech.R;
import com.example.leilaoautopecastech.helper.UserFirebase;
import com.example.leilaoautopecastech.model.PessoaFisica;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;


public class perfil_PfFragment extends Fragment {

    private CircleImageView imageEditarPerfil;
    private TextView textAlterarFoto;
    private TextInputEditText editNomePerfil, editEmailPerfil;
    private Button buttonEdit;
    private PessoaFisica usuarioLogado;
    private String teste = "Edson Roger";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_perfil, container, false);
        //
            usuarioLogado = UserFirebase.getDadodsUsuarioLogado();


        //
        imageEditarPerfil = root.findViewById(R.id.editImagePerfil);
        textAlterarFoto = root.findViewById(R.id.editAlterarFoto);
        editNomePerfil = root.findViewById(R.id.editAlterarNome);
        editEmailPerfil = root.findViewById(R.id.editAlterarEmail);
        buttonEdit = root.findViewById(R.id.buttonEdit);
        editEmailPerfil.setFocusable(false);

        //recuperar dados
        FirebaseUser usuarioPerfil = UserFirebase.getUsuatioAtual();
        editNomePerfil.setText( usuarioPerfil.getDisplayName());
        editEmailPerfil.setText( usuarioPerfil.getEmail());
        if( usuarioPerfil.getDisplayName() == teste){

            Toast.makeText(getContext(), "deu certo", Toast.LENGTH_SHORT).show();


        }else{
            Toast.makeText(getContext(), "nada ver", Toast.LENGTH_SHORT).show();

        }

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nomeAtualizado = editNomePerfil.getText().toString();
                // atualiza o nome
                UserFirebase.updateNomeUser( nomeAtualizado );

                //atualiza no banco
                usuarioLogado.setNome( nomeAtualizado );
                usuarioLogado.salvarPessoaFisica();

                Toast.makeText(getContext(), "Alterações salvas", Toast.LENGTH_SHORT).show();

            }
        });





        return root;
    }

    public void inicializarComponentes(){



    }


}