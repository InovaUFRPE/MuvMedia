package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.zxing.Result;

import app.muvmedia.inova.muvmediaapp.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class AnuncioFragmentActivity extends Fragment  {
    private Button anunciarButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_anunciar, container, false);
        setItensView(v);
        return v;

    }

    private void setItensView(View v) {
        anunciarButton = v.findViewById(R.id.escanear);
        anunciarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplication(),QrCodeActivity.class);
                startActivity(i);
            }
        });

    }


}
