package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.cupom.dominio.Toten;
import app.muvmedia.inova.muvmediaapp.infra.HttpConnection;
import app.muvmedia.inova.muvmediaapp.infra.ServicoDownload;
import app.muvmedia.inova.muvmediaapp.infra.Sessao;


public class BottomNavigation extends AppCompatActivity {
    private Fragment selectedFragment = new HomeTeste();
    private int tela ;
    private boolean enviar = true;



    private Location minhaLocalizacao2;
    private List<Toten> locaisToten = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        getLocalizacaoAparelho2();
        chamarMapaHome();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(navListener);

        if (isOnline()){
            atualizarLocalizacao();
            iniciarCont();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.navigation_home2:
//                            HomeTeste.setMinhaLocalizacao();
//                            HomeTeste.cont = false;
                            if (tela != 1){
                                HomeTeste homeTeste = new HomeTeste();
                                FragmentManager manager1 = getSupportFragmentManager();
                                manager1.beginTransaction().replace(R.id.fragment_container, homeTeste).commit();
                                getLocalizacaoAparelho2();
                                tela = 1;
                            }
                            else{Log.e("E","Fragmento já Aberto"); return false;}break;

                        case R.id.navigation_anunciar:
                            if (tela != 2) {
                                selectedFragment = new AnuncioFragmentActivity();
                                getLocalizacaoAparelho2();
                                tela = 2;
//                                HomeTeste.setCont();
                            }
                            break;
                        case R.id.navigation_perfil:
                            selectedFragment = new PerfilFragmentActivity();
                            getLocalizacaoAparelho2();
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
        getLocalizacaoAparelho2();
        tela = 1;
    }

    private void iniciarCont(){
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                handler.postDelayed(this, 60000);
                if (minhaLocalizacao2 != null) {
//                    Log.i("Resposta", minhaLocalizacao2.getLatitude() + " " + minhaLocalizacao2.getLongitude());
                    try {
                        enviarLocalizacao();

                        Log.i("ListaToten Size", String.valueOf(locaisToten.size()));
                        if (locaisToten.size() != 0){
                            Toast.makeText(BottomNavigation.this, String.valueOf(locaisToten.get(0).getLocation().get(0).getLatitude() + " "
                                    + locaisToten.get(0).getLocation().get(0).getLongitude()), Toast.LENGTH_SHORT).show();
                        }


                    } catch (InterruptedException e) {
                        Toast.makeText(BottomNavigation.this, "Erro inesperado", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }


    public void getLocalizacaoAparelho2(){
        FusedLocationProviderClient provedorLocalizacaoCliente;

        provedorLocalizacaoCliente = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        try{
                final Task location = provedorLocalizacaoCliente.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation == null){
                                Toast.makeText(getApplicationContext(), "Ative o GPS Porfavor", Toast.LENGTH_SHORT).show();
                                Log.e("E","Cliente desligou GPS desligado");
                            }
                            else{
                                minhaLocalizacao2 = currentLocation;
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Não podemos encontrar sua localização atual", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        }catch (SecurityException e){
            e.getMessage();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void enviarLocalizacao() throws InterruptedException {
        callServerPostLocalizacao(minhaLocalizacao2);

        if (isOnline()){
//                enviarNotificacao();
//                notinha();
//                receber();
        }
//        enviar=false;
    }

    private void callServerPostLocalizacao(final Location location) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //testando localização falsa
//                Sessao.instance.setResposta(HttpConnection.get("https://capitao-api.herokuapp.com/totens/nearby?lat="+ -8.058969 + "&lon=" + -34.935671));

                //testando localização verdadeira
                Sessao.instance.setResposta(HttpConnection.get("https://capitao-api.herokuapp.com/totens/nearby?lat="+ location.getLatitude() +"&lon=" + location.getLongitude()));

                Type type = new TypeToken<List<Toten>>(){}.getType();
                Gson gson = new Gson();
                if (!Sessao.instance.getResposta().contains("location")) {
                    Log.i("Resposta Json vazio", Sessao.instance.getResposta());
                }
                else{
                    Log.i("Resposta Json correto", Sessao.instance.getResposta());
                    locaisToten = gson.fromJson(Sessao.instance.getResposta(), type);
                    Toast.makeText(BottomNavigation.this, String.valueOf(locaisToten.get(0)), Toast.LENGTH_SHORT).show();
                }
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
        notification.flags = Notification.FLAG_AUTO_CANCEL;
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
//        Sessao.instance.setCodigo("");
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




    private void atualizarLocalizacao(){
        final Handler handler = new Handler();
        Log.i("Contador", "Iniciou contador");
        Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                handler.postDelayed(this, 5000);
                if (minhaLocalizacao2 != null) {
                    getLocalizacaoAparelho2();
                    Log.i("Contador", "Lat/Lon: " + minhaLocalizacao2.getLatitude() + " " + minhaLocalizacao2.getLongitude());
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }





}
