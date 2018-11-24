package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import app.muvmedia.inova.muvmediaapp.R;
import app.muvmedia.inova.muvmediaapp.mapa.gui.HomeFragmentActivity;

public class BottomNavigation extends AppCompatActivity {
    private Fragment selectedFragment = new HomeTeste();
    private int tela ;
//    private Fragment selectedFragment = new HomeFragmentActivity();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        chamarMapaHome();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(navListener);


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
                            }
                            break;
                        case R.id.navigation_perfil:
                            selectedFragment = new PerfilFragmentActivity();
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

}
