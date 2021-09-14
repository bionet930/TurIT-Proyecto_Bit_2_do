package com.bit.turit;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.Blob;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity<uid> extends AppCompatActivity  {

    //Variables
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    MenuItem menuMain;
    FragmentManager fragmentManager;
    ImageView misTicketsIV;
    TextView misTicketsTV;

    FirebaseAuth mfirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    String Uid_menutienda1 = "GtAa5IHrPhMLlwqRRfY8GLRlxh52";

    FirebaseFirestore mFirestoreMain;

    TextView puntosusuario;

    Switch swActivarNoti;

    public static final int REQUEST_CODE = 5454;

    List<AuthUI.IdpConfig> provider = Arrays.asList(
            new AuthUI.IdpConfig.FacebookBuilder().build(),
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        puntosusuario = findViewById(R.id.counterTV);
        swActivarNoti = findViewById(R.id.swActivarNotificaciones);

        mFirestoreMain = FirebaseFirestore.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        FirebaseMessaging.getInstance().subscribeToTopic("enviaratodos").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //Toast.makeText(MainActivity.this,"Suscrito al canal", Toast.LENGTH_SHORT).show();
            }
        });


        mfirebaseAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user !=null){
                    //Toast.makeText(MainActivity.this, "Sesión Iniciada", Toast.LENGTH_SHORT).show();
                    //Metodo para comparar por Uid de un usuario logueado, si es igual a la variable de Menu Tienda lo lleva al activity Menu Tienda
                    alertaAdmin();
                    //Metodo para Leer los puntos por Uid de usuario logueado por Firebase y datos de FireStore
                    obtenerPuntos();
                }else  {
                    startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(provider)
                            .setIsSmartLockEnabled(false)
                            .setTheme(R.style.Theme_AppCompat_DayNight)
                            .build(), REQUEST_CODE
                    );
                }
            }
        };

        /*----------Hooks----------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        /*----------Toolbar----------*/
        setSupportActionBar(toolbar);

        /*----------Navigation Drawer Menu----------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        /*navigationView.setNavigationItemSelectedListener(this);*/

        /*----------Mis Tickets----------*/
        misTicketsIV = findViewById(R.id.tickets_acumuladosIV);
        misTicketsTV = findViewById(R.id.tickets_acumuladosTV);

        misTicketsIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMisTickets();
            }
        });

        misTicketsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMisTickets();
            }
        });

        /*----------Bottom Navigation Bar----------*/
        bottomNavigationView = findViewById(R.id.bottom_NavigationView);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_NavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:

                        break;
                    case R.id.ticket:
                        Intent intent1 = new Intent(MainActivity.this, TicketActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.agregar:
                        Intent intent2 = new Intent(MainActivity.this, AgregarActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.lugares:
                        Intent intent3 = new Intent(MainActivity.this, LugaresActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.tiendas:
                        Intent intent4 = new Intent(MainActivity.this, TiendasActivity.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });

    }// FINAL DEL ON-CREATE!!!!!!!!!!!

    private void openMisTickets() {
        Intent intent = new Intent(this, MisTicketsActivity.class);
        startActivity(intent);
    }

    public void obtenerPuntos(){
        //Para poder obtener datos del usuario logueado se instancia FirebaseUser y FirebaseAuth
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //Se guarda en la variable el dato del Uid del usuario logueado y lo usa para validar la coleccion de datos Usuarios en FireStore y el documento lo filtra por Uid
        String uidUserLogued = new String(user.getUid());
        String nombre = user.getDisplayName();
        String correo = user.getEmail();
        String celular = user.getPhoneNumber();
        String puntos = "0";
        mFirestoreMain.collection("Usuarios").document(uidUserLogued).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    String puntos = documentSnapshot.getString("Puntos");
                    puntosusuario.setText(puntos);
                }else{
                    Map<String, Object> map = new HashMap<>();
                    map.put("Uid", uidUserLogued);
                    map.put("Nombre", nombre);
                    map.put("Correo", correo);
                    map.put("Celular", celular);
                    map.put("Puntos", puntos);

                    mFirestoreMain.collection("Usuarios").document(uidUserLogued).set(map);
                }
            }
        });
    }

    public void alertaAdmin() {
        String uid = new String (FirebaseAuth.getInstance().getUid());

        if (uid.equals(Uid_menutienda1)) {
            Toast.makeText(this, "Eres User de Tienda",Toast.LENGTH_SHORT).show();
            Intent menutienda = new Intent(getApplicationContext(),MenuTiendaActivity.class);
            startActivity(menutienda);
            finish();
        }
    }

    /*Para que no se cierre la app entera cuando abrimos el drawer y apretamos botón para atrás.*/
    @Override
    public void onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_datos_del_usuario:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new DatosUsuarioFragment()).commit();
                break;
            case R.id.nav_notificaciones:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new NotificacionesFragment()).commit();
                break;
            case R.id.nav_historial:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HistorialFragment()).commit();
                break;
            case R.id.nav_TyC:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new TerminosYCondicionesFragment()).commit();
                break;
            case R.id.nav_seguridad:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SeguridadFragment()).commit();
                break;
            case R.id.nav_sobre_nosotros:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SobreNosotrosFragment()).commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }*/

    public void onClickSW(View view){
        final FirebaseMessaging mensajespush = FirebaseMessaging.getInstance();
        if (view.getId() == R.id.swActivarNotificaciones){
            if (swActivarNoti.isChecked()){
                mensajespush.subscribeToTopic("enviarSuscritos").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this,"Activaste Notificaciones para Usuarios", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                mensajespush.unsubscribeFromTopic("enviarSuscritos");
                mensajespush.subscribeToTopic("enviaratodos").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this,"Descativaste Notificaciones para Usuarios", Toast.LENGTH_SHORT).show();
            }
        });
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mfirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mfirebaseAuth.removeAuthStateListener(mAuthListener);
    }

    //Metodo creado para el cierre de sesion por Firebase, ya que al cerrar la app no se finaliza la sesion de Firebase hasta que el usuario marque la opcion cerrar sesion desde el menu lateral del drawer
    public void cerrarsesion (MenuItem item) {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this, "Sesión Finalizada", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}


