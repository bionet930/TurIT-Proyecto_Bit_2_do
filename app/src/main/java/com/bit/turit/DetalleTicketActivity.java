package com.bit.turit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class DetalleTicketActivity extends AppCompatActivity {

    //Variables
    TextView tvLocal, tvPremio, tvValor;
    ImageView ivFoto, qrImage;
    Button btnObtener, btnSiguiente;
    Integer valorEntero, idQRcode;


    String idQRcodeString;
    String textoPremio;
    String uid;

    FirebaseFirestore mFirestoreDetalleTicket;
    DatabaseReference imgref;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_ticket);

        uid = user.getUid();

        mFirestoreDetalleTicket = FirebaseFirestore.getInstance();
        imgref = FirebaseDatabase.getInstance().getReference().child("Url_QR");

        Bundle parametros = getIntent().getExtras();
        String nombre = parametros.getString("nombre");
        String premio = parametros.getString("premio");
        String valor = parametros.getString("valor");
        int foto = parametros.getInt("foto");

        tvLocal = (TextView) findViewById(R.id.tvLocal);
        tvPremio = (TextView) findViewById(R.id.tvPremio);
        tvValor = (TextView) findViewById(R.id.tvValor);
        ivFoto = (ImageView) findViewById(R.id.ivFoto);
        btnObtener = (Button) findViewById(R.id.obtenerButton);
        qrImage = (ImageView) findViewById(R.id.qrIV);
        btnSiguiente = (Button) findViewById(R.id.siguienteButton);

        btnObtener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Para poder obtener datos del usuario logueado se instancia FirebaseUser y FirebaseAuth
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //Se guarda en la variable el dato del Uid del usuario logueado y lo usa para validar la coleccion de datos Usuarios en FireStore y el documento lo filtra por Uid
                String uidUserLogued = new String(user.getUid());
                String nombre = user.getDisplayName();
                String correo = user.getEmail();
                String celular = user.getPhoneNumber();
                mFirestoreDetalleTicket.collection("Usuarios").document(uidUserLogued).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //Comprueba si hay registro en la base de datos de FireStore
                        if (documentSnapshot.exists()){
                            String puntos = documentSnapshot.getString("Puntos");
                            //Como los valores mostrados y guardados estan en String hay que pasarlos a valores enteros, se los guarda en una nueva variable
                            int puntosValor = Integer.parseInt(puntos);
                            int puntosPremio = valorEntero;

                            int nuevoValor = puntosValor - puntosPremio;

                            if (puntosValor >= puntosPremio){
                                String nuevoValorDefinitivo = Integer.toString(nuevoValor);

                                Map<String, Object> map = new HashMap<>();
                                map.put("Uid", uidUserLogued);
                                map.put("Nombre", nombre);
                                map.put("Correo", correo);
                                map.put("Celular", celular);
                                map.put("Puntos", nuevoValorDefinitivo);

                                mFirestoreDetalleTicket.collection("Usuarios").document(uidUserLogued).set(map);
                                generarQR();
                                //Boton siguiente se hace visible cuando presionamos el boton generar qr
                                btnSiguiente.setVisibility(View.VISIBLE);
                                Toast.makeText(DetalleTicketActivity.this,"Puntos Descontados",Toast.LENGTH_SHORT).show();

                                subirQRimagen();
                            }else {
                                Toast.makeText(DetalleTicketActivity.this,"Debe generar mas puntos",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                valorEntero = Integer.parseInt(valor);
            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSiguiente();
            }
        });

        tvLocal.setText(nombre);
        tvPremio.setText(premio);
        tvValor.setText(valor);
        ivFoto.setImageResource(foto);
    } //FIN ONCREATE

    private void generarQR() {
        //Se obtiene datos del text view
        String sTextPremio = tvPremio.getText().toString();
        //Inicializar multi format writer
        MultiFormatWriter writer = new MultiFormatWriter();

        Random random = new Random();
        idQRcode = random.nextInt(800000);

        idQRcodeString = Integer.toString(idQRcode);
        textoPremio = sTextPremio;

        try {
            //Inicializa bit matrix con el texto del premio solamente
            BitMatrix matrix = writer.encode("[" + idQRcodeString + "|" + uid + "|" + sTextPremio + "]", BarcodeFormat.QR_CODE, 550, 550);
            //Inicializa el barcode encoder
            BarcodeEncoder encoder = new BarcodeEncoder();
            //Inicializa bitmap
            Bitmap bitmap = encoder.createBitmap(matrix);
            //Set bitmap en imageview
            qrImage.setImageBitmap(bitmap);
            //Inicializa  input manager
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void subirQRimagen() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String uid = user.getUid();
        String nombreQR = idQRcodeString + "|" + uid + "|" + textoPremio;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://turit-fc7f4.appspot.com").child("QR_Codes").child(uid).child(nombreQR);

        qrImage.setDrawingCacheEnabled(true);
        qrImage.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        qrImage.layout(0, 0, qrImage.getMeasuredWidth(), qrImage.getMeasuredHeight());
        qrImage.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(qrImage.getDrawingCache());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] data = outputStream.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });

    }

    /*public void calculoPremio(){
        //Para poder obtener datos del usuario logueado se instancia FirebaseUser y FirebaseAuth
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //Se guarda en la variable el dato del Uid del usuario logueado y lo usa para validar la coleccion de datos Usuarios en FireStore y el documento lo filtra por Uid
        String uidUserLogued = new String(user.getUid());
        String nombre = user.getDisplayName();
        String correo = user.getEmail();
        String celular = user.getPhoneNumber();
        mFirestoreDetalleTicket.collection("Usuarios").document(uidUserLogued).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //Comprueba si hay registro en la base de datos de FireStore
                if (documentSnapshot.exists()){
                    String puntos = documentSnapshot.getString("Puntos");
                    //Como los valores mostrados y guardados estan en String hay que pasarlos a valores enteros, se los guarda en una nueva variable
                    int puntosValor = Integer.parseInt(puntos);
                    int puntosPremio = valorEntero;

                    int nuevoValor = puntosValor - puntosPremio;

                    if (puntosValor >= puntosPremio){
                        String nuevoValorDefinitivo = Integer.toString(nuevoValor);

                        Map<String, Object> map = new HashMap<>();
                        map.put("Uid", uidUserLogued);
                        map.put("Nombre", nombre);
                        map.put("Correo", correo);
                        map.put("Celular", celular);
                        map.put("Puntos", nuevoValorDefinitivo);

                        mFirestoreDetalleTicket.collection("Usuarios").document(uidUserLogued).set(map);
                        Toast.makeText(DetalleTicketActivity.this,"Puntos Descontados",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(DetalleTicketActivity.this,"Debe generar mas puntos",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }*/


    /*private void showQRToast() {
        Toast.makeText(DetalleTicketActivity.this,"QR Generado",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, QRActivity.class);
        startActivity(intent);
    }*/

    private void openSiguiente() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
