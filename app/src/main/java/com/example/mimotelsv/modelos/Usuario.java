package com.example.mimotelsv.modelos;

public class Usuario {
    private int id;
    private String correo;
    private String pass;
    private int mun;
    private int idTipo;

    public Usuario() {
    }

    public Usuario(int id, String correo, String pass, int mun, int idTipo) {
        this.id = id;
        this.correo = correo;
        this.pass = pass;
        this.mun = mun;
        this.idTipo = idTipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getMun() {
        return mun;
    }

    public void setMun(int mun) {
        this.mun = mun;
    }

    public int getIdTipo() {
        return idTipo;
    }

    public void setIdTipo(int idTipo) {
        this.idTipo = idTipo;
    }
}
