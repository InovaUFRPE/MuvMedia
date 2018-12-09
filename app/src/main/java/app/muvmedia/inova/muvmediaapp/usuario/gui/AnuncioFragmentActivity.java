package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.Result;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.cupom.dominio.Campaign;
import app.muvmedia.inova.muvmediaapp.infra.Sessao;
import app.muvmedia.inova.muvmediaapp.usuario.servico.ServicoHttpMuver;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
                escanearCodigo();

//                Intent i = new Intent(getActivity().getApplication(),QrCodeActivity.class);
//                startActivity(i);
            }
        });

    }

    public void escanearCodigo(){
        IntentIntegrator.forSupportFragment(AnuncioFragmentActivity.this)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                .setBeepEnabled(false)
                .setOrientationLocked(false)
                .initiateScan();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult resultData = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (resultData != null && resultCode == Activity.RESULT_OK) {
            String scanContent = resultData.getContents();
            String scanFormat = resultData.getFormatName();
            try {
                callCampanha(scanContent);
                getResposta();
            } catch (InterruptedException e) {
                Toast.makeText(getContext(), "Erro Inesperado", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(getContext(), scanContent, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(), "Erro Inesperado", Toast.LENGTH_LONG).show();
        }
    }



    public void callCampanha(final String mensagem) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ServicoHttpMuver servicoHttpMuver = new ServicoHttpMuver();
                try {
                    Campaign campaign = servicoHttpMuver.getCampanha(mensagem);
//                    Sessao.instance.getSailor().setCampaigns(campaign);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Sessao.instance.setResposta(usuarioEditado);
            }
        });
        thread.start();
        thread.join();


    }

    public void getResposta() {
        String mensagem = "Hoje não temos nada para você!";
        if (Sessao.instance.getResposta().equals("200")) {
            mensagem = "YOOOOOOOOO, PARABÉNS MARUJO VOCÊ RECEBEU UM NOME CUPOM... VÁ ATÉ SEU PERFIL E APROVEITE HOHOHOHO";
        } else {
            mensagem = "ARRRRGHHHH, TA ACHANDO QUE SOU BOBO? VOLTE AMANHÃ!!!";

        }
        Toast.makeText(getContext(), mensagem , Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getContext(), BottomNavigation.class);
        startActivity(intent);
    }

}
