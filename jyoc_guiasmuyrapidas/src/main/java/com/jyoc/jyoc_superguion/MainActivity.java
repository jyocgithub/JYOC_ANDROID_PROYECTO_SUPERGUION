package com.jyoc.jyoc_superguion;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;


//import com.android.jyocguion_broadcats_bateria.BroadcastNivelBateria;
//import com.android.jyocguion_broadcats_bateria.BroadcastNivelBateria;
import com.google.android.material.snackbar.Snackbar;
import com.jyoc.jyoc_superguion.databinding.ActivityMainBinding;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding vistas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**************************
         **************************
         ****   VIEW BINDING    ***
         **************************
         **************************/
        // --------------------------------------------- esto en el build.gradle
        //viewBinding {
        //    enabled = true
        //}

        // setContentView(R.layout.activity_main); // ------- quitar esto
       // private ActivityMainBinding vistas;  // ESTO COMO ATRIBUTO DE CLASE
        vistas = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(vistas.getRoot());

        /**************************
         **************************
         ****   INTENT          ***
         **************************
         **************************/
        //Intent miIntent = new Intent(MainActivity.this, SegundaActivity.class);
        //miIntent.putExtra("nombre", "Juan");
        //startActivity(miIntent);


        /**************************
         **************************
         ****   TOAST   ***
         **************************
         **************************/
        Toast.makeText(this, "Mensaje", Toast.LENGTH_LONG).show();

        /**************************
         **************************
         ****    SNACKBAR  ***
         **************************
         **************************/
        //Snackbar.make(vistas.tvCabecera, "Mensaje", Snackbar.LENGTH_LONG).show();

        Snackbar.make(vistas.tvCabecera, "Esto es Snackbar mas COMPLEJO !!!", Snackbar.LENGTH_INDEFINITE)
                // esto es opcional, y pone un color al texto con enlace que dispara la accion 
                .setActionTextColor(Color.CYAN)
                // Esto muestra un texto con enlace que ejecuta la accion de su OnClickListener()  
                .setAction("Nueva Acción", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // COLOCAR AQUI LA ACCION QUE SE DESEE  
                    }
                }).show();


        // ***************** LISTENERS PARA OTRAS ACTIVIDADES
        vistas.btAgregarViews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AgregarViewsActivity.class));
            }
        });
        vistas.btPreferencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SharedPreferenciasActivity.class));
            }
        });
        vistas.btServicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ServicioActivity.class));
            }
        });
        vistas.btContentProviderContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ContentProviderContactosActivity.class));
            }
        });
        // EL NIVEL DE BATERIA ES UN MODULO NUEVO APARTE
        //vistas.btBroadcastBateria.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        startActivity(new Intent(MainActivity.this, BroadcastNivelBateria.class));
        //    }
        //});

    }


    public void listenerCambioTexto() {

        vistas.tvCabecera.addTextChangedListener(new TextWatcher() {
            // Este método se dispara con cada cambio en el texto del view
            // insicando que se ha modificado el String s,
            // se han reemplazado count caractéres
            // comenzando en start y han reemplazado before número caracteres
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Se han reemplazado de '" + s + "'" + count
                        + " caracteres a partir de " + start +
                        " que antes ocupaban " + before + " posiciones");
            }

            //Este método te avisa de que en la cadena s,
            // los count caracteres empezando en start,
            // están a punto de ser reemplazos por nuevo texto con longitud after

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("Se van a reemplazar de '" + s + "'" + count +
                        " caracteres a partir de " + start +
                        " por texto con longitud " + after);
            }

            //Este método te avisa de que,en algún
            //punto de la cadena s, el texto ha cambiado.
            public void afterTextChanged(Editable s) {
                System.out.println("Tu texto tiene " + s.length() + " caracteres");
            }
        });
    }



}

