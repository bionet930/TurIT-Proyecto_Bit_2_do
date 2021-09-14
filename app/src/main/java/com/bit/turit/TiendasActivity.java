package com.bit.turit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class TiendasActivity extends AppCompatActivity {

    //Variables
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiendas);

        /*----------findViewById----------*/
        bottomNavigationView = findViewById(R.id.bottom_NavigationView);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_NavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent0 = new Intent(TiendasActivity.this, MainActivity.class);
                        startActivity(intent0);
                        break;
                    case R.id.ticket:
                        Intent intent1 = new Intent(TiendasActivity.this, TicketActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.agregar:
                        Intent intent2 = new Intent(TiendasActivity.this, AgregarActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.lugares:
                        Intent intent3 = new Intent(TiendasActivity.this, LugaresActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.tiendas:

                        break;
                }
                return false;
            }
        });

    }

    public void irHotelEspañol(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hotelespanolsalto.com/"));
        startActivity(i);
    }

    public void irRestaurante(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/dolcezzahelado/"));
        startActivity(i);
    }

    public void irShopping(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.pimenton.com.uy/tiendas/salto_16"));
        startActivity(i);
    }
}
