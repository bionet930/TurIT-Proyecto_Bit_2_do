package com.bit.turit;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Fcm extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        Log.e("token","Mi token es:"+s);

        guardarToken(s);


    }

    private void guardarToken(String s) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Token");
        ref.child("Prueba").setValue(s);


    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from = remoteMessage.getFrom();
        Log.e("TAG", "Mensaje Recibido de:"+from);

        if (remoteMessage.getData().size() > 0){

            String titulo = remoteMessage.getData().get("Titulo");
            String mensaje = remoteMessage.getData().get("Mensaje");
            String foto = remoteMessage.getData().get("foto");

            mayorqueoreo(titulo, mensaje, foto);

        }

    }

    private void mayorqueoreo(String titulo, String mensaje, String foto) {

        String id = "Mensaje";
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, id);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel nc = new NotificationChannel(id, "Nuevo", NotificationManager.IMPORTANCE_HIGH);
            nc.setShowBadge(true);
            nm.createNotificationChannel(nc);
        }
        try {
            Bitmap img_foto = Picasso.with(getApplicationContext()).load(foto).get();
            builder.setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(titulo)
                    .setSmallIcon(R.mipmap.ic_logoturit_round)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(img_foto).bigLargeIcon(null))
                    .setContentText(mensaje)
                    .setContentIntent(clicknoti())
                    .setContentInfo("Nuevo");
            Random random = new Random();
            int idNotify = random.nextInt(8000);

            assert nm != null;
            nm.notify(idNotify, builder.build());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public PendingIntent clicknoti(){
        Intent nf = new Intent(getApplicationContext(), MainActivity.class);
        nf.putExtra("Color", "Rojo");
        nf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this, 0,nf,0);
    }
}
