package com.jyoc.jyocpruebacogerfotogaleria;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    ImageView ivPrueba;
    public final int SELECT_DOCUMENTO = 654;
    public ArrayList<Contacto> listaContactos = new ArrayList<>();
    MainActivity mainActivity;
    TextView txtprogress;
    //GestionImagenes gestionImagenes = new GestionImagenes();
    //GestionContactos gestionContactos = new GestionContactos();

    public void onCreate_conAccionesSoloConPermiso(Bundle savedInstanceState) {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainActivity = this;
        ivPrueba = findViewById(R.id.ivPrueba);
        txtprogress = findViewById(R.id.txt_progress);

        findViewById(R.id.button_camara).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GestionImagenes.hacerFotoSoloParaThumbnail(mainActivity);
            }
        });

        findViewById(R.id.button_galeria).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GestionImagenes.elegirImagenDeCualquierAplicacion(mainActivity);
            }
        });

        findViewById(R.id.button_fichero).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GestionImagenes.elegirImagenDeSistemaDeArchivos(mainActivity);
            }
        });

        findViewById(R.id.button_contacto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GestionContactos.elegirTelefonoDeUnContacto(mainActivity);
            }
        });

        findViewById(R.id.button_leertodoslosconrtactos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txtProgress = findViewById(R.id.txt_progress);
                new LeerContactosAsyncTask(MainActivity.this, listaContactos, txtProgress).execute("");
            }
        });
        findViewById(R.id.bt_EditarContarcto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txtProgress = findViewById(R.id.txt_progress);
                Contacto contactoresultante = new Contacto();
                new LeerUnContactoAsyncTask(MainActivity.this, "1", contactoresultante, txtProgress).execute("");
            
            }
        });
    }

    public void mostrarContactosTrasHiloLectura() {
        ivPrueba.setImageBitmap(listaContactos.get(1).getFoto());
    }




    // --------------------- tras volver de llamadas a otras apps................
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GestionImagenes.CONSTANTE_TOMAR_FOTO_PARA_THUMBNAIL:
                Bundle extras = data.getExtras();
                Bitmap fotoTomada = (Bitmap) extras.get("data");
                ivPrueba.setImageBitmap(fotoTomada);
                break;
            case GestionImagenes.ELEGIR_IMAGEN_DE_SISTEMA_DE_ARCHIVOS:
            case GestionImagenes.ELEGIR_IMAGEN_DE_CUALQUIER_APP:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    ivPrueba.setImageURI(uri);
                    //String[] projection = {MediaStore.Images.Media.DATA};
                    //Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    //cursor.moveToFirst();
                    //int columnIndex = cursor.getColumnIndex(projection[0]);
                    //String filepath = cursor.getString(columnIndex);
                    //cursor.close();
                    //Bitmap bitmap = BitmapFactory.decodeFile(filepath);
                    //Drawable drawable = new BitmapDrawable(bitmap);
                    //ivPrueba.setImageURI(uri);
                }
                break;
            case GestionContactos.CONSTANTE_ELEGIR_TELEFONO_DEUN_CONTACTO:
                Uri contactData = data.getData();
                long elid = ContentUris.parseId(contactData);
                Cursor c = getContentResolver().query(contactData, null, null, null, null);
                if (c.moveToFirst()) {
                    String nombreColumnaTELEFONO = ContactsContract.CommonDataKinds.Phone.NUMBER;
                    int phoneIndex = c.getColumnIndex(nombreColumnaTELEFONO);
                    String num = c.getString(phoneIndex);
                    txtprogress.setText("Numero de telefono elegido=" + num);
                }
                break;
        }
    }


    // **********************+++++++++++++++++++++++***************************************
    // **                     INICIO BLOQUE PETICION PERMISOS                            **
    // ********++++++++++++++++++++++++****************************************************
    //
    // Procedimiendo de uso (en la primera actividad de la aplicacion):
    // 1.- Renombrar el método onCreate(savedInstanceState);  como    
    //        onCreate_conAccionesSoloConPermiso(savedInstanceState);
    // 2.- Quitar las lineas     
    //        super.onCreate(savedInstanceState);     
    //        setContentView(R.layout.activity_main);
    // 3.- Copiar este bloque entero en esa misma primera actividad de la aplicacion y descomentarlo
    // 4.- Modificar el arrayDePermisosSolicitados para incluir solo los que se necesita pedir
    // 5.- Añadir esos mismos permisos en el Android.Manifest con lineas como estas: 
    //        <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>

    private Bundle savedInstanceState;
    private static final int PETICION_DE_PERMISOS_DESDE_ONCREATE = 12321;
    private String[] arrayDePermisosSolicitados = {
            Manifest.permission.READ_CONTACTS};

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
    //**********************+++++++++++++++++++++++***************************************
    //**            FIN BLOQUE PETICION PERMISOS                                        **
    //********++++++++++++++++++++++++****************************************************


}

