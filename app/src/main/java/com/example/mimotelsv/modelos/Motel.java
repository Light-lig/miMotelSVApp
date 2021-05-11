package com.example.mimotelsv.modelos;

import android.graphics.Bitmap;

import java.util.List;

public class Motel {
        private int id;
        private String nombre;
        private String direccion;
        private double latitud;
        private double longitud;
        private int municipio;
        private int categoria;
        private String horaApertura;
        private String horaCierre;
        private byte[] imagenProtada;
        private double rating;

    public Motel() {
    }

    public Motel(int id, String nombre, String direccion, double latitud, double longitud, int municipio, int categoria, String horaApertura, String horaCierre, byte[] imagenProtada, double rating) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.municipio = municipio;
        this.categoria = categoria;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
        this.imagenProtada = imagenProtada;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public int getMunicipio() {
        return municipio;
    }

    public void setMunicipio(int municipio) {
        this.municipio = municipio;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public String getHoraApertura() {
        return horaApertura;
    }

    public void setHoraApertura(String horaApertura) {
        this.horaApertura = horaApertura;
    }

    public String getHoraCierre() {
        return horaCierre;
    }

    public void setHoraCierre(String horaCierre) {
        this.horaCierre = horaCierre;
    }

    public byte[] getImagenProtada() {
        return imagenProtada;
    }

    public void setImagenProtada(byte[] imagenProtada) {
        this.imagenProtada = imagenProtada;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
