package com.bit.turit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class MenuTiendaActivity extends AppCompatActivity  {


    //Variables de TextView para mostrar datos del usuario Logueado en pantalla principal
    ImageView img_fototienda;
    TextView tv_usernametienda, tv_useremailtienda;


    //Variables para lectura del codigo QR
    String resultadoQR;

    String idQRCode;
    String uidUsuarioQR;
    String textoPremioQR;


   // private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_tienda);

        //Variables para pasar informacion al Header del menu lateral donde va a tener infor del usuario
        img_fototienda = findViewById(R.id.image_usertienda);
        tv_usernametienda = findViewById(R.id.tv_usernametienda);


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        /*FirebaseMessaging.getInstance().subscribeToTopic("enviaratodos").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MenuTiendaActivity.this,"Suscrito al enviar a todos!", Toast.LENGTH_SHORT).show();
            }
        });*/

        //Uso de libreria para traer foto del usuario por URL
        Glide.with(this).load(user.getPhotoUrl()).into(img_fototienda);
        //Muestra los datos del nombre y del email en los textview del header (usuario que ya esta logueado)
        tv_usernametienda.setText(user.getDisplayName());


    }//FINAL DEL ON CREATE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    public void mensajesPush(View view){
        RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
        JSONObject json = new JSONObject();
        try{
            String urlfoto = "https://miracomosehace.com/wp-content/uploads/2020/02/telefono-notificaciones.jpg";
            //String token = "c_220upnSr-gdQp0u3Rlp5:APA91bEOjQ2Zh6K06BpJW2eF7g8X1UCoSMoD_nif0nvAiEoWSfg6yxRC8W9T-sXS5gBdiRD_Iq_-_CfhhOVUvf_x2LY1VDtmO1JZmSahS3L_J5iP0hay19q0HSq4lmOMj5xU9g1dIMyb";

            json.put("to", "/topics/"+"enviaratodos");
            JSONObject notificacion = new JSONObject();
            notificacion.put("Titulo", "Suscribete a Nuestro Canal de Usuarios");
            notificacion.put("Mensaje", "Suscribiendote a Nuestras Notificaciones para Usuarios obtendras mayores beneficios!");
            notificacion.put("foto", urlfoto);

            json.put("data", notificacion);

            String URL = "https://fcm.googleapis.com/fcm/send";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, json, null, null){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();

                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAAZgO-XA0:APA91bFpsSTVrohxB-h6SjhEdho3llGpoS1J8B4Zb0wEPyfWQ3PK0zM4DP0UTjMwi3nDoOgFcai5Tf2YVNXdpYj5OOs8LXVtJLD5t4tfR92WUg96hGu1TxWvbsqMCIlQm-20Y-nMMhVM");
                    return header;
                }
            };
            myrequest.add(request);
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    public void mensajesPushUsersSuscritos(View view){
        RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
        JSONObject json = new JSONObject();
        try{
            String urlfoto = "https://static.hosteltur.com/app/public/uploads/img/articles/2015/06/01/O_5c1a40fa70519_termas-dayman2.jpg";
            //String token = "c_220upnSr-gdQp0u3Rlp5:APA91bEOjQ2Zh6K06BpJW2eF7g8X1UCoSMoD_nif0nvAiEoWSfg6yxRC8W9T-sXS5gBdiRD_Iq_-_CfhhOVUvf_x2LY1VDtmO1JZmSahS3L_J5iP0hay19q0HSq4lmOMj5xU9g1dIMyb";

            json.put("to", "/topics/"+"enviarSuscritos");
            JSONObject notificacion = new JSONObject();
            notificacion.put("Titulo", "Disfruta del Turismo Salteño");
            notificacion.put("Mensaje", "Ingresando a TurIT te regalamos 200 puntos extras!");
            notificacion.put("foto", urlfoto);

            json.put("data", notificacion);

            String URL = "https://fcm.googleapis.com/fcm/send";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, json, null, null){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();

                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAAZgO-XA0:APA91bFpsSTVrohxB-h6SjhEdho3llGpoS1J8B4Zb0wEPyfWQ3PK0zM4DP0UTjMwi3nDoOgFcai5Tf2YVNXdpYj5OOs8LXVtJLD5t4tfR92WUg96hGu1TxWvbsqMCIlQm-20Y-nMMhVM");
                    return header;
                }
            };
            myrequest.add(request);
        }catch (JSONException e){
            e.printStackTrace();
        }

    }



    public void reportarProblemaWhats(View view){
        Uri uri = Uri.parse("http://wa.me/59892213571");
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(i);
    }


    public void btnEscanear (View view){
        /*mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();*/

        IntentIntegrator integrator = new IntentIntegrator(MenuTiendaActivity.this);

        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Canje de Premios por QR");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();

    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null){
            if (result.getContents() == null){
                Toast.makeText(this,"No se puede leer codigo QR", Toast.LENGTH_SHORT).show();
            }else{

                //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                resultadoQR = result.getContents();

                String string = resultadoQR;
                String[] parts = string.split("\\|");
                idQRCode = parts[0]; // ID qr
                uidUsuarioQR = parts[1]; // Uid
                textoPremioQR = parts[2]; // Texto Premio

                //Toast.makeText(this, "El premio es: " + textoPremioQR, Toast.LENGTH_LONG).show();

                anularQRusuario();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(this,"Qr ya canjeado o no existe el codigo", Toast.LENGTH_SHORT).show();
        }

    }


    public void cerrarsesion (View view) {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MenuTiendaActivity.this, "Sesión Finalizada", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


   /* @Override
    public void handleResult(Result result) {
        Log.v("HandleResult", result.getText());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Resultado del Codigo");
        builder.setMessage(result.getText());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        anularQRusuario();

        //mScannerView.resumeCameraPreview(this);
    }*/

    private void anularQRusuario() {


        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference to the file to delete
        StorageReference desertRef = storageRef.child("QR_Codes").child(uidUsuarioQR).child(idQRCode + "|" + uidUsuarioQR + "|" + textoPremioQR);

        // Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // File deleted successfully
                Toast.makeText(MenuTiendaActivity.this,"Premio Canjeado : "+ textoPremioQR,Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Intent intent = new Intent(getApplicationContext(), MenuTiendaActivity.class);
                startActivity(intent);
                Toast.makeText(MenuTiendaActivity.this,"Qr ya canjeado o no existe el codigo", Toast.LENGTH_SHORT).show();
            }
        });

    }


}