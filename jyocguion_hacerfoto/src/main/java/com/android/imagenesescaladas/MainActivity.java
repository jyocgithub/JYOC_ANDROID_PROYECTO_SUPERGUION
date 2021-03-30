package com.android.imagenesescaladas;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
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
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Bundle savedInstanceState;
    private static final int PETICION_DE_PERMISOS_DESDE_ONCREATE = 12321;
    private String[] arrayDePermisosSolicitados = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    protected void onCreate_conAccionesSoloConPermiso(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        SeekBar sb = findViewById(R.id.sbTamano);
        ImageView ivfoto = findViewById(R.id.ivFoto);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                // EN CASO 1             
                /*             Toast.makeText(getApplicationContext(), String.valueOf(seekBar.getProgress()), Toast.LENGTH_LONG).show();             BitmapFactory.Options bmOptions = new     BitmapFactory.Options();             bmOptions.inSampleSize = 100 / sb.getProgress();             imageBitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);             imageView.setImageBitmap(imageBitmap);             
                // 
                */
                // EN CASO 2
                int anchoEscalado = anchoOriginal * sb.getProgress() / 100;
                int altoEscalado = altoOriginal * sb.getProgress() / 100;
                imageBitmap = imageBitmap.createScaledBitmap(imageBitmap, anchoEscalado, altoEscalado, false);
                ivfoto.setImageBitmap(imageBitmap);


                // TODO Auto-generated method stub      
                Toast.makeText(getApplicationContext(), String.valueOf(seekBar.getProgress()), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), String.valueOf(seekBar.getProgress()), Toast.LENGTH_LONG).show();
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                int aspecto = 100 / sb.getProgress();
                bmOptions.inSampleSize = aspecto;
                imageBitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
                ivfoto.setImageBitmap(imageBitmap);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                // TODO Auto-generated method stub   
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // TODO Auto-generated method stub      


            }
        });
    }

    File fileDeimagenEnDirDeApp;

    private String crearStringUnicoConFechaDeHoy() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    //private File crearFicheroConFileProvider(String nombreFichero) throws IOException {
    //    File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    //    File fileDeimagen = new File(dir, nombreFichero);
    //    fileDeimagen.createNewFile();
    //
    //    File file = new File(getFilesDir().getAbsolutePath() + nombreFichero);
    //    String fileauthorities = BuildConfig.APPLICATION_ID + ".fileprovider";
    //    Uri uri = FileProvider.getUriForFile(getApplicationContext(), fileauthorities, file);
    //    return fileDeimagen;
    //}
    //
    //private void hacerFotoCompleta(String nombreFoto) {
    //    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    //    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
    //        startActivityForResult(takePictureIntent, TOMAR_FOTO);
    //    }
    //}

    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,   /* prefix */
                ".jpg",           /* suffix */
                storageDir       /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    Uri URIdeFichero = null;
    private File crearFileParaFicheroTemporal(String nombreFichero, String extension) throws IOException {
        // Crear objeto File del fichero definitivo, segun donde se quiera crear:
        // -- crear dichero temporal en la carpeta de fotos (temporal permite usar la imagen en nuestra app, pero que luego desaparece si la procesamos)
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File fichero = File.createTempFile(nombreFichero, extension, dir);
       
        return fichero;
    }
    private File crearFileParaFichero(String nombreFichero, String extension) throws IOException {
        // ----- Crear objeto File en directorio privado interno de la app
        File file = new File(getFilesDir(), nombreFichero + extension);

        return file;
    }


    private Uri crearUriConFileProvider(File fichero) throws IOException {
        if (fichero != null) {
            String fileauthorities = BuildConfig.APPLICATION_ID + ".fileprovider";
            URIdeFichero = FileProvider.getUriForFile(this, fileauthorities, fichero);
            return URIdeFichero;
        }
        return null;

        //
        //// Save a file: path for use with ACTION_VIEW intents
        //currentPhotoPath = fichero.getAbsolutePath();
        //return fichero;
    }

    static final int TOMAR_FOTO = 24351;
    Uri atributoUri = null;
    private void intentParaHacerFoto() throws IOException {
        //Uri photoURI = null;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File file = crearFileParaFicheroTemporal("otraimagen", ".png");
            if (file != null) {
                Uri uri = crearUriConFileProvider(file);
                atributoUri = uri;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(takePictureIntent, TOMAR_FOTO);
            }
        }
    }
    
    
    // ORIGINAL
    private void intentFoto() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            photoFile = createImageFile();
            if (photoFile != null) {
                String fileauthorities = BuildConfig.APPLICATION_ID + ".fileprovider";
                //String fileauthorities = BuildConfig.APPLICATION_ID + ".fileprovider";
                atributoUri = FileProvider.getUriForFile(this,
                        fileauthorities,
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, atributoUri);
                startActivityForResult(takePictureIntent, TOMAR_FOTO);
            }
        }
    }

    Bitmap imageBitmap = null;
    int anchoOriginal;
    int altoOriginal;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TOMAR_FOTO && resultCode == RESULT_OK) {
            try {
                // ------- 
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), atributoUri);


                anchoOriginal = imageBitmap.getWidth();
                altoOriginal = imageBitmap.getHeight();


                ImageView imagenEjemplo = findViewById(R.id.ivFoto);
                imagenEjemplo.setImageBitmap(imageBitmap);


                // ---------------    esto solo coge el thumbnail de la foto !!!!!!!!!!!!!!!!!!!!!!!!!!
                //Bundle extras = data.getExtras();
                //Bitmap imagenThumbNail = (Bitmap) extras.get("data");
                //ImageView imagenThumbNailEjemplo = findViewById(R.id.ivFotoThumb);
                //imagenThumbNailEjemplo.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onClickHacerFoto(View view) throws IOException {

        intentParaHacerFoto();

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
        setContentView(R.layout.activity_main);
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