package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.Toast;

import com.google.gson.Gson;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.infra.HttpConnection;
import app.muvmedia.inova.muvmediaapp.infra.ServicoDownload;
import app.muvmedia.inova.muvmediaapp.infra.Sessao;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.AppSession;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Sailor;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Session;

public class BottomNavigation extends AppCompatActivity {
    private Fragment selectedFragment = new HomeTeste();
    private int tela ;
    //    private Fragment selectedFragment = new HomeFragmentActivity();
    private Chronometer contador;
    private boolean enviar = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        contador = (Chronometer) findViewById(R.id.contador);
        crono();
        chamarMapaHome();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(navListener);

        if (isOnline()){
            iniciarCont();
        }
    }

    private void crono(){
        contador.setBase(SystemClock.elapsedRealtime());
        contador.start();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.navigation_home2:
//                                HomeFragmentActivity homeFragmentActivity = new HomeFragmentActivity();
//                                FragmentManager manager1 = getSupportFragmentManager();
//                                manager1.beginTransaction().replace(R.id.fragment_container, homeFragmentActivity).commit();
                            HomeTeste.setMinhaLocalizacao();
//                            HomeTeste.cont = false;
                            if (tela != 1){
                                HomeTeste homeTeste = new HomeTeste();
                                FragmentManager manager1 = getSupportFragmentManager();
                                manager1.beginTransaction().replace(R.id.fragment_container, homeTeste).commit();
                                tela = 1;
                            }
                            else{Log.e("E","Fragmento já Aberto"); return false;}break;

                        case R.id.navigation_anunciar:
                            if (tela != 2) {
                                selectedFragment = new AnuncioFragmentActivity();
                                tela = 2;
                                HomeTeste.setCont();
                            }
                            break;
                        case R.id.navigation_perfil:
                            selectedFragment = new PerfilFragmentActivity();
                            HomeTeste.setCont();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };



    private void chamarMapaHome(){
//        HomeFragmentActivity homeFragmentActivity = new HomeFragmentActivity();
//        FragmentManager manager1 = getSupportFragmentManager();
//        manager1.beginTransaction().replace(R.id.fragment_container, homeFragmentActivity).commit();

        HomeTeste homeTeste = new HomeTeste();
        FragmentManager manager1 = getSupportFragmentManager();
        manager1.beginTransaction().replace(R.id.fragment_container, homeTeste).commit();
        tela = 1;
    }

    private void iniciarCont(){
        final Handler handler = new Handler();
        Log.i("Contador", "Iniciou contador");
        Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                handler.postDelayed(this, 40000);
                if (HomeTeste.getMinhaLocalizacao() != null) {
                    try {
                        enviarLocalizacao();
                    } catch (InterruptedException e) {
                        Toast.makeText(BottomNavigation.this, "Erro inesperado", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    Log.i("Contador", "Lat/Lon: " + HomeTeste.getMinhaLocalizacao().getLatitude() + " " + HomeTeste.getMinhaLocalizacao().getLongitude());
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void enviarLocalizacao() throws InterruptedException {
        Gson gson = new Gson();
//        String idSailor = gson.toJson(Sessao.instance.getSailor().get_id());
//        String location = gson.toJson(HomeTeste.getMinhaLocalizacao());
        callServerPostLocalizacao();

        if (Sessao.instance.getResposta().contains("Sucess")){
            if (enviar){
                enviarNotificacao();
                receber();
            }
//            Toast.makeText(this, "Notificação Sucesso", Toast.LENGTH_SHORT).show();
            //Chama notificação;
        }
        enviar=false;
    }

//    private void callServerPostLocalizacao(final String location, final String idSailor) throws InterruptedException {
        private void callServerPostLocalizacao() throws InterruptedException {

            Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Sessao.instance.setResposta("Haha Sucess");
//                Sessao.instance.setResposta(HttpConnection.post("https://capitao-api.herokuapp.com/public/register",location, idSailor));//Setar URL
            }
        });
        thread.start();
        thread.join();

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void enviarNotificacao(){
        Intent intent = new Intent(BottomNavigation.this, BottomNavigation.class);
        intent.putExtra(Sessao.instance.getSailor().get_id(), "Um novo tesouro próximo de você");
        int id = (int) (Math.random()*1000);
        PendingIntent pi = PendingIntent.getActivity(BottomNavigation.this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(BottomNavigation.this)
                .setContentTitle("Capitão Cupom")
                .setContentText("Um novo tesouro próximo de você").setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pi).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(id, notification);
//        finish();
    }

    private void receber(){
        getIntent().getStringExtra(Sessao.instance.getSailor().get_id());
        Sessao.instance.setCodigo("");
//        try {
//            getSessaoApi();
//        } catch (InterruptedException e) {
//            Toast.makeText(this, "Erro de Notificação", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
    }


    private void getSessaoApi() throws InterruptedException {
        Gson gson = new Gson();
        AppSession sessionApi = gson.fromJson(Sessao.instance.getResposta(), AppSession.class);
        Session session = sessionApi.getSession();
        Sessao.instance.setSession(sessionApi.getSession());
        Sessao.instance.setSailor(sessionApi.getSailor());
    }




    private boolean isOnline() {
        if(ServicoDownload.isNetworkAvailable(getApplicationContext())) {
            return true;
        }else{
            return false;
        }
    }

}
