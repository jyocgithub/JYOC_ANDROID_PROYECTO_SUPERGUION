package com.android.imagenesescaladas;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

public class HacerFotoActivity extends AppCompatActivity {

    private Bundle savedInstanceState;
    private static final int PETICION_DE_PERMISOS_DESDE_ONCREATE = 12321;
    private String[] arrayDePermisosSolicitados = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    protected void onCreate_conAccionesSoloConPermiso(Bundle savedInstanceState) {
        SeekBar sb = findViewById(R.id.sbTamano);
        ImageView ivfoto = findViewById(R.id.ivFoto);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            Toast.makeText(getApplicationContext(), String.valueOf(seekBar.getProgress()), Toast.LENGTH_LONG).show();            
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
        });
    }


    Uri URIdeFichero = null;
    Uri atributoUri = null;
    Bitmap imagenCompletaBitmap = null;
    static final int TOMAR_FOTO = 24351;
    
    public void onClickHacerFoto(View view) throws IOException {
        intentParaHacerFoto("fotopru", ".png");
    }

    private void intentParaHacerFoto(String nombrefoto, String extension) throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if (takePictureIntent.resolveActivity(getPackageManager()) != null) {  // Confirma que haya una app que pueda hacer unaa foto
            File file = crearFileParaFicheroTemporal(nombrefoto, extension);
            if (file != null) {
                Uri uri = crearUriConFileProvider(file);
                atributoUri = uri;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(takePictureIntent, TOMAR_FOTO);
            }
        //}
    }
    
    private File crearFileParaFicheroTemporal(String nombreFichero, String extension) throws IOException {
        // Crear objeto File del fichero definitivo, segun donde se quiera crear:
        // -- crear dichero temporal en la carpeta de fotos (temporal permite usar la imagen en nuestra app, pero que luego desaparece si la procesamos)
        File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File fichero = File.createTempFile(nombreFichero, extension, dir);
        return fichero;
    }

    private Uri crearUriConFileProvider(File fichero) throws IOException {
        if (fichero != null) {
            String fileauthorities = BuildConfig.APPLICATION_ID + ".fileprovider";
            String currentPhotoPath = fichero.getAbsolutePath();
            URIdeFichero = FileProvider.getUriForFile(this, fileauthorities, fichero);
            return URIdeFichero;
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TOMAR_FOTO && resultCode == RESULT_OK) {
            try {
                // ------- ASI RECOGEMOS LA FOTO COMPLETA EN UN BITMAP
                imagenCompletaBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), atributoUri);
                
                String kk = atributoUri.getEncodedPath();

                // ------- ASI SI SOLO QUEREMOS EL THUMBNAIL
                //Bundle extras = data.getExtras();
                //Bitmap imagenThumbNail = (Bitmap) extras.get("data");
                
                // Ejemplo de poner la foto en un imageview
                ImageView ivFoto = findViewById(R.id.ivFoto);
                ivFoto.setImageBitmap(imagenCompletaBitmap);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * onCreate
     * Sustituye al onCreate original de la actividad
     * Cuidado que si esta no es MainActivity hay que modificar el setContentView
     *
     * @param savedInstanceState  Bundle con el estado de la actividad
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_hacerfoto);
        confirmarQueExistenTodosEstosPermisos(this, savedInstanceState, arrayDePermisosSolicitados);
    }//  FIN nuevo onCreate

    /**
     * confirmarQueExistenTodosEstosPermisos
     * <p>
     * Solicita una serie de permisos si no se han concedido previamente por el usuario
     *
     * @param actividad            actividad donde se piden los permisos
     * @param savedInstanceState   bundle con el estado de la actividad
     * @param arrayPermisos        array con los permisos solicitados
     */
    public void confirmarQueExistenTodosEstosPermisos(Activity actividad, Bundle savedInstanceState, String... arrayPermisos) {
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
            onCreate_conAccionesSoloConPermiso(savedInstanceState);
        } else {
            ActivityCompat.requestPermissions(actividad, arrayPermisos, PETICION_DE_PERMISOS_DESDE_ONCREATE);
        }
    }//  FIN confirmarQueExistenTodosEstosPermisos

    /**
     * onRequestPermissionsResult
     * Complemento al metodo confirmarQueExistenTodosEstosPermisos, que solicita permisos
     * <p>
     * Recordar modificar si se desea las acciones que se deseen realizar si se conceden
     * o si no se conceden los permisos
     * La correcta concesion de permisos, actualmente,  llama al antiguo OnCreate que
     * se renombro como     onCreate_conAccionesSoloConPermiso
     *
     * @param requestCode  codigo de quien solicito los permisos
     * @param permissions  array de permisos que se han pedido al usuario
     * @param grantResults array de respuestas del usuario a los permisos pedidos
     */
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PETICION_DE_PERMISOS_DESDE_ONCREATE) {
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
                    onCreate_conAccionesSoloConPermiso(savedInstanceState);

                } else {
                    //-- RECORDAR HACER AQUI LO QUE SE DESEE SI NO HAY PERMISOS ---------------
                    finish(); // algun permiso no se otorgó, terminamos la actividad, no se deja seguir
                }
            } else {
                //-- RECORDAR HACER AQUI LO QUE SE DESEE SI NO HAY PERMISOS ---------------
                finish(); // se canceló al solicitar permisos, terminamos la actividad, no se deja seguir
            }
        }
    }//  FIN onRequestPermissionsResult

}