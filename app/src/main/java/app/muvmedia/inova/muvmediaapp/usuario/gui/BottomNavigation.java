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
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.cupom.dominio.Toten;
import app.muvmedia.inova.muvmediaapp.infra.HttpConnection;
import app.muvmedia.inova.muvmediaapp.infra.ServicoDownload;
import app.muvmedia.inova.muvmediaapp.infra.Sessao;
import app.muvmedia.inova.muvmediaapp.mapa.gui.HomeFragmentActivity;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Sailor;


public class BottomNavigation extends AppCompatActivity {
    private Fragment selectedFragment = new HomeTeste();
    private int tela ;
    private boolean novoTotem = false;

    private static Location minhaLocalizacao2;
    private ArrayList<Toten> listaTotem = new ArrayList<>();
    private static List<Toten> listaDefinitiva = new ArrayList<>();
    private ArrayList<Toten> teste = new ArrayList<>();
    private Sailor sailor = Sessao.instance.getSailor();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        getLocalizacaoAparelho2();
        chamarMapaHome();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(navListener);

        atualizarLocalizacao();
        iniciarCont();

//        Bundle bundle = getIntent().getExtras();
//        teste = getIntent().getSerializableExtra("totens");
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
                            if (tela!= 3){
                                selectedFragment = new PerfilFragmentActivity();
                                getLocalizacaoAparelho2();
                                tela = 3;
//                            HomeTeste.setCont();
                            }
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
                handler.postDelayed(this, 8000);
                if (minhaLocalizacao2 != null) {
                    try {
                        if (isOnline()) {
                            enviarLocalizacao();
                            if (listaTotem.size() == 0){
                                listaDefinitiva = listaTotem;
                            }
                            else{
                                setListaDefinitiva();
                                notificacao();
                            }
                            Log.i("Lista Definitiva Size", String.valueOf(listaDefinitiva.size()));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Toast.makeText(BottomNavigation.this, "Erro inesperado", Toast.LENGTH_SHORT).show();
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
                            atualizarLocalizacao();
//                            Toast.makeText(getApplicationContext(), "Ative o GPS Porfavor", Toast.LENGTH_SHORT).show();
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
        if (isOnline()){
            try{
                callServerPostLocalizacao(minhaLocalizacao2);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(this, "Sem conexão com a internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void callServerPostLocalizacao(final Location location) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Sessao.instance.setResposta(HttpConnection.get("https://capitao-api.herokuapp.com/totens/nearby?lat="+ -8.017297 + "&lon=" + -34.944933));

//                //testando localização verdadeira
//                Sessao.instance.setResposta(HttpConnection.get("https://capitao-api.herokuapp.com/totens/nearby?lat="+ location.getLatitude() +"&lon=" + location.getLongitude()));

//                Type type = new TypeToken<List<Toten>>(){}.getType();
                Gson gson = new Gson();
                if (!Sessao.instance.getResposta().contains("location")) {
                    Log.i("Resposta Json vazio", Sessao.instance.getResposta());
                }
                else{
                    Log.i("Resposta Json correto", Sessao.instance.getResposta());
//                    listaTotem = gson.fromJson(Sessao.instance.getResposta(), List<Toten>.class);
                    listaTotem = new Gson().fromJson(Sessao.instance.getResposta(), new TypeToken<List<Toten>>(){}.getType());
                }
            }
        });
        thread.start();
        thread.join();

    }

    private boolean isOnline() {
        if(ServicoDownload.isNetworkAvailable(getApplicationContext())) {
            return true;
        }else{
            return false;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void notificacao(){
        if (isOnline() && novoTotem){
            enviarNotificacao();
            novoTotem=false;
        }
        Sessao.instance.setSailor(sailor);
    }

//    public void notinha(){
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Intent intent = new Intent(this, BottomNavigation.class);
//        Bundle bundle = new Bundle();
//        intent.setAction(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.putExtra("totens", listaTotem);
////        bundle.putSerializable("totens", listaTotem);
////        intent.putExtras(bundle);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setTicker("Olá Marujo!!!");
//        builder.setContentTitle("Capitão Cupom");
//        builder.setContentText("Um novo tesouro próximo de você");
//        builder.setSmallIcon(R.drawable.logo);
//        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
//        builder.setContentIntent(pendingIntent);
//
//
//        Notification n = builder.build();
//        n.vibrate = new long[]{150, 300, 150, 600};
//        n.flags = Notification.FLAG_AUTO_CANCEL;
//        notificationManager.notify(R.drawable.logo, n);
//
//        try {
//            Uri som  = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Ringtone toque = RingtoneManager.getRingtone(this, som);
//            toque.play();
//        }catch (Exception e){
//
//        }
//    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void enviarNotificacao(){
        Intent intent = new Intent(this, BottomNavigation.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.putExtra("totens", listaTotem);

        int id = (int) (Math.random()*1000);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("Olá Marujo!!!");
        builder.setContentTitle("Capitão Cupom");
        builder.setContentText("Um novo tesouro próximo de você");
        builder.setSmallIcon(R.drawable.logo);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
        builder.setContentIntent(pi);
        builder.setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(id, builder.build());

        Notification n = builder.build();
        n.vibrate = new long[]{150, 300, 150, 600};
//      n.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(R.drawable.logo, n);

        try {
            Uri som  = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this, som);
            toque.play();
        }catch (Exception e){

        }
        Sessao.instance.setSailor(sailor);
    }


    private void atualizarLocalizacao(){
        final Handler handler = new Handler();
        Log.i("Contador", "Iniciou contador");
        Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                handler.postDelayed(this, 5000);
                if (isOnline()){
                    if (minhaLocalizacao2 == null) {
                        getLocalizacaoAparelho2();
                    }
                    else{
                        Log.i("Contador", "Lat/Lon: " + minhaLocalizacao2.getLatitude() + " " + minhaLocalizacao2.getLongitude());
                    }
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }


    public static List<Toten> getTotens(){
        return listaDefinitiva;
    }

    private void setListaDefinitiva(){
        if (listaDefinitiva.size() == 0) {
            for (Toten toten : listaTotem) {
                listaDefinitiva.add(toten);
                novoTotem = true;
            }
        }
        else{
            boolean adicionou = false;
            ArrayList<Toten> provisoria = new ArrayList<Toten>();
            for (int i = 0; i < listaTotem.size(); i++) {
                for (int j = 0; j < listaDefinitiva.size(); j++) {
                    if (listaTotem.get(i).getName().equals(listaDefinitiva.get(j).getName())) {
                        provisoria.add(listaTotem.get(i));
                    }
                }
            }
            for (int i = 0; i < provisoria.size(); i++) {
                if (listaTotem.contains(provisoria.get(i))) {
                    listaTotem.remove(provisoria.get(i));
                }
            }
            for (Toten toten : listaTotem) {
                listaDefinitiva.add(toten);
                adicionou = true;
            }
            if (adicionou){
                novoTotem = true;
            }
        }
    }

    public static Location getMinhaLocalizacao(){
        return minhaLocalizacao2;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//TODO      Adicionar dialog de confirmação para sair
        Sessao.instance.setSailor(null);
    }
}
