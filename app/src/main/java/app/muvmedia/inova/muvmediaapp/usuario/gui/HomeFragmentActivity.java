package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.cupom.dominio.Toten;


public class HomeFragmentActivity extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private boolean permitirLocalizacao = false;
    private static final int CODIGO_PERMISSAO_SOLICITACAO_RESULTADO = 1234;
    private GoogleMap mMap;
    private FusedLocationProviderClient provedorLocalizacaoCliente;
    private static final float ZOOM = 15f;
    private ImageView gpsMapa;

    private static boolean cont;
    private Location minhaLocalizacao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        gpsMapa = v.findViewById(R.id.ic_gps);
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

    private void gps(){
        gpsMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocalizacaoAparelho();
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style_json));
        if (permitirLocalizacao) {
            getLocalizacaoAparelho();
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            gps();
        }
    }

    private void grauCamera(LatLng latLng, GoogleMap mMap){
        CameraPosition position = CameraPosition.builder()
                .target(latLng)
                .zoom( ZOOM )
                .bearing( 0.0f )
                .tilt( 90f )
                .build();

        mMap.animateCamera(
                CameraUpdateFactory.newCameraPosition( position ), null );
    }


    private void iniciarMapa(){
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapinha1);
        mapFragment.getMapAsync(HomeFragmentActivity.this);
    }


    public void getLocalizacaoAparelho(){
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
                                minhaLocalizacao = BottomNavigation.getMinhaLocalizacao();
                                Log.i("ContGPS", "GPS ligado mas dando erro");
//                                Log.e("E","Cliente desligou GPS desligado");
                            }
                            else{
                                Log.i("Susesso GPS", "GPS Sucesso");
                                minhaLocalizacao = currentLocation;
                                grauCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), mMap);
//                                moverCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), ZOOM, "Estou aqui");
                                chamarTesouros(mMap);
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

    public static void setCont(){
        cont = true;
    }

    private void setTotensMap(GoogleMap mMap){
        if (BottomNavigation.getTotens().size() != 0){
            List<Toten> totens = BottomNavigation.getTotens();
            mMap.clear();
            for (int i = 0; i < totens.size(); i++){
                LatLng localToten = new LatLng(totens.get(i).getLocation().getLongitude(), totens.get(i).getLocation().getLatitude());
                mMap.addMarker(new MarkerOptions().position(localToten).title(totens.get(i).getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.treasuse)));
                Log.i("Totem", String.valueOf(localToten.latitude + " " + localToten.longitude));
            }
        }
        else{
            mMap.clear();
        }
    }

    private void chamarTesouros(final GoogleMap mapa){
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                handler.postDelayed(this, 11000);
                setTotensMap(mapa);
            }
        };
        handler.postDelayed(runnable, 0);
    }

}