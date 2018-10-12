package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import app.muvmedia.inova.muvmediaapp.infra.Permissoes;
import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.infra.ServicoDownload;

public class SplashActivity extends Activity implements Runnable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        isOnline();
        String permissions[] = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
        boolean validate = Permissoes.validate(this, 0, permissions);
        if (validate) {
            Handler handler = new Handler();
            handler.postDelayed(this, 5000);
        }
    }
    public void run() {
        startActivity(new Intent(this, LoginActivity.class));

        finish();
    }

    private void isOnline() {
        if(ServicoDownload.isNetworkAvailable(getApplicationContext()))
        {
            Toast.makeText(getApplicationContext(), "ONLINE", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "Sem conexão com a internet", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults){
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "É preciso autorizar as permissões para utilizar o app", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
        run();
    }
}
