package app.muvmedia.inova.muvmediaapp.usuario.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import app.muvmedia.inova.muvmediaapp.R;

public class BottomNavigation extends AppCompatActivity {
    Fragment selectedFragment = new HomeFragmentActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(navListener);
    }

        private BottomNavigationView.OnNavigationItemSelectedListener navListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()){
                            case R.id.navigation_home2:
                                selectedFragment = new HomeFragmentActivity();
                                break;
                            case R.id.navigation_anunciar:
                                selectedFragment = new AnuncioFragmentActivity();
                                break;
                            case R.id.navigation_perfil:
                                selectedFragment = new PerfilFragmentActivity();
                                break;
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                        return true;
                    }
                };


    private void setFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

}
