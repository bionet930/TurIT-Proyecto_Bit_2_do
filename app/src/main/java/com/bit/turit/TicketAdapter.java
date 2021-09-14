package com.bit.turit;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {

    ArrayList<Ticket> tickets;
    Activity activity;

    //Cuando estemos llamando a TicketAdapter se va a invocar el método constructor. Entonces acá le pasamos la lista de tickets.
    public TicketAdapter(ArrayList<Ticket> tickets, Activity activity){
        this.tickets = tickets;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_ticket, parent, false);
        //Esta linea de código arriba, lo que hace es darle vida al Layout. Se asocia al RecyclerView.
        return new TicketViewHolder(v); //Va a manejar la parte de inflar/darle vida a nuestro Layout CardView
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder ticketViewHolder, int position) {

        final Ticket ticket = tickets.get(position);
        ticketViewHolder.tvLocal.setText(ticket.getNombreLocal());
        ticketViewHolder.tvPremio.setText(ticket.getTextoPremio());
        ticketViewHolder.tvPuntos.setText(ticket.getValorPuntos());
        ticketViewHolder.imgFoto.setImageResource(ticket.getFoto());

        ticketViewHolder.imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(activity,ticket.getNombreLocal(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, DetalleTicketActivity.class);
                intent.putExtra("nombre",ticket.getNombreLocal());
                intent.putExtra("premio",ticket.getTextoPremio());
                intent.putExtra("valor",ticket.getValorPuntos());
                intent.putExtra("foto",ticket.getFoto());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { //Cantidad de elementos que contiene mi lista de tickets
        return tickets.size();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder{

        //Variables
        private TextView tvLocal, tvPremio, tvPuntos;
        private ImageView imgFoto;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocal = (TextView) itemView.findViewById(R.id.cardviewNombreLocalTV);
            tvPremio = (TextView) itemView.findViewById(R.id.textoPremioTV);
            tvPuntos = (TextView) itemView.findViewById(R.id.valorPuntosTV);
            imgFoto = (ImageView) itemView.findViewById(R.id.cardviewIV);
        }
    }
}