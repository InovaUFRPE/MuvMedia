package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import app.muvmedia.inova.muvmediaapp.infra.HttpConnection;
import app.muvmedia.inova.muvmediaapp.infra.MuvMediaException;
import app.muvmedia.inova.muvmediaapp.infra.Permissoes;
import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.infra.ServicoDownload;
import app.muvmedia.inova.muvmediaapp.infra.Sessao;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.AppSession;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Session;

public class SplashActivity extends Activity implements Runnable {
    ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progress = findViewById(R.id.loader);
        progress.setVisibility(View.VISIBLE);
        isOnline();
        String permissions[] = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
        boolean validate = Permissoes.validate(this, 0, permissions);
        if (validate) {
            Handler handler = new Handler();
            handler.postDelayed(this, 2000);
        }
    }
    public void run() {
        if(Sessao.instance.getSailor() == null) {
            SharedPreferences sharedPreferences = getSharedPreferences("sessao",
                    Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("token", null);
            System.out.println(token);
            if (token != null && !token.equals("null")) {
                relogar(token);
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }

            finish();
            //Toast.makeText(getApplicationContext(), "Usuario null", Toast.LENGTH_SHORT).show();
        }
    }

    private void relogar(String token) {
        token = "Bearer " + token;
        try {
            signin(token);
            if(Sessao.instance.getResposta().contains("Usuário ou senha incorreto")){
                progress.setVisibility(View.GONE);
                Toast.makeText(this, "Usuário ou senha incorreto", Toast.LENGTH_SHORT).show();
            } else {
                getSessaoApi();

                Intent intent = new Intent(getApplicationContext(), BottomNavigation.class);
                Sessao.instance.setCodigo("");
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void isOnline() {
        if(ServicoDownload.isNetworkAvailable(getApplicationContext()))
        {
            if (Sessao.instance.getSailor() != null){
                Intent intent = new Intent(getApplicationContext(), BottomNavigation.class);
                startActivity(intent);
                finish();
//                Toast.makeText(getApplicationContext(), Sessao.instance.getMuver().getUsuario().getEmail(), Toast.LENGTH_LONG).show();
            }
//            Toast.makeText(getApplicationContext(), "ONLINE", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "Sem conexão com a internet", Toast.LENGTH_LONG).show();
        }
    }

    private void getSessaoApi() throws InterruptedException {
        Gson gson = new Gson();
        AppSession sessionApi = gson.fromJson(Sessao.instance.getResposta(), AppSession.class);
        Session session = sessionApi.getSession();
        Sessao.instance.setSession(sessionApi.getSession());
        Sessao.instance.setSailor(sessionApi.getSailor());

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("sessao",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("token", session.getToken());
            editor.apply();
        } catch (Error e) {
            System.out.print(e);
        }

    }

    private void signin(String token) throws InterruptedException, MuvMediaException {
        String json = "{\"token\": \"" + token + "\"}";
        callServerSignin(json);
    }

    private void callServerSignin(final String data) throws MuvMediaException, InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Sessao.instance.setResposta(HttpConnection.post("https://capitao-api.herokuapp.com/auth/app/signin", data));
                    Log.i("Script", "OLHAAA: "+ Sessao.instance.getResposta());
                }catch (Exception e){
                    try {
                        throw new MuvMediaException("Conexão interrompida");
                    } catch (MuvMediaException e1) {
                        e1.printStackTrace();
                    }
                }


            }
        });
        thread.start();
        thread.join();

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
//        run();
    }

//    private void verificaLogado(){
//        if (Sessao.instance.getMuver() != null){
//            Sessao.instance.getMuver().getUsuario().getEmail();
//            Sessao.instance.getMuver().getUsuario().getPassword();
//            Intent intent = new Intent(getApplicationContext(), BottomNavigation.class);
//            startActivity(intent);
//            finish();
//            Toast.makeText(getApplicationContext(), "LOGOOOU", Toast.LENGTH_LONG).show();
//        }
//        else {
//            startActivity(new Intent(this, LoginActivity.class));
//            finish();
//            Toast.makeText(getApplicationContext(), "Usuario null", Toast.LENGTH_SHORT).show();
//        }
//    }
}
