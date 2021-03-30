package com.jyoc.jyoc_superguion;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class AgregarViewsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_views);

        // PARTIMOS DE QUE EN EL LAYPUT EXISTE UN LINERLAYOUT VACIO
        LinearLayout linearmain = findViewById(R.id.linearLayoutPrincipal);

        // **************************************************************************************************
        // *** PASO 1 ***  CREAR UN NUEVO LAYOUT Relative por ejemplo, para ponerlo dentro del linear original
        // **************************************************************************************************
        RelativeLayout nuevoRelativeLayout = new RelativeLayout(this);
        nuevoRelativeLayout.setBackgroundColor(Color.CYAN);

        // *** 1.1 CREAR PARAMETROS DEL NUEVO LAYOUT
        // Segun el diagrama de herencia, se pueden extraer los parámetros de un view con distintas clases:
        // OPCION 1:
        //    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // OPCION 2:
        RelativeLayout.LayoutParams paramsRelLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        // *** 1.2 AÑADIMOS LOS PARAMETROS AL NUEVO LAYOUT Y ESTE LOS AGREGAMOS AL LAYOPUT INICIAL
        // OPCION 1: ambas cosas en instrucciones separadas
        //   nuevoRelativeLayout.setLayoutParams(paramsRelLayout);
        //   linearmain.addView(nuevoRelativeLayout);
        // OPCION 2: ambas cosas en una sola instruccion
        linearmain.addView(nuevoRelativeLayout, paramsRelLayout);

        // **************************************************************************************************
        // *** PASO 2 ***    CREAR ELEMENTOS PARA AÑADIR AL NUEVO LAYOUT
        // **************************************************************************************************
        // *** 2.1 Crear boton1 y boton2 y colocarlos
        final Button boton1 = new Button(this);
        final Button boton2 = new Button(this);
        boton1.setId(View.generateViewId());
        boton2.setId(View.generateViewId());

        nuevoRelativeLayout.addView(boton1);
        nuevoRelativeLayout.addView(boton2);

        boton1.setText("Texto boton1");
        boton2.setText("Texto boton2");

        // *** 2.2 Aplicamos atributos de posición. 
        // Opcion 1, sacando los atributos que ya tenga el boton
        // (cuidado, para esto hemos de haber añadido ya el view a su contenedor)
        final RelativeLayout.LayoutParams paramsDelBoton2 = (RelativeLayout.LayoutParams) boton2.getLayoutParams(); 
        // Opción 1 BIS:
        //      final LayoutParams paramsDelBoton2 = (LayoutParams) boton2.getLayoutParams();     // aplicamos atributos de posición. 
        // Opción 2, creando un nuevo grupo de atributos, algo peor puede ser,  si ya hubiéramos aplicado alguno, los perderíamos
        //      final RelativeLayout.LayoutParams paramsDelBoton2 = 
        //              new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        // *** 2.3 Añadimos reglas como atributos de colocacion
        // ponemos el segundo boton debajo del primero y a la derecha de este
        paramsDelBoton2.addRule(RelativeLayout.BELOW, boton1.getId());
        paramsDelBoton2.addRule(RelativeLayout.END_OF, boton1.getId());
     
        // *** 2.4 Aplicamos nuevamente los parametros modificados
        boton2.setLayoutParams(paramsDelBoton2);

        
        // Ahora ya añadimos mas cosas a cada boton, a nuestro gusto, 
        // por ejemplo, un listener al boton2
        boton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ejemplo de como eliminar una rule a unos parametros que ya existen
                // quitamos que el segundo boton este a la derecha del primero
                paramsDelBoton2.removeRule(RelativeLayout.END_OF);
                boton2.setLayoutParams(paramsDelBoton2);
            }
        });

        // aunque tambien hay muchisimas propiedades que se pueden aplicar directamente a una vista sin
        // pasar por los atributos del layout, claro.
        boton2.setPadding(0, 7, 10, 7);
        boton2.setTypeface(null, Typeface.BOLD);
        boton2.setTextColor(Color.BLACK);
        boton2.setBackgroundColor(Color.WHITE);
        boton2.setGravity(Gravity.CENTER);
        

    }
}