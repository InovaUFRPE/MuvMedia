package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.infra.HttpConnection;
import app.muvmedia.inova.muvmediaapp.infra.ServicoDownload;
import app.muvmedia.inova.muvmediaapp.infra.Sessao;
import app.muvmedia.inova.muvmediaapp.usuario.dominio.Sailor;

import static android.content.Context.NOTIFICATION_SERVICE;

public class HomeTeste extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private boolean permitirLocalizacao = false;
    private static final int CODIGO_PERMISSAO_SOLICITACAO_RESULTADO = 1234;
    private GoogleMap mMap;
    private FusedLocationProviderClient provedorLocalizacaoCliente;
    private static final float ZOOM = 17f;

    private AutoCompleteTextView buscaMapa;
    private GoogleApiClient googleApiClient;
    private ImageView gpsMapa;

    private static boolean cont;
    private static Location minhaLocalizacao;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        buscaMapa = v.findViewById(R.id.editBuscaMapa);
        gpsMapa = v.findViewById(R.id.ic_gps);
//        iniciarCont();
//        receberNotificacao();
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPermissaoLocalizacao();
    }

    private void buscarMapa(){
        buscaMapa.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER){
                    geoLocalizacao();
                }
                return false;
            }
        });

        gpsMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocalizacaoAparelho();
//                ArrayList<Float>  meuLocal= calcularMinhaDistancia(minhaLocalizacao);
//                Toast.makeText(getActivity(),
//                        String.valueOf(meuLocal.get(0)) + " " +
//                        String.valueOf(meuLocal.get(1)) + " " +
//                        String.valueOf(meuLocal.get(2)) + "\n" +
//                        String.valueOf(meuLocal.get(3)) + " " +
//                        String.valueOf(meuLocal.get(4)) + " " +
//                        String.valueOf(meuLocal.get(5)), Toast.LENGTH_LONG).show();
            }
        });
    }

