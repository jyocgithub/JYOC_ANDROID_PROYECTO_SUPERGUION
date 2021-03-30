package com.example.jyoccanvas_paint_guion;


  import androidx.annotation.RequiresApi; 
import androidx.appcompat.app.AppCompatActivity;
 import androidx.constraintlayout.widget.ConstraintLayout;  
import android.content.Context; 
import android.graphics.Canvas; 
import android.graphics.Color; 
import android.graphics.Paint;
 import android.graphics.Path; 
import android.graphics.Shader; 
import android.graphics.SweepGradient;
 import android.os.Build; 
import android.os.Bundle; 
import android.view.Display; 
import android.view.View;  

public class MainActivity extends AppCompatActivity { 
    private Lienzo fondo;  
    
    @Override 
    protected void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); 
        setContentView(R.layout.activity_main);  
        
        ConstraintLayout constraintPrincipal = findViewById(R.id.constraintPrincipal); 
        fondo = new Lienzo(this);
          constraintPrincipal.addView(fondo); 
    }  

    class Lienzo extends View {

        
        public Lienzo(Context context) {super(context);}

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        protected void onDraw(Canvas canvas) {
            // Display display = getWindowManager().getDefaultDisplay(); 
            //       /*int x = display.getWidth();  // deprecated   
            //       int y = display.getHeight();  // deprecated*/            
            // nos guardamos el alto y ancho de la ventana principal (del canvas, de la tela del lienzo)        
            int ancho = canvas.getWidth();
            //
            int alto = canvas.getHeight();
            // Creamos un PINCEL para poder pintar con el      
            Paint pincel = new Paint();            // ponemos color de fondo, esto es, pintamos el propio canvas (la tela)    
            pincel.setColor(Color.GRAY);
            canvas.drawPaint(pincel);            //Indicamos el tipo de trazo de pincel  (FILL para rellenar ficguras, STROKE pora solo la linea externa)      
            pincel.setStyle(Paint.Style.FILL);            //indicamos el color del pincel     
            pincel.setColor(Color.BLUE);            //creamos una variable para el radio del círculo      
            int radio = 200;            // drawCircle pinra un circulo, diciendole: coordenadax, coordenaday, radio y pincel      
            canvas.drawCircle(ancho / 2, alto / 2, radio, pincel);            // cambiamos las caracteristicas del pincel para pintar otra cosa   
            pincel.setColor(Color.GREEN);
            radio = 100;
            canvas.drawCircle(ancho / 2, alto / 2, radio, pincel);            // cambiamos las caracteristicas del pincel para pintar otra cosa     
            pincel.setColor(Color.YELLOW);
            radio = 50;
            canvas.drawCircle(ancho / 2, alto / 2, radio, pincel);            //Rectángulo conociendo las coordenadas de las dos esuinas ( arribaInaquierda  y abajoderecha)         
            pincel.setColor(Color.parseColor("#1F618D"));
            int esquinaArribaIzquierdacoordenadaX = 100;
            int esquinaArribaIzquierdacoordenadaY = 100;
            int esquinaAbajoDerechacoordenadaX = 200;
            int esquinaAbajoDerechacoordenadaY = 200;
            canvas.drawRect(esquinaArribaIzquierdacoordenadaX, esquinaArribaIzquierdacoordenadaY, esquinaAbajoDerechacoordenadaX, esquinaAbajoDerechacoordenadaY, pincel);  //Rectángulo conociendo la  coordenadas  arribaInaquierda  y el amncho y alto     
            pincel.setColor(Color.MAGENTA);
            esquinaArribaIzquierdacoordenadaX = 400;
             esquinaArribaIzquierdacoordenadaY = 100;
            int anchorect = 300;int altorect = 200;
            esquinaAbajoDerechacoordenadaX = esquinaArribaIzquierdacoordenadaX + anchorect;
            esquinaAbajoDerechacoordenadaY = esquinaArribaIzquierdacoordenadaY + altorect;
            canvas.drawRect(esquinaArribaIzquierdacoordenadaX, esquinaArribaIzquierdacoordenadaY, esquinaAbajoDerechacoordenadaX, esquinaAbajoDerechacoordenadaY, pincel);
            //un Path es un TRAZO DEL PINCEL        
            Path trazo = new Path();
            trazo.moveTo(400, 300);  // coordenadas del punto inicial desde donde empiezo a mover el pincel    
            trazo.lineTo(500, 400);   // mover el pincel hasta las coordenadas indicadas      
            trazo.lineTo(300, 400);     // mover el pincel hasta las coordenadas indicadas   
            trazo.lineTo(400, 300);     // mover el pincel hasta las coordenadas indicadas      
            canvas.drawPath(trazo, pincel);            // un Path es un TRAZO DEL PINCEL       
            Path trazo2 = new Path();pincel.setStyle(Paint.Style.STROKE);
            pincel.setStrokeWidth(10);   // esto pone el grosor de la linea       
            trazo2.moveTo(600, 300);  // coordenadas del punto inicial desde donde empiezo a mover el pincel     
            trazo2.lineTo(700, 400);   // mover el pincel hasta las coordenadas indicadas        
            trazo2.lineTo(500, 400);     // mover el pincel hasta las coordenadas indicadas    
            trazo2.lineTo(600, 300);     // mover el pincel hasta las coordenadas indicadas    
            canvas.drawPath(trazo2, pincel);            // pintar una línea        
            pincel.setStrokeWidth(10);pincel.setColor(Color.RED);
            canvas.drawLine(100, 30, 1000, 30, pincel);            // pintar un ovalo, se le indican las coordenadas del RECTANGULO QUE HACE DE MARCO Y QUE CONTIENE EL OVALO        
            pincel.setColor(Color.RED);
            esquinaArribaIzquierdacoordenadaX = 400;
            esquinaArribaIzquierdacoordenadaY = 100;
            anchorect = 300;
            altorect = 200;
            esquinaAbajoDerechacoordenadaX = esquinaArribaIzquierdacoordenadaX + anchorect;
            esquinaAbajoDerechacoordenadaY = esquinaArribaIzquierdacoordenadaY + altorect;
            canvas.drawOval(esquinaArribaIzquierdacoordenadaX, esquinaArribaIzquierdacoordenadaY, esquinaAbajoDerechacoordenadaX, esquinaAbajoDerechacoordenadaY, pincel);
            //ovalo //Dibujo con Shade, intsnaciamos a SweepGradient            
            // Realizamos un array de colores      
            int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.BLACK};
            // indicamos a shadercx,cy,lista de colores,posición         
            Shader shader = new SweepGradient(ancho / 2, (alto / 2) + 260, colors, null);
            //Shader shader = new SweepGradient(ancho/2, alto/2+260, colors, null);            
            // indicamos que paint aplique el shader (degradado)       
            pincel.setShader(shader);
            canvas.drawOval((ancho / 2) - 400, 1000, (alto / 2), 1200, pincel);
            pincel.setShader(null);
            pincel.setColor(Color.GREEN);
            radio = 100;
            int posx = 100;
            int posy = 1400;
            for (int x = 0; x < 10; x++) {
                canvas.drawCircle(posx, posy, radio, pincel);
                posx = posx + 100;
            }
        }
    }
}
