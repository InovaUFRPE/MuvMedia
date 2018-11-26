package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.Toast;
import com.google.gson.Gson;
import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.infra.ServicoDownload;
import app.muvmedia.inova.muvmediaapp.infra.Sessao;


public class BottomNavigation extends AppCompatActivity {
    private Fragment selectedFragment = new HomeTeste();
    private int tela ;
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
//                                HomeTeste.setCont();
                            }
                            break;
                        case R.id.navigation_perfil:
                            selectedFragment = new PerfilFragmentActivity();
//                            HomeTeste.setCont();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

    private void chamarMapaHome(){
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
                handler.postDelayed(this, 10000);
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
//                notinha();
                receber();
            }
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
        intent.putExtra(String.valueOf(R.drawable.logo), "Um novo tesouro próximo de você");
        int id = (int) (Math.random()*1000);
        PendingIntent pi = PendingIntent.getActivity(BottomNavigation.this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(BottomNavigation.this)
                .setContentTitle("Capitão Cupom")
                .setContentText("Um novo tesouro próximo de você")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                .setContentIntent(pi).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification.vibrate = new long[]{150, 300, 150, 600};
//        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(id, notification);


        try {
            Uri som  = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this, som);
            toque.play();
        }catch (Exception e){

        }
//        finish();
    }

    private void receber(){
        getIntent().getStringExtra(String.valueOf(R.drawable.logo));
        Sessao.instance.setCodigo("");
    }


    private boolean isOnline() {
        if(ServicoDownload.isNetworkAvailable(getApplicationContext())) {
            return true;
        }else{
            return false;
        }
    }


    public void notinha(){

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(BottomNavigation.this, BottomNavigation.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("Textinho");
        builder.setContentTitle("Capitão Cupom");
        builder.setContentText("Um novo tesouro próximo de você");
        builder.setSmallIcon(R.drawable.logo);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
        builder.setContentIntent(pendingIntent);

        Notification n = builder.build();
        n.vibrate = new long[]{150, 300, 150, 600};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(R.drawable.logo, n);

        try {
            Uri som  = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this, som);
            toque.play();
        }catch (Exception e){

        }
    }

}
