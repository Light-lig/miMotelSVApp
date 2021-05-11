package com.example.mimotelsv.modelos;

public class Municipio {
    private int id;
    private String nombre;
    private Departamento depa;

    public Municipio() {
    }

    public Municipio(int id, String nombre, Departamento depa) {
        this.id = id;
        this.nombre = nombre;
        this.depa = depa;
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

    public Departamento getDepa() {
        return depa;
    }

    public void setDepa(Departamento depa) {
        this.depa = depa;
    }
}