//
//class Contacto {
//    private String id, nombre, telefono, etiqueta;
//    private BitMap foto;
//
//    Contacto(String id, String nombre, String telefono, String etiqueta) {
//        this.id = id;
//        this.nombre = nombre;
//        this.telefono = telefono;
//        this.etiqueta = etiqueta;
//    }
//
//    @Override
//    public String toString() {
//        return nombre + " | " + etiqueta + " : " + telefono;
//    }
//}


//public Uri getPhotoUri() {
//    try {
//        Cursor cur = this.ctx.getContentResolver().query(
//                ContactsContract.Data.CONTENT_URI,
//                null,
//                ContactsContract.Data.CONTACT_ID + "=" + this.getId() + " AND "
//                        + ContactsContract.Data.MIMETYPE + "='"
//                        + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'", null,
//                null);
//        if (cur != null) {
//            if (!cur.moveToFirst()) {
//                return null; // no photo
//            }
//        } else {
//            return null; // error in cursor process
//        }
//    } catch (Exception e) {
//        e.printStackTrace();
//        return null;
//    }
//    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
//            .parseLong(getId()));
//    return Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
//}


//
//
//
//
//public void getTodosLosContactos() {
//    Cursor cursorDeTodosLosContactos =
//            getContentResolver()
//                    .query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
//                            null);
//
//    if (cursorDeTodosLosContactos != null && cursorDeTodosLosContactos.getCount() > 0) {
//        while (cursorDeTodosLosContactos.moveToNext()) {
//
//            long idDeUnContacto = cursorDeTodosLosContactos.getLong(cursorDeTodosLosContactos.getColumnIndex(ContactsContract.Contacts._ID));
//
//            // obtener datos de un contacto
//            Cursor cursorDeUnContacto = getContentResolver().query(ContactsContract.Data.CONTENT_URI,
//                    null,
//                    ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?",
//                    new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, Long.valueOf(idDeUnContacto).toString()},
//                    null);
//            if (cursorDeUnContacto != null) {
//                if (cursorDeUnContacto.moveToFirst()) {
//
//                    String nombreColumnaTELEFONO = ContactsContract.CommonDataKinds.Phone.NUMBER;
//                    int phoneIndex = cursorDeUnContacto.getColumnIndex(nombreColumnaTELEFONO);
//                    String num = cursorDeUnContacto.getString(phoneIndex);
//                    Toast.makeText(this, "Numero de telefono elegido=" + num, Toast.LENGTH_LONG).show();
//
//
//                    String displayName = cursorDeUnContacto.getString(cursorDeUnContacto.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                    if (displayName == null)
//                        displayName = "";
//
//                    String firstName = cursorDeUnContacto.getString(cursorDeUnContacto.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
//                    if (firstName == null)
//                        firstName = "";
//                    //Log.d("--> ", firstName.length()>0?firstName:displayName);
//
//                    String middleName = cursorDeUnContacto.getString(cursorDeUnContacto.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME));
//                    if (middleName == null)
//                        middleName = "";
//
//                    String lastName = cursorDeUnContacto.getString(cursorDeUnContacto.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));
//                    if (lastName == null)
//                        lastName = "";
//                    lastName = middleName + (middleName.length() > 0 ? " " : "") + lastName;
//
//
//                    String phone1 = "";
//                    if (cursorDeUnContacto.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER) > 0) {  // tiene telefonos (1 al menos)
//
//                        phone1 = cursorDeUnContacto.getString(cursorDeUnContacto.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//
//                    }
//
//
//                    Log.d("--> ", lastName + phone1);
//                }
//                cursorDeUnContacto.close();
//            }
//
//            Bitmap photo = null;
//
//            try {
//                InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
//                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(idDeUnContacto)));
//
//                if (inputStream != null) {
//                    photo = BitmapFactory.decodeStream(inputStream);
//                }
//
//                if (inputStream != null)
//                    inputStream.close();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            //Log.d("--> ", photo);
//        }
//    }
//
//    if (cursorDeTodosLosContactos != null) {
//        cursorDeTodosLosContactos.close();
//    }
//}
