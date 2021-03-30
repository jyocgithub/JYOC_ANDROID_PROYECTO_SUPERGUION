package com.jyoc.jyocasynctask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends Activity {
    private Button btInicio;
    private ProgressDialog pbProgressDialog1;
    private MiAsyncTaskConProgressBar miAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btInicio = findViewById(R.id.btInicio);
    }

    public void inicio_listener(View v) {
        // Creamos el progressDialog y le damos valores iniciales
        pbProgressDialog1 = new ProgressDialog(MainActivity.this);
        pbProgressDialog1.setMessage("");
        pbProgressDialog1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pbProgressDialog1.setMax(10);
        pbProgressDialog1.setCancelable(true);
        // Creamos nuestro objeto de AsyncTask
        miAsyncTask = new MiAsyncTaskConProgressBar();
        // y ejecutamos el hilo. Le pasamos un String de ejemplo por argumento
        miAsyncTask.execute("Descargando 10 ficheros...");
    }

    // Metodo que simula la descarga de un fichero, 
    // simplemente espera un tiempo aleatorio
    private void descargaFichero() {
        try {
            Thread.sleep(new Random().nextInt(1000) + 200);
        } catch (InterruptedException e) {
        }
    }
    
    private class MiAsyncTaskConProgressBar extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            // inicilizamos el progressDialog
            pbProgressDialog1.setProgress(0);
            pbProgressDialog1.show();
            // un listener para que si se cancela el progressDialog se cancele tb el hilo
            pbProgressDialog1.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // asi se indica al hilo que se ha pedido cancelarse. 
                    // se debe comprobar continuamente en el doInBackground si se ha 
                    // cancelado con el metodo isCancelled()
                    miAsyncTask.cancel(true);
                }
            });
            Toast.makeText(MainActivity.this, "Descarga iniciada...", Toast.LENGTH_LONG).show();
        }
        
        
        @Override
        protected Boolean doInBackground(String... params) {
            // recogemos el String enviado y lo ponemos de titulo al progressDialog
            pbProgressDialog1.setMessage(params[0]);
            // bucle que da 10 vueltas
            boolean seguir = true;
            for (int i = 1; i <= 10 && seguir; i++) {
                descargaFichero(); // en cada vuelta del bucle bajamos un archivo (simulamos)
                publishProgress(i); // y enviamos al publishProgress el indice del archivo bajado
                // es importante este if. Si no se pone, la cancelacion del hilo no se acomete 
                // sino que sigue normalmente y, al acabar, es cuando llama al onCancelled()
                if (isCancelled()) {
                    seguir = false;
                }
            }
            return seguir;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // en cada llamada desde doInBackground(), hacemos un cambio en el UI
            // Recordar que este metodo si se ejecuta en el hilo principal, por lo que 
            // SI actualiza la UI
            int progreso = values[0]; // cogemos el valor recibido
            pbProgressDialog1.setProgress(progreso); // actualizamos el progressDialog
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // solo decimos "terminada" si doInBackground devolvio true
            if (result) {
                pbProgressDialog1.dismiss();
                Toast.makeText(MainActivity.this, "Descarga terminada.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MainActivity.this, "Descarga cancelada...", Toast.LENGTH_SHORT).show();
        }
    }
}
