package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.infra.Sessao;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Muver;

public class PerfilFragmentActivity extends Fragment {
    private TextView boasVindas;
    private TextView anuncio;
    private TextView conversoes;
    private ImageView botaoEditarPerfil;
    private Muver muver = Sessao.instance.getMuver();

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

    private void setBoasVindas(View v) {
        boasVindas = v.findViewById(R.id.nomeMuver);
        String nome = muver.getNome();
        boasVindas.setText("Olá "+nome+"!"); //+nome do muver
    }

    private void setAnuncio(View v){
       anuncio = v.findViewById(R.id.anuncio);
       anuncio.setText("Seu anúncio é:"+  Sessao.instance.getCodigo()); //promocao que o muver está anunciando
    }

    private void setConversoes(View v){
        conversoes = v.findViewById(R.id.conversoes);
        conversoes.setText("Suas conversões: 0"); //conversoes do muver
    }

    private void irEditarPerfil(final View v) {
        botaoEditarPerfil = v.findViewById(R.id.editarPerfilButtton);
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
