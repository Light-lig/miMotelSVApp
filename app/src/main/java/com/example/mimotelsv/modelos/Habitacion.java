package com.example.mimotelsv.modelos;

import java.util.List;

public class Habitacion {
    private int id;
    private String nombre;
    private int numero;
    private String tipo;
    private double precio;
    private String tiempo;
    private String descripcion;
    private String estado;
    private List<Fotos> fotos;

    public Habitacion() {
    }


    public Habitacion(int id, String nombre, int numero, String tipo, double precio, String tiempo, String descripcion, String estado, List<Fotos> fotos) {
        this.id = id;
        this.nombre = nombre;
        this.numero = numero;
        this.tipo = tipo;
        this.precio = precio;
        this.tiempo = tiempo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fotos = fotos;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<Fotos> getFotos() {
        return fotos;
    }

    public void setFotos(List<Fotos> fotos) {
        this.fotos = fotos;
    }
}
