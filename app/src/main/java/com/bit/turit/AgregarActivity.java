package com.bit.turit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AgregarActivity extends AppCompatActivity {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    //Variables
    BottomNavigationView bottomNavigationView;
    ImageView selectedImage;
    Button fotoBtn, formularioBtn;
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        /*----------findViewById----------*/
        bottomNavigationView = findViewById(R.id.bottom_NavigationView);
        selectedImage = findViewById(R.id.fotoIV);
        fotoBtn = findViewById(R.id.tomarFotoBtn);
        formularioBtn = findViewById(R.id.pasarAFormularioBtn);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_NavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        /*----------Para abrir la cámara cuando se seleccione el botón Tomar Foto. Antes pide permiso.----------*/
        fotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermissions();
            }
        });

        /*----------Botón que nos lleva a actividad del formulario----------*/
        formularioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFormularioActivity();
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent0 = new Intent(AgregarActivity.this, MainActivity.class);
                        startActivity(intent0);
                        break;
                    case R.id.ticket:
                        Intent intent1 = new Intent(AgregarActivity.this, TicketActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.agregar:

                        break;
                    case R.id.lugares:
                        Intent intent3 = new Intent(AgregarActivity.this, LugaresActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.tiendas:
                        Intent intent4 = new Intent(AgregarActivity.this, TiendasActivity.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });
    } //FINAL DE ONCREATE

    private void openFormularioActivity() {
        Intent intent = new Intent (this, FormularioActivity.class);
        startActivity(intent);
    }


    /*----------Primero se necesita saber si el permiso ya estaba otorgado----------*/
    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            /*Si no fue otorgado, necesitamos pedirle el permiso en runtime*/

            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }/*Si el permiso es otorgado, se abre la camara*/else {
            dispatchTakePictureIntent();
        }
    }

    /*----------Si el permiso no fue otorgado, necesitamos hacerle saber al usuario que es necesario para que pueda utilizar la cámara----------*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { /*Si ambos son verdaderos, el usuario dio permiso*/
                dispatchTakePictureIntent();
            }else { /*Si son falsos, el usuario denegó el acceso*/
                Toast.makeText(this, "El permiso es necesario para utilizar la cámara.", Toast.LENGTH_SHORT).show();

            }

        }

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                selectedImage.setImageURI(Uri.fromFile(f));
            }
        }
    }

 //El siguiente codigo se obtuvo de https://developer.android.com/training/camera/photobasics
    private File createImageFile() throws IOException {
        //Crea nombre para la imagen. Se va a llamar JPG_fechayhora
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //Creamos la imagen y le pasamos parametros
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        //Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.bit.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
}