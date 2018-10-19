package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.infra.Sessao;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Muver;

public class PerfilFragmentActivity extends Fragment {
    private TextView boasVindas;
    private TextView anuncio;
    private TextView conversoes;
    private Button botaoEditarPerfil;
    private Muver muver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);
        irEditarPerfil(v);
        setBoasVindas(v);
        setAnuncio(v);
        setConversoes(v);
        return v;
    }

    private void setMuver() {
        Gson gson = new Gson();
        String teste = Sessao.instance.getResposta();
        Muver muver = gson.fromJson(Sessao.instance.getResposta(), Muver.class);//fromJson(muverString, Muver.class);
        Sessao.instance.setMuver(muver);
    }


    private void setBoasVindas(View v) {
        boasVindas = v.findViewById(R.id.textView);
      //  String nome = muver.getNome();
      //  boasVindas.setText(nome); //+nome do muver
    }

    private void setAnuncio(View v){
        anuncio = v.findViewById(R.id.textView3);
        anuncio.setText("Seu anúncio é: "); //promocao que o muver está anunciando
    }

    private void setConversoes(View v){
        conversoes = v.findViewById(R.id.textView4);
        conversoes.setText("Suas conversoes: "); //conversoes do muver
    }

    private void irEditarPerfil(final View v) {
        botaoEditarPerfil = v.findViewById(R.id.button5);
        botaoEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplication(), EditarPerfilActivity.class);
                startActivity(intent);
            }
        });
    }

//    private void salvarPerfil(){
//        botaoSalvar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity().getApplication(), "Salvo", Toast.LENGTH_SHORT);
//            }
//        });
//    }

}
