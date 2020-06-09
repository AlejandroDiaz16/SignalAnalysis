package com.example.interiorpositioning.entidades;

import java.util.Date;

public class data {
    private String fecha;
    private String mac, magneto, acelerometro,giroscopio, pasos;

    public data() {
        this.fecha = fecha;
        this.mac = mac;
        this.magneto = magneto;
        this.acelerometro = acelerometro;
        this.giroscopio = giroscopio;
        this.pasos = pasos;
    }

    public data(String fecha, String mac, String magneto, String acelerometro, String giroscopio, String pasos) {
        this.fecha = fecha;
        this.mac = mac;
        this.magneto = magneto;
        this.acelerometro = acelerometro;
        this.giroscopio = giroscopio;
        this.pasos = pasos;
    }


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMagneto() {
        return magneto;
    }

    public void setMagneto(String magneto) {
        this.magneto = magneto;
    }

    public String getAcelerometro() {
        return acelerometro;
    }

    public void setAcelerometro(String acelerometro) {
        this.acelerometro = acelerometro;
    }

    public String getGiroscopio() {
        return giroscopio;
    }

    public void setGiroscopio(String giroscopio) {
        this.giroscopio = giroscopio;
    }

    public String getPasos() {
        return pasos;
    }

    public void setPasos(String pasos) {
        this.pasos = pasos;
    }
}
