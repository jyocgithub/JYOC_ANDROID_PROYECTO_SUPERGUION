package com.jyoc.jyocsqlite_guion;

public class Jugador {

    private int idJugador;
    private String nombre;
    private int puntos;

    public Jugador(int idJugador, String nombre, int puntos) {
        this.idJugador = idJugador;
        this.nombre = nombre;
        this.puntos = puntos;
    }

    public int getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(int idJugador) {
        this.idJugador = idJugador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
}
