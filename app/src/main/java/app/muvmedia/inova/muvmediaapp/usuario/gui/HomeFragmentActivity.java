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

public class HomeFragmentActivity extends Fragment {

    TextView texto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        setTexto(v);

        return v;
    }

    private void setTexto(View v){
        texto = v.findViewById(R.id.textoHome);
        texto.setText("Home");
    }
}
