package com.bit.turit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MisTicketsActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    ImageView img_qruser1;
    ImageView nombreqrlista;

    String uid = user.getUid();
    String nombreqrcode;
    String nombreqrcode2;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://turit-fc7f4.appspot.com").child("QR_Codes").child(uid);
    private ArrayList <String> codigosQR;

    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_tickets);

        nombreqrlista = findViewById(R.id.nombreqrlista);
        listView = findViewById(R.id.qrCodesList);
        codigosQR = new ArrayList<>();

        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {


                    //Inicializar multi format writer
                    MultiFormatWriter writer = new MultiFormatWriter();

                    codigosQR.add(item.getName());
                    nombreqrcode = item.getName();


                    //Prueba par armar los qr de datos obtenidos
                    try {
                        //Inicializa bit matrix con el texto del premio solamente
                        BitMatrix matrix = writer.encode(nombreqrcode, BarcodeFormat.QR_CODE, 550, 550);
                        //Inicializa el barcode encoder
                        BarcodeEncoder encoder = new BarcodeEncoder();
                        //Inicializa bitmap
                        Bitmap bitmap = encoder.createBitmap(matrix);
                        //Set bitmap en imageview
                        nombreqrlista.setImageBitmap(bitmap);
                        //Inicializa  input manager
                        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.row, codigosQR);

                listView.setAdapter(adapter);
            }
        }).addOnFailureListener(e ->  {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(MisTicketsActivity.this);
            builder1.setMessage("Ha Ocurrido un Error al cargar los Codigos QR");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = builder1.create();
            alert11.show();
        });

    }

    }
