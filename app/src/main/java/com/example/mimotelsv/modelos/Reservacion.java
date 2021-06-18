package com.example.mimotelsv.modelos;

import java.util.Date;

public class Reservacion {
    private Integer resId;
    private double resCantidadPagar;
    private Date fecha;
    private String hora;
    private String nombreHabitacion;
    private String tipoHabitacion;
    private int numeroHabitacion;
    private int haId;
    private int usrId;

    public Reservacion() {
    }

    public Reservacion(Integer resId, double resCantidadPagar, Date fecha, String hora, String nombreHabitacion, String tipoHabitacion, int numeroHabitacion, int haId, int usrId) {
        this.resId = resId;
        this.resCantidadPagar = resCantidadPagar;
        this.fecha = fecha;
        this.hora = hora;
        this.nombreHabitacion = nombreHabitacion;
        this.tipoHabitacion = tipoHabitacion;
        this.numeroHabitacion = numeroHabitacion;
        this.haId = haId;
        this.usrId = usrId;
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public double getResCantidadPagar() {
        return resCantidadPagar;
    }

    public void setResCantidadPagar(double resCantidadPagar) {
        this.resCantidadPagar = resCantidadPagar;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNombreHabitacion() {
        return nombreHabitacion;
    }

    public void setNombreHabitacion(String nombreHabitacion) {
        this.nombreHabitacion = nombreHabitacion;
    }

    public String getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(String tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }

    public int getNumeroHabitacion() {
        return numeroHabitacion;
    }

    public void setNumeroHabitacion(int numeroHabitacion) {
        this.numeroHabitacion = numeroHabitacion;
    }

    public int getHaId() {
        return haId;
    }

    public void setHaId(int haId) {
        this.haId = haId;
    }

    public int getUsrId() {
        return usrId;
    }

    public void setUsrId(int usrId) {
        this.usrId = usrId;
    }
}
