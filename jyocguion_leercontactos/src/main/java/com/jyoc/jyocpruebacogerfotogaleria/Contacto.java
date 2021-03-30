package com.jyoc.jyocpruebacogerfotogaleria;


import android.database.Cursor;
import android.graphics.Bitmap;

import java.util.List;

public class Contacto {
    private String id;
    private String nombre;
    private String segundonombre;
    private String apellidos;
    private String direccion;
    private String calle;
    private String ciudad;
    private String pais;
    private String cumpleanos;
    private String emailprincipal;
    private List<Telefono> listatelefonos;
    private Bitmap foto;
    private Cursor cursorContacto;  // el cursor que contiene la info del contacto, puede ser null
    

    public Contacto(String id, String nombre, String segundonombre, String apellidos,String calle,String ciudad,String pais,String cumpleanos, String emailprincipal,List<Telefono> listatelefonos, Bitmap foto) {
        this.id = id;
        this.nombre = nombre;
        this.segundonombre = segundonombre;
        this.apellidos = apellidos;
        this.calle = calle;
        this.ciudad = ciudad;
        this.pais = pais;
        this.cumpleanos = cumpleanos;
        this.emailprincipal= emailprincipal;
        this.listatelefonos = listatelefonos;
        this.foto = foto;
    }

    public Contacto() {
    }

    static class Telefono{
        String tipo;
        String numero;
        public Telefono(String tipo, String numero) {
            this.tipo = tipo;
            this.numero = numero;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSegundonombre() {
        return segundonombre;
    }

    public void setSegundonombre(String segundonombre) {
        this.segundonombre = segundonombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Telefono> getListatelefonos() {
        return listatelefonos;
    }

    public void setListatelefonos(List<Telefono> listatelefonos) {
        this.listatelefonos = listatelefonos;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }
}