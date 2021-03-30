package com.jyoc.jyocpruebamailconadjunto;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    Activity mainactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        confirmarQueExistenTodosEstosPermisos(this,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    void onCreate_conAccionesSoloConPermiso() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainactivity = this;
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bitmap bitmap = JYOCUtilsv4.tomarScreenShot(mainactivity);
                //File file = JYOCUtilsv4.salvarBitmapEnEnDirectorioExternoApp(Environment.DIRECTORY_PICTURES,bitmap,"ScreenShots2","pantallazo.png");
                //if(file!=null){
                //    Toast.makeText(getApplicationContext(), "Pantallazo creado", Toast.LENGTH_SHORT).show();
                //}
                //JYOCUtilsv4.Compartir(file,mainactivity);
                JYOCUtilsv4.enviarMailConFichero(mainactivity, "poipoi", "jkjk h", new String[]{});
                //JYOCUtilsv4.salvarStringEnDirectorioInternoApp(getApplicationContext(), "PEPEPEPE", "prubastxt.txt") ;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void confirmarQueExistenTodosEstosPermisos(Activity actividad, String... arrayPermisos) {
        boolean todosLosPemisosOk = true;
        for (String cadapermiso : arrayPermisos) {
            int permiso = ContextCompat.checkSelfPermission(actividad, cadapermiso);
            if (!(ContextCompat.checkSelfPermission(actividad, cadapermiso) == PackageManager.PERMISSION_GRANTED)) {
                todosLosPemisosOk = false;
            }
        }
        if (todosLosPemisosOk) {
            //-- RECORDAR HACER AQUI LO QUE SE DESEE CUANDO HAY PERMISOS   ---------------
            //-- LO NORMAL ES LLAMAR A UN METODO QUE COMPLETE EL ONCREATE  ---------------
             onCreate_conAccionesSoloConPermiso();
        } else {
            ActivityCompat.requestPermissions(this, arrayPermisos, PETICION_DE_PERMISOS);
        }
    }//
    /**
     * onRequestPermissionsResult
     * Complemento al metodo anterior (confirmarQueExistenTodosEstosPermisos) que solicita permisos
     * ESTE METODO HA DE COPAIRSE EN LA ACTIVIDAD QUE INCLUYE EL METODO ANTERIOR; NO VALE INVOCARLO
     * <p>
     * Recordar completar las acciones que se deseen realizar si se conceden o si no se conceden
     * los permisos
     * La concesion normalmente llama al mismo metodo que completa el OnCreate vista en el
     * metodo anterior (confirmarQueExistenTodosEstosPermisos)
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public static final int PETICION_DE_PERMISOS = 12321;

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PETICION_DE_PERMISOS) {
            if (grantResults.length > 0) {
                boolean todosLosPemisosOk = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (!(grantResults[i] == PackageManager.PERMISSION_GRANTED)) {
                        todosLosPemisosOk = false;
                    }
                }
                if (todosLosPemisosOk) {
                    //-- RECORDAR HACER AQUI LO QUE SE DESEE CUANDO HAY PERMISOS   ---------------
                    //-- LO NORMAL ES LLAMAR A UN METODO QUE COMPLETE EL ONCREATE  ---------------
                    onCreate_conAccionesSoloConPermiso();

                } else {
                    //-- RECORDAR HACER AQUI LO QUE SE DESEE SI NO HAY PERMISOS ---------------
                    finish(); // algun permiso no se otorgó, terminamos la actividad, no se deja seguir
                }
            } else {
                //-- RECORDAR HACER AQUI LO QUE SE DESEE SI NO HAY PERMISOS ---------------
                finish(); // se canceló al solicitar permisos, terminamos la actividad, no se deja seguir
            }
        }
    }// ---------------------------------------------------- FIN onRequestPermissionsResult

}
