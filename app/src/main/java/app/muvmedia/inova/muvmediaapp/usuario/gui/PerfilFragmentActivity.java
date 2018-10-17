package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Muver;

public class PerfilFragmentActivity extends Fragment {
    private TextView boasVindas;
    private TextView anuncio;
    private TextView conversoes;
    private Muver muver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil, container, false);
        setBoasVindas(v);
        setAnuncio(v);
        setConversoes(v);
        return v;
    }

    private void setBoasVindas(View v) {
        boasVindas = v.findViewById(R.id.textView);
        boasVindas.setText("Olá "); //+nome do muver
    }

    private void setAnuncio(View v){
        anuncio = v.findViewById(R.id.textView3);
        anuncio.setText("Seu anúncio é: "); //promocao que o muver está anunciando
    }

    private void setConversoes(View v){
        conversoes = v.findViewById(R.id.textView4);
        conversoes.setText("Suas conversoes: "); //conversoes do muver
    }

}
