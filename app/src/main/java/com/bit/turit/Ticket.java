package com.bit.turit;

public class Ticket {

    //Variables
    private String nombreLocal;
    private String textoPremio;
    private String valorPuntos;
    private int foto;


    public Ticket(String nombreLocal, String textoPremio, String valorPuntos, int foto) {
        this.nombreLocal = nombreLocal;
        this.textoPremio = textoPremio;
        this.valorPuntos = valorPuntos;
        this.foto = foto;
    }

    public String getNombreLocal() {
        return nombreLocal;
    }

    public void setNombreLocal(String nombreLocal) {
        this.nombreLocal = nombreLocal;
    }

    public String getTextoPremio() {
        return textoPremio;
    }

    public void setTextoPremio(String textoPremio) {
        this.textoPremio = textoPremio;
    }

    public String getValorPuntos() {
        return valorPuntos;
    }

    public void setValorPuntos(String valorPuntos) {
        this.valorPuntos = valorPuntos;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }
}