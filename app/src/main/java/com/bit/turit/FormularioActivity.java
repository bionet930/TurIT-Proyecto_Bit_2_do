package com.bit.turit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class FormularioActivity extends AppCompatActivity implements OnMapReadyCallback {

    //VARIABLES
    Button buttonEnviarFormulario;
    Button consultarLatLong;
    EditText edtLat, edtLong, edtRespuesta1, edtRespuesta2, edtRespuesta3;
    GoogleMap mapa;
    FirebaseFirestore mFirestore;
    LatLng posicion;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirestore = FirebaseFirestore.getInstance();

        /*----------findViewById----------*/
        buttonEnviarFormulario = findViewById(R.id.enviarFormularioButton);
        consultarLatLong = findViewById(R.id.enviarUbicacionFormulario);

        edtLat = findViewById(R.id.txtLat);
        edtLong = findViewById(R.id.txtLong);

        edtRespuesta1 = findViewById(R.id.respuesta1ET);
        edtRespuesta2 = findViewById(R.id.respuesta2ET);
        edtRespuesta3 = findViewById(R.id.respuesta3ET);

        getCargarLocalizacion();


        buttonEnviarFormulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cargarDatosUsuario();
                cargardatos();
                //cargarPuntos();
                //openGraciasActivity();
            }
        });


        MapFragment micontenedor = (MapFragment) getFragmentManager().findFragmentById(R.id.ubicacionActualIV);
        micontenedor.getMapAsync(this);


    } // FINAL DE ONCREATE

   /* private void cargarDatosUsuario() {
        //Para poder obtener datos del usuario logueado se instancia FirebaseUser y FirebaseAuth
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String uid = user.getUid();
        String nombre = user.getDisplayName();
        String correo = user.getEmail();
        String celular = user.getPhoneNumber();
        String puntos = "0";

        Map<String, Object> map = new HashMap<>();
        map.put("Uid", uid);
        map.put("Nombre", nombre);
        map.put("Correo", correo);
        map.put("Celular", celular);
        map.put("Puntos", puntos);

        mFirestore.collection("Usuarios").document(uid).set(map);
    }*/

    private void cargarPuntos() {
        //Para poder obtener datos del usuario logueado se instancia FirebaseUser y FirebaseAuth
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //Se guarda en la variable el dato del Uid del usuario logueado y lo usa para validar la coleccion de datos Usuarios en FireStore y el documento lo filtra por Uid
        String uidUserLogued = new String(user.getUid());
        mFirestore.collection("Usuarios").document(uidUserLogued).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //Comprueba si hay registro en la base de datos de FireStore
                if (documentSnapshot.exists()){
                    String puntos = documentSnapshot.getString("Puntos");
                    //Como los valores mostrados y guardados estan en String hay que pasarlos a valores enteros, se los guarda en una nueva variable
                    int puntosValor = Integer.parseInt(puntos);
                    int puntosNuevos = 50;

                    int nuevoValor = puntosValor + puntosNuevos;

                    if (puntosValor >= 0){

                        String nuevoValorDefinitivo = Integer.toString(nuevoValor);

                        String uid = user.getUid();
                        String nombre = user.getDisplayName();
                        String correo = user.getEmail();
                        String celular = user.getPhoneNumber();

                        Map<String, Object> map = new HashMap<>();
                        map.put("Uid", uid);
                        map.put("Nombre", nombre);
                        map.put("Correo", correo);
                        map.put("Celular", celular);
                        map.put("Puntos", nuevoValorDefinitivo);

                        mFirestore.collection("Usuarios").document(uidUserLogued).set(map);
                        Toast.makeText(FormularioActivity.this,"Puntos Sumados",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(FormularioActivity.this,"No se pudieron cargar los Puntos",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    private void getCargarLocalizacion() {
        consultarLatLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) FormularioActivity.this.getSystemService(Context.LOCATION_SERVICE);

                LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        edtLat.setText("" + location.getLatitude());
                        edtLong.setText("" + location.getLongitude());
                        posicion = new LatLng(location.getLatitude(), location.getLongitude());
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                };
                int permiso = ContextCompat.checkSelfPermission(FormularioActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                Toast.makeText(FormularioActivity.this, "Ubicacion Generada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargardatos() {
        String respuesta1 = edtRespuesta1.getText().toString();
        String respuesta2 = edtRespuesta2.getText().toString();
        String respuesta3 = edtRespuesta3.getText().toString();
        String latitud = edtLat.getText().toString();
        String longitud = edtLong.getText().toString();
        String uid = user.getUid();


        if (TextUtils.isEmpty(respuesta1)){
            Toast.makeText(getApplicationContext(),"Debes Agregar Respuesta 1", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(respuesta2)){
            Toast.makeText(getApplicationContext(),"Debes Agregar Respuesta 2", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(respuesta3)){
            Toast.makeText(getApplicationContext(),"Debes Agregar Respuesta 3", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(latitud)){
            Toast.makeText(getApplicationContext(),"Debes Pulsar Boton Generar Ubicación", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(longitud)){
            Toast.makeText(getApplicationContext(),"Debes Pulsar Boton Generar Ubicación", Toast.LENGTH_SHORT).show();
        }else {
            Map<String, Object> map = new HashMap<>();
            map.put("Respuesta1", respuesta1);
            map.put("Respuesta2", respuesta2);
            map.put("Respuesta3", respuesta3);
            map.put("Latitud", latitud);
            map.put("Longitud", longitud);
            map.put("Uid", uid);

            mFirestore.collection("Formularios").document().set(map);

            cargarPuntos();
            openGraciasActivity();
        }

        /*
        Map<String, Object> map = new HashMap<>();
        map.put("Respuesta1", respuesta1);
        map.put("Respuesta2", respuesta2);
        map.put("Respuesta3", respuesta3);
        map.put("Latitud", latitud);
        map.put("Longitud", longitud);
        map.put("Uid", uid);

        mFirestore.collection("Formularios").document().set(map);*/
    }

    private void openGraciasActivity() {
        Intent intent = new Intent(this, GraciasActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mapa = googleMap;

        LatLng salto = new LatLng(-31.388227,-57.959474);
        mapa.addMarker(new MarkerOptions()
                .position(salto)
        );
        
        mapa.moveCamera(CameraUpdateFactory.newLatLng(salto));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(salto)
                .zoom(13)
                .bearing(45)
                .tilt(45)
                .build();
        mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));




        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mapa.setMyLocationEnabled(true);

        mapa.getUiSettings().setMyLocationButtonEnabled(false);

        LocationManager locationManager = (LocationManager) FormularioActivity.this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mapa.addMarker(new MarkerOptions()
                        .position(currentLocation)
                        .title("Posición Actual")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps_chico)).anchor(0.0f, 0.0f));

            }
        };

    }

}