//    private void iniciarCont(){
//        if (!cont) {
//            final Handler handler = new Handler();
//            Log.i("Contador", "Iniciou contador");
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
////                Toast.makeText(getContext(), "5 segundos", Toast.LENGTH_SHORT).show();
//                    handler.postDelayed(this, 5000);
//                    if (minhaLocalizacao != null) {
//                        Log.i("Contador", "Lat/Lon: " + minhaLocalizacao.getLatitude() + " " + minhaLocalizacao.getLongitude());
//                    }
//                }
//            };
//            handler.postDelayed(runnable, 0);
//        }
//    }


    private void geoLocalizacao(){
        String busca = buscaMapa.getText().toString();
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> lista = new ArrayList<>();
        try{
            lista = geocoder.getFromLocationName(busca, 1);
        }catch (IOException e){
            e.getMessage();
        }
        if (lista.size() > 0){
            Address endereco = lista.get(0);
            moverCamera(new LatLng(endereco.getLatitude(), endereco.getLongitude()), ZOOM, endereco.getAddressLine(0));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (permitirLocalizacao) {
            getLocalizacaoAparelho();
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //setar minha localização
            mMap.setMyLocationEnabled(true);
            //desativar o botão de localização do google maps para não ficar atrás da barra de pesquisa
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            buscarMapa();
            setRaioQr(mMap);
        }
    }


    private void iniciarMapa(){
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapinha1);
        mapFragment.getMapAsync(HomeTeste.this);
    }


    private void getLocalizacaoAparelho(){
        provedorLocalizacaoCliente = LocationServices.getFusedLocationProviderClient(getActivity());
        try{
            if (permitirLocalizacao){
                final Task location = provedorLocalizacaoCliente.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation == null){
                                Toast.makeText(getContext(), "Ative o GPS Porfavor", Toast.LENGTH_SHORT).show();
                                Log.e("E","Cliente desligou GPS desligado");
                            }
                            else{
                                local(currentLocation);
                                moverCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), ZOOM, "Estou aqui");
                                Log.i("Contador","Lat/Lon: " + currentLocation.getLatitude() + " " + currentLocation.getLongitude());
                            }
                        }
                        else {
                            Toast.makeText(getContext(), "Não podemos encontrar sua localização atual", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

        }catch (SecurityException e){
            e.getMessage();
        }
    }

    private Location local(Location location){
        minhaLocalizacao = location;
        return minhaLocalizacao;
    }

    private void moverCamera(LatLng latLng, float zoom, String titulo){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if (!titulo.equals("Estou aqui")){
            MarkerOptions marker = new MarkerOptions().position(latLng).title(titulo);
            mMap.addMarker(marker);
        }
    }

    private void getPermissaoLocalizacao(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(getContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (ContextCompat.checkSelfPermission(getContext(),
                    COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                permitirLocalizacao = true;
                iniciarMapa();
            } else{
                ActivityCompat.requestPermissions(getActivity(), permissions, CODIGO_PERMISSAO_SOLICITACAO_RESULTADO);
            }
        } else{
            ActivityCompat.requestPermissions(getActivity(), permissions, CODIGO_PERMISSAO_SOLICITACAO_RESULTADO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permitirLocalizacao = false;

        switch (requestCode){
            case CODIGO_PERMISSAO_SOLICITACAO_RESULTADO:{
                if (grantResults.length > 0){
                    for (int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                            permitirLocalizacao = false;
                            return;
                        }
                    }
                    permitirLocalizacao = true;
                    iniciarMapa();
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void setRaioQr(GoogleMap mMap){
        ArrayList<LatLng> locais = new ArrayList<LatLng>();

        locais.add(new LatLng(-8.064054, -34.937031));
        locais.add(new LatLng(-8.061719, -34.934452));
        locais.add(new LatLng(-8.017752, -34.944756));
        for (LatLng local : locais){
            mMap.addCircle(new CircleOptions()
                    .center(local)
                    .radius(150)
                    .strokeColor(Color.RED)
                    .fillColor(Color.TRANSPARENT));
            mMap.addMarker(new MarkerOptions().position(local).icon(BitmapDescriptorFactory.fromResource(R.drawable.treasuse)));
        }

    }

//    private ArrayList<Float> calcularMinhaDistancia(Location minhaLocalizacao){
//        ArrayList<Float> meuLocal = new ArrayList<Float>();
//        float minhaLatitude = (float) minhaLocalizacao.getLatitude();
//        float meuGrauLat;
//        float meuMinutoLat;
//        float meuMinutoTempLat;
//        float meuSegundoLat;
//        float minhaLongitude = (float) minhaLocalizacao.getLongitude();
//        float meuGrauLong;
//        float meuMinutoLong;
//        float meuMinutoTempLong;
//        float meuSegundoLong;
//
//        meuGrauLat = (int) minhaLatitude;
//        meuMinutoLat = (int)((minhaLatitude - meuGrauLat)*60);
//        meuMinutoTempLat = (minhaLatitude - meuGrauLat)*60;
//        meuSegundoLat = (int)((meuMinutoTempLat - meuMinutoLat)*60);
//
//        meuGrauLong = (int) minhaLongitude;
//        meuMinutoLong = (int) ((minhaLongitude - meuGrauLong)*60);
//        meuMinutoTempLong = (minhaLongitude - meuGrauLong)*60;
//        meuSegundoLong = (int) ((meuMinutoTempLong - meuMinutoLong)*60);
//
//
//        meuLocal.add(meuGrauLat);
//        meuLocal.add(meuMinutoLat);
//        meuLocal.add(meuSegundoLat);
//        meuLocal.add(meuGrauLong);
//        meuLocal.add(meuMinutoLong);
//        meuLocal.add(meuSegundoLong);
//
//        return meuLocal;
//    }


    public static void setCont(){
        cont = true;
    }

    public static void setMinhaLocalizacao(){
        minhaLocalizacao=null;
    }
    public static Location getMinhaLocalizacao(){
        return minhaLocalizacao;
    }
}