package com.jyoc.jyocpruebacogerfotogaleria;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;

public class GestionImagenes {

    // ---------------- CONSTANTES DE ACCION DE CADA METODO ---------------------
    public static final int CONSTANTE_TOMAR_FOTO_PARA_THUMBNAIL = 17742;
    public static final int ELEGIR_IMAGEN_DE_SISTEMA_DE_ARCHIVOS = 234;
    public static final int ELEGIR_IMAGEN_DE_CUALQUIER_APP = 7345;

    
    // =====================================================
    // ====     elegirImagenDeSistemaDeArchivos       ======
    // =====================================================
    public static void elegirImagenDeSistemaDeArchivos(Activity actividad) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        actividad.startActivityForResult(intent, ELEGIR_IMAGEN_DE_SISTEMA_DE_ARCHIVOS);
    }


    
    // =====================================================
    // ====     hacerFotoSoloParaThumbnail       ======
    // =====================================================
    public static void hacerFotoSoloParaThumbnail(Activity actividad) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // el IF siguiente comprueba que haya una aplicacion en el movil que pueda hacer fotos
        if (takePictureIntent.resolveActivity(actividad.getPackageManager()) != null) {
            actividad.startActivityForResult(takePictureIntent, CONSTANTE_TOMAR_FOTO_PARA_THUMBNAIL);
        }
    }
    


    // =====================================================
    // ====     elegirImagenDeCualquierAplicacion     ======
    // =====================================================
    public static void elegirImagenDeCualquierAplicacion(Activity actividad) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Seleccionar Aplicacion");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        actividad.startActivityForResult(chooserIntent, ELEGIR_IMAGEN_DE_CUALQUIER_APP);
    }
    
    
}
