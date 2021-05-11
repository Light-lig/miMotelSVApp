package com.example.mimotelsv.modelos;

public class Fotos {
    private int id;
    private int idHabitacion;
    private byte[] foto;
    private String descripcion;
    private int moId;

    public Fotos() {
    }

    public Fotos(int id, int idHabitacion, byte[] foto, String descripcion, int moId) {
        this.id = id;
        this.idHabitacion = idHabitacion;
        this.foto = foto;
        this.descripcion = descripcion;
        this.moId = moId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdHabitacion(int idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getMoId() {
        return moId;
    }

    public void setMoId(int moId) {
        this.moId = moId;
    }
}
