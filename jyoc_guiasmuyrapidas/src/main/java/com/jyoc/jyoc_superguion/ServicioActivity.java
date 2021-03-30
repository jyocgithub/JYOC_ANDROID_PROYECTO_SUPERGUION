package com.jyoc.jyoc_superguion;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class ServicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio);

        // **************************************************
        //  RECORDAR REGISTRAR LOS ERVICIOS EN EL MANIFIESTO
        // ***************************************************
        // <application
        //  …
        //   <service
        //     android:name=".ServicioPrueba"
        //     android:enabled="true"
        //     android:exported="true" >     
        //   </service>
        //   ….
        //</application>

        
        
        // ************ LANZAMIENTO DE UN SERVICE
        Intent intent = new Intent(this, ServicioPrueba.class);
        startService(intent);

        // ************ LANZAMIENTO DE UN INTENT SERVICE
        // Si solo se va a usar un hilo dentro del servicio, o se desea que las sucesivas llamadas se ejecuten 
        // de modo encadenado (una tras otra) y no concurrentemente, entonces es más práctico usar IntentService, 
        // que se caracteriza por:
        // - Crea automáticamente un hilo, no hace falta que lo cree el usuario.
        // - Cada vez que se “inicia” el IntentService se encola, y no se ejecuta hasta que se hayan terminado 
        //   de ejecutar las anteriores “llamadas”
        // - El servicio se termina cuando se termina la ejecución de su contenido, no es necesario que explícitamente se llame a stopSelf() o stopService()
        //   Cuando se termina el servicio, se destruye igualmente el hilo correspondiente (sin intervención del usuario)
        Intent intentSer = new Intent(this, IntentServicePrueba.class);
        startService(intentSer);

        // ************ LANZAMIENTO DE UN FOREGROUND SERVICE
        // Un servicio en primer plano realiza una operación que el usuario puede notar. 
        // Por ejemplo, una aplicación de audio usa un servicio en primer plano para reproducir una pista de audio. 
        // Los servicios en primer plano deben mostrar una notificación
        // Se inician con el método startForegroundService
        // Desde Android 10, exige que se declare un permiso explícito en el manifest:
        //        <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
        Intent primerPlanoIntent = new Intent(this, ServicioPrimerPlano.class);
        //si queremos podemos pasarle alguna información.
        primerPlanoIntent.putExtra("ejemploDeDatoPasadoAlServicio", "Texto pasado al servicio");
        //Iniciamos el servicio con startForegroundService()
        ContextCompat.startForegroundService(this, primerPlanoIntent);
        //Se termina el servicio como cualquier  otro, con stopService()
        stopService(primerPlanoIntent);

    }
}

/**
 * ServicioPrueba
 * Clase que procesa un Servicio
 */
class ServicioPrueba extends Service {
    public ServicioPrueba() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        Log.d("", "Servicio creado...");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("", "Servicio iniciado…");

        new Thread(new Runnable() {
            @Override
            public void run() {

                // INSTRUCCIONES DEL SERVICIO 
                // EN ESTE CASO CON UN HILO EXTERNO (YA NO EL HILO PRINCIPAL)
                // ESTE HILO DEBE ACABAR POR SI MISMO

            }
        }).start();

        // En este caso, cerramos el servicio por que hay un hilo lanzado
        // Si no hubieramos lanzado el hilo, esta instruccion deberia controlarse cuando se lanza
        this.stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("", "Servicio destruido...");
    }
}

/**
 * IntentServicePrueba
 * Clase que procesa un IntentServicio
 */
class IntentServicePrueba extends IntentService {

    public IntentServicePrueba() {
        super("MiIntentService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // AQUI VIENEN LAS INSTRUCCIONES DEL INTENT SERVICE 
        // EN ESTE CASO NO NECESITA CREARSE EXPLICITAMENTE UN HILO PUES INTENT SERVICE LO CREA
        // A DIFERENCIA DE UN SERVICE, VARIAS LLAMADAS A UN INTENT SERVICE SE PROCESAN ENCADENANDAS, NO EN PARALELO
        // ESTE HILO-INTEMT-SERVICE TERMINA AUTOMATICAMENTE AL ACABAR ESTE METODO
    }
}

/**
 * ServicioPrimerPlano
 * Clase que procesa un ForegroundService
 */
class ServicioPrimerPlano extends Service {
    public static final String CHANNEL_ID = "ServicioPrimerPlanoChannel";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String texto = intent.getStringExtra("ejemploDeDatoPasadoAlServicio");
        crearUnNotificationChannel();  // desde ultimas versiones se necesita crear un canal para una notificacion
        Intent intentConNotificacion = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentConNotificacion, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Servicio Intent en primer plano... !!! ")
                .setContentText(texto)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        // AQUI DEBE VENIR LA CREACION DEL HILO CON EL TRABAJO A REALIZAR POR EL SERVICIO
        // AL IGUAL QUE CON UN SERVICIO NORMAL (RECORDAR AÑADIR EN ALGUN SITIO UN stopself() )
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void crearUnNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel( CHANNEL_ID,
                    "Canal de notificaciones para ServicioPrimerPlano",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}