package com.example.interiorpositioning.utilidades;

public class utilidades {
    //constantes tabla
    public static final String tabla_data="data";
    public static final String campo_fecha="fecha";
    public static final String campo_mac="mac";
    public static final String campo_magneto="magnetometro";
    public static final String campo_acele="acelerometro";
    public static final String campo_giro="giroscopio";
    public static final String campo_pasos="pasos";
    public static final String crear="CREATE TABLE "+tabla_data+" ("+campo_fecha+" TEXT, "+campo_mac+" TEXT, "+campo_magneto+" TEXT, "+campo_acele+" TEXT, "+campo_giro+" TEXT, "+campo_pasos+" TEXT)";
}
