package com.example.mimotelsv.modelos;

import java.util.Date;

public class Reservacion {
    private Integer resId;
    private double resCantidadPagar;
    private Date fecha;
    private String hora;
    private Integer haId;
    private Integer UsrId;

    public Reservacion() {
    }

    public Reservacion(Integer resId, double resCantidadPagar, Date fecha, String hora, Integer haId, Integer usrId) {
        this.resId = resId;
        this.resCantidadPagar = resCantidadPagar;
        this.fecha = fecha;
        this.hora = hora;
        this.haId = haId;
        UsrId = usrId;
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

    public Integer getHaId() {
        return haId;
    }

    public void setHaId(Integer haId) {
        this.haId = haId;
    }

    public Integer getUsrId() {
        return UsrId;
    }

    public void setUsrId(Integer usrId) {
        UsrId = usrId;
    }
}
