package com.bit.turit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.util.ArrayList;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

public class TicketActivity extends AppCompatActivity  {

    //Variables
    BottomNavigationView bottomNavigationView;
    ArrayList<Ticket> tickets;
    private RecyclerView listaTickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        /*----------Bottom Navigation Bar----------*/
        bottomNavigationView = findViewById(R.id.bottom_NavigationView);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_NavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent0 = new Intent(TicketActivity.this, MainActivity.class);
                        startActivity(intent0);
                        break;
                    case R.id.ticket:

                        break;
                    case R.id.agregar:
                        Intent intent2 = new Intent(TicketActivity.this, AgregarActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.lugares:
                        Intent intent3 = new Intent(TicketActivity.this, LugaresActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.tiendas:
                        Intent intent4 = new Intent(TicketActivity.this, TiendasActivity.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });

        /*----------RecyclerView----------*/
        listaTickets = (RecyclerView) findViewById(R.id.rvTicket);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        /*Le estamos diciendo que el RecyclerView se comporte como un LinearLayout, que adquiera las características*/
        listaTickets.setLayoutManager(llm);
        /*Luego inicializamos lista de tickets*/
        inicializarListaTickets();
        /*Y ahora inicializamos adaptador*/
        inicializarAdaptador();


    } //FIN ONCREATE

    private boolean onNavigationItemSelected(MenuItem menuItem) {
        return true;
    }

    public void inicializarAdaptador() {
        /*Va a estar trabajando la instanciacion de TicketAdapter. Es decir, crear un objeto de  TickerAdapter,
        pasarle nuestra lista que tenemos para que TicketAdapter internamente pueda hacer to.do lo que configuramos*/
        TicketAdapter adapter = new TicketAdapter(tickets, this); //Va a recibir la lista de tickets que ya está inicializada abajo con los 11 elementos.
        // Y activity:this pasa la actividad para que se abra DetalleTicketActivity al seleccionar un cardview en TicketActivity.
        listaTickets.setAdapter(adapter); //Ahora el RecyclerView ya contiene el adaptador, el cual, el adapter va a estar llamando al Layout CardView,
        // va a estar pasándole todos los datos de la lista tickets, va a estar pasándosela a todos los views que el ViewHolder (en TicketAdapter.java) está declarando.
    }

    public void inicializarListaTickets() {
        tickets = new ArrayList<Ticket>();

        tickets.add(new Ticket("LA RECOVA", "UN TRAGO GRATIS CON TU CENA", "150", R.drawable.drink));
        tickets.add(new Ticket("SALTO HOTEL Y CASINO", "UN DESAYUNO GRATIS PARA 1 PERSONA CON SU HOSPEDAJE", "200", R.drawable.breakfast));
        tickets.add(new Ticket("ARTESANOS DAYMAN", "20% DE DESCUENTO EN TU COMPRA", "50", R.drawable.artesanos));
        tickets.add(new Ticket("PIZZERIA TROUVILLE CENTRO O SHOPPING", "REFRESCO GRATIS CON TU COMIDA", "100", R.drawable.sodadrinks));
        tickets.add(new Ticket("HELADERIA DOLCEZZA", "50% DE DESCUENTO EN SEGUNDA UNIDAD", "200", R.drawable.dolcezza));
        tickets.add(new Ticket("HELADERIA ALFREDITO", "50% DE DESCUENTO EN CONO 2 SABORES", "100", R.drawable.alfredito));
        tickets.add(new Ticket("HOTEL ESPANOL", "UNA NOCHE GRATIS", "500", R.drawable.hotelespanol));
        tickets.add(new Ticket("PEDIDOS YA", "ENVIO GRATIS EN TU PROXIMA COMPRA EN CUALQUIER LOCAL DE SALTO", "50", R.drawable.pedidosya));
        tickets.add(new Ticket("SORTEO MENSUAL TUR IT", "UNA PARTICIPACION POR 2 NOCHES EN SALTO TODO PAGO PARA 2 PERSONAS", "50", R.drawable.hotelcasino));
        tickets.add(new Ticket("SORTEO MENSUAL TUR IT", "UNA PARTICIPACION POR 2 NOCHES EN TERMAS DE DAYMAN TODO PAGO PARA 2 PERSONAS", "50", R.drawable.dayman));
        tickets.add(new Ticket("SORTEO MENSUAL TUR IT", "UNA PARTICIPACION POR 2 NOCHES EN TERMAS DE ARAPEY TODO PAGO PARA 2 PERSONAS", "50", R.drawable.arapey));

    }
}