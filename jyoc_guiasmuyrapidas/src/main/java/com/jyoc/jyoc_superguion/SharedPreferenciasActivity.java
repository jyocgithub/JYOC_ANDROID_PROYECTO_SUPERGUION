package com.jyoc.jyoc_superguion;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SharedPreferenciasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_con_preferencias);
    }

    // USANDO SHARED PREFERENCES CON UN NOMBRE PROPIO
    // SE PUEDEN CREAR MUCHOS "PAQUETES" DE SHARED PREFERENCES PROPIOS 
    // ********************************************************************************
    
    public void leerPreferenciasDeLaAplicacion() {
        SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        // Si no existe el valor, se devuelve el segundo parametro       
        String correo = prefs.getString("mail", "por_defecto @gmail.com");
        int numero = prefs.getInt("nombreDelValorIntAlmacenado", 10);
    }

    public void guardarPreferenciasDeLaAplicacion() {
        SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        // GUARDAR o AÑADIR NUEVOS valores a preferencias        
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("mail", "pruebas@gmail.com");
        editor.putInt("edad", 33);
        editor.commit();
    }
    public void borrarPreferenciasDeLaAplicacion() {
        SharedPreferences prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        // BORRAR preferencias                                                          
        // Asi eliminamos TODAS las preferencias del grupo prefs: 
        editor.clear();
        editor.commit();
        // y así eliminamos SOLO la preferencia titulo del grupo prefs: 
        editor.remove("titulo");
        editor.commit();
    }


    // USANDO SHARED PREFERENCES CON UN NOMBRE POR DEFECTO APLICADO POR ANDROID
    // SOLO SE PUEDE USAR UN "PAQUETES" DE SHARED PREFERENCES POR DEFECTO EN TODA LA APLICACION 
    // ES EL PAQUETE QUE SE USA SI EXISTE UAN ACTIVIDAD DE PREFERENCIAS
    // ********************************************************************************

    public void leerPreferenciasPorDefectoDeLaAplicacion() {
        SharedPreferences preferencias;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // RECORDAR AGREGAR DEPENDENCIA 
            //     implementation "androidx.preference:preference:1.1.0"
            preferencias = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        } else {
            preferencias = android.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        }
        String nombre = preferencias.getString("nombreDelValorAlmacenado", "valorPorDefectoSiNoExiste");
        int numero = preferencias.getInt("nombreDelValorIntAlmacenado", 10);
    }

    public void guardarPreferenciasPorDefectoDeLaAplicacion() {
        SharedPreferences preferencias;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // RECORDAR AGREGAR DEPENDENCIA 
            //     implementation "androidx.preference:preference:1.1.0"
            preferencias = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        } else {
            preferencias = android.preference.PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        }
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("nombreDelValorAlmacenado", "Valor");
        editor.putInt("nombreDelValorIntAlmacenado", 14);
        editor.commit();
    }
}