package com.jyoc.jyocsqlite_guion;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

        // TRABAJAR CON LA BBDD: ACCEDER A ELLA Y EXECSQL()
        // ***************************************************
        /**
         * Creamos un objeto de nuestra clase de conexion (JYOCSQLiteHelper)
         * Aqui es donde indicamos el nombre de la BBDD y la version que debe abrir
         *
         * Parámetros;
         *  - Contexto de la aplicación (puede ser simplemente this, como referencia a la actividad principal),
         *  - Nombre de la base de datos,
         *  - Un objeto CursorFactory que por ahora no es necesario (poner valor null)
         *  - Versión de la base de datos
         *
         * El efecto de crear este objeto es
         *  - Si la base de datos ya existe y su versión coincide con la solicitada se realiza la conexión dicha version
         *  - Si la base de datos existe pero su versión actual es anterior a la solicitada, s
         *    se llama al método onUpgrade() y se conectará con la base de datos convertida.
         *  - Si la base de datos no existe, se llamará automáticamente al método onCreate()
         *    y se conectará con la base de datos creada.
         */
        MiClaseSqlite miClaseSqlite = new MiClaseSqlite(this, "JugadoresDB", null, 1);

        Jugador jug = new Jugador(323, "pepe", 100);
        miClaseSqlite.guardarJugador(jug);
        Jugador jug2 = new Jugador(222, "luis", 300);
        miClaseSqlite.guardarJugador(jug2);
        Jugador jug3 = new Jugador(11, "dani", 400);
        miClaseSqlite.guardarJugador(jug3);

        miClaseSqlite.borrarJugador(222);

        ArrayList<Jugador> listajugadoresleidos = miClaseSqlite.leerTodosLosJugadores();
        TextView et = (TextView) findViewById(R.id.etiqueta1);
        et.setText(listajugadoresleidos.size() + "");


        Jugador unjugador = miClaseSqlite.leerUnJugadoresPorSuNombre("pepe");
        if (unjugador != null) {
            et.setText(unjugador.getPuntos() + "");
        }


    }


}



