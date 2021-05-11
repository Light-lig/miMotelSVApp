package com.example.mimotelsv.modelos;

import java.util.List;

public class Habitacion {
    private int id;
    private String nombre;
    private String tipo;
    private double precio;
    private String tiempo;
    private String descripcion;
    private int idMotel;
    private String estado;
    private List<Fotos> fotos;

    public Habitacion() {
    }

    public Habitacion(int id, String nombre, String tipo, double precio, String tiempo, String descripcion, int idMotel, String estado, List<Fotos> fotos) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.precio = precio;
        this.tiempo = tiempo;
        this.descripcion = descripcion;
        this.idMotel = idMotel;
        this.estado = estado;
        this.fotos = fotos;
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

    public int getIdMotel() {
        return idMotel;
    }

    public void setIdMotel(int idMotel) {
        this.idMotel = idMotel;
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
