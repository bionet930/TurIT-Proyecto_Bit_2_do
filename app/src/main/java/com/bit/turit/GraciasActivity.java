package com.bit.turit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GraciasActivity extends AppCompatActivity {

    //VARIABLES
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gracias);

        /*----------findViewById----------*/
        button = findViewById(R.id.continuarButton);

        /*----------Botón que nos lleva a HomeActivity----------*/
        button.setOnClickListener(v -> openHome());
    } //FIN DEL ONCREATE

    private void openHome() {
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }
}