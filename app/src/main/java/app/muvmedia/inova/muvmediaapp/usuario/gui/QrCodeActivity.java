package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.infra.Sessao;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class QrCodeActivity extends AppCompatActivity  implements ZXingScannerView.ResultHandler{
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(checkPermission()){
                Toast.makeText(QrCodeActivity.this, "Permissões concedidas", Toast.LENGTH_LONG).show();

            }else{
                requestPermission();
            }
        }
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(QrCodeActivity.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void  onRequestPermissionsResult(int requestCode, String permission[], int grantResults[]){
        switch (requestCode){
            case REQUEST_CAMERA:
                if(grantResults.length >0){
                    boolean cameraAceita = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAceita){
                        Toast.makeText(QrCodeActivity.this, "Permissão concedida", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(QrCodeActivity.this, "Permissão não concedida", Toast.LENGTH_LONG).show();
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if(shouldShowRequestPermissionRationale(CAMERA)) {
                                displayAlertMenssage("Você tem que aceitar todas as permissões",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                                    requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                                }

                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkPermission()){
                if(scannerView == null){
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(QrCodeActivity.this);
                scannerView.startCamera();

            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();


    }

    public void displayAlertMenssage(String mensagem, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(QrCodeActivity.this)
                .setMessage(mensagem)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancelar", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(final Result result) {
        final String meuResultado = result.getText();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Anúncio Gerado");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Sessao.instance.setCodigo(meuResultado);
                Intent intent = new Intent(getApplicationContext(), BottomNavigation.class);
                startActivity(intent);
            }
        });
        builder.setMessage("Vá até a tela perfil para ver seu código");
        AlertDialog alert = builder.create();
        alert.show();
    }
}
