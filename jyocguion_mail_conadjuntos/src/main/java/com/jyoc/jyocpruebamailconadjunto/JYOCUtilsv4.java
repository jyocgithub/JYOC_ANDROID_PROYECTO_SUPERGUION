package com.jyoc.jyocpruebamailconadjunto;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class JYOCUtilsv4 {


    /**
     * ***************************************************************************
     * ************            PETICION DE PERMISOS                 **************
     * ***************************************************************************
     */

    /**
     * confirmarQueExistenTodosEstosPermisos
     * ESTE METODO HA DE COPIARSE EN LA ACTIVIDAD QUE INICIA LA APLICACION
     * NO VALE INVOCARLO CON UN OBJETO DE ESTA CLASE
     * <p>
     * Solicita una serie de permisos si no se han concedido previamente por el usuario
     * <p>
     * Para pedir permisos de modo sencillo, el OnCreate debe llamar inicialmente a este método.
     * El resto del OnCreate debe extraerse y colocarse en otro metodo, como por ejemplo, el que se
     * indica en este ejemplo, onCreate_conAccionesSoloConPermiso();
     * Ademas de esto se ha de añadir onRequestPermissionsResult(), que igualmente usa este
     * añadido al OnCreate()
     * <p>
     * Los distintos permisos que recibe el metodo se añaden como Strings continuados, que se
     * añadena a la misma llamada al método, seria algo asi:
     * confirmarQueExistenTodosEstosPermisos(this, Manifest.permission.INTERNET,
     * Manifest.permission.WRITE_EXTERNAL_STORAGE,
     * Manifest.permission.READ_CONTACTS,
     * Manifest.permission.WRITE_CONTACTS,
     * Manifest.permission.READ_EXTERNAL_STORAGE);
     * RECORDAR QUE TODOS LOS PERMISOS QUE SE SOLICITEN EN ESTE METODO HAN DE ESTAR IGUALMENTE
     * AÑADIDOS EN EL MANIFEST, CON LINEAS COMO
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
     *
     * @param actividad
     * @param arrayPermisos
     */
    public static final int PETICION_DE_PERMISOS = 12321;

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
            // onCreate_conAccionesSoloConPermiso();
        } else {
            ActivityCompat.requestPermissions(actividad, arrayPermisos, PETICION_DE_PERMISOS);
        }
    }// ---------------------------------------------------- FIN confirmarQueExistenTodosEstosPermisos

    /**
     * onRequestPermissionsResult
     * Complemento al metodo anterior (confirmarQueExistenTodosEstosPermisos) que solicita permisos
     * ESTE METODO HA DE COPIARSE EN LA ACTIVIDAD INICIAL, DONDE SE INCLUYE EL METODO ANTERIOR;
     * NO VALE INVOCARLO CON UN OBJETO DE ESTA CLASE
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
    //DESCOMENTAR AL COPIAR EN LA ACTIVIDAD INICIAL DE LA APLICACION
    //public static final int PETICION_DE_PERMISOS = 12321;
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
                    //onCreate_conAccionesSoloConPermiso();

                } else {
                    //-- RECORDAR HACER AQUI LO QUE SE DESEE SI NO HAY PERMISOS ---------------
                    //finish(); // algun permiso no se otorgó, terminamos la actividad, no se deja seguir
                }
            } else {
                //-- RECORDAR HACER AQUI LO QUE SE DESEE SI NO HAY PERMISOS ---------------
                //finish(); // se canceló al solicitar permisos, terminamos la actividad, no se deja seguir
            }
        }
    }// ---------------------------------------------------- FIN onRequestPermissionsResult


    /**
     * ***************************************************************************
     * ************            PREFERENCIAS                         **************
     * ***************************************************************************
     */
    //:::::::::::::::::::::::::::::::  Atributos
    public static SharedPreferences preferenciasPrivadas, preferenciasPublicas;
    //:::::::::::::::::::::::::::::::  Metodos

    /**
     * leerPreferencias
     * <p>
     * Patron para leer preferencias, tanto públicas como privadas
     *
     * @param context
     */
    public static void leerPreferencias(Context context) {
        // Las privadas
        preferenciasPublicas = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        preferenciasPrivadas = context.getSharedPreferences("MisPreferenciasPrivadas", MODE_PRIVATE);
        // int numero = preferenciasPrivadas.getInt("base", 10);
        // String nombre = preferenciasPrivadas.getString("nombre", "nadapordefecto");
    }

    /**
     * guardarPreferencias
     * <p>
     * Patron para guardar preferencias, tanto públicas como privadas
     */
    public static void guardarPreferencias() {
        SharedPreferences.Editor editor = preferenciasPrivadas.edit();
        // editor.putInt("numero", 2233);
        // editor.putString("nombre", "Juan");
        editor.commit();
    }

    /**
     * ***************************************************************************
     * ************            DATE Y STRING                        **************
     * ***************************************************************************
     */
    /**
     * hoy_en_DATE
     *
     * @return la fecha actual en formato DATE
     */
    public static Date hoy_en_DATE() {
        Calendar cc = Calendar.getInstance();
        Date hoyEnDate = cc.getTime();
        return hoyEnDate;
    }

    /**
     * de_DATE_a_STRING
     * <p>
     * convertir de un Date de java.util a STRING, con un formato
     *
     * @param fechaEnDate Objeto Date de la fecha a cambiar
     * @param formato     formato, como p.e. "dd/MM/yyyy"
     *
     * @return fecha en string en dicho formato, por ejemplo, "12/22/2016"
     */
    public static String de_DATE_a_STRING(Date fechaEnDate, String formato) {
        SimpleDateFormat miFormato = new SimpleDateFormat(formato);
        String fechaEnString = miFormato.format(fechaEnDate);
        return fechaEnString;
    }

    /**
     * de_STRING_a_DATE
     * <p>
     * Convierte un String en un util.Date
     *
     * @param fechaEnString
     *
     * @return
     */
    public static Date de_STRING_a_DATE(String fechaEnString) {
        SimpleDateFormat miFormato2 = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaenjava = null;
        try {
            fechaenjava = miFormato2.parse(fechaEnString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fechaenjava;
    }

    /**
     * ***************************************************************************
     * ************            ALMACENAMIENTO EN DISPOSITIVO        **************
     * ***************************************************************************
     */

    /**
     * salvarBitMapEnDirectorioInternoApp
     * <p>
     * Guarda un Bitmap en un fichero, en el directorio de imagenes de la aplicacion (Internal Storage)
     * Devuelve un File que referencia directamente al fichero creado
     *
     * @param context
     * @param bitmap
     * @param nombredelficheroDestino
     *
     * @return Devuelve un File que referencia directamente al fichero creado
     */
    public static File salvarBitMapEnUnidadInternaApp(Context context, Bitmap bitmap, String nombredelficheroDestino) {
        ContextWrapper wrapper = new ContextWrapper(context);
        File carpeta = wrapper.getDir("Images", MODE_PRIVATE);
        File fileDestino = new File(carpeta, nombredelficheroDestino);
        try {
            OutputStream stream = new FileOutputStream(fileDestino);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileDestino;
    }

    /**
     * salvarBitMapEnDirectorioLocalApp
     * <p>
     * Guarda un Bitmap en un fichero, en el directorio de imagenes de la aplicacion (Internal Storage)
     * Devuelve un File que referencia directamente al fichero creado
     *
     * @param context
     * @param contenido
     * @param nombredelficheroDestino
     *
     * @return Devuelve un File que referencia directamente al fichero creado
     */
    public static File salvarStringEnUnidadInterna(Context context, String contenido, String nombredelficheroDestino) {
        ContextWrapper wrapper = new ContextWrapper(context);
        // AL indicar "Images" almacena en /data/data/(nuestraappid)/app_Images ... aunque se puede
        // poner cualquier cosa, "Almacen" por ejemplo, y lo crea en /data/data/(nuestraappid)/app_Almacen
        File carpeta = wrapper.getDir("Images", MODE_PRIVATE);
        File fileDestino = new File(carpeta, nombredelficheroDestino);
        try {
            if (!fileDestino.exists())
                fileDestino.createNewFile();  // por si acaso android en el futuro ..... hace algo raro
            PrintWriter pw = new PrintWriter(fileDestino);
            pw.println(contenido);
            pw.close();
            return fileDestino;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileDestino;
    }

    /**
     * salvarBitmapEnEnDirectorioExterno
     * <p>
     * Si el directorio elegido no existe, intenta crearlo y devolverlo
     * guardarStringEnFicheroExterno
     *
     * @param DIRECTORIOEXTERNO    Ubicacion RAIZ del almacenamieno externo
     *                             El valor de DIRECTORIOEXTERNO que se puden usar con este metodo son (entre otros):
     *                             Environment.DIRECTORY_PICTURES
     *                             Environment.DIRECTORY_DOCUMENTS
     *                             Environment.DIRECTORY_DCIM
     *                             Environment.DIRECTORY_MOVIES
     *                             Environment.DIRECTORY_MUSIC
     *                             Environment.DIRECTORY_PODCASTS
     * @param carpetadestino
     * @param nombreficherodestino
     *
     * @return Devuelve un File que referencia directamente al fichero creado, o null si hay error
     */
    public static File salvarBitmapEnUnidadExterna(String DIRECTORIOEXTERNO, Bitmap bitmap, String carpetadestino, String nombreficherodestino) {
        boolean res = true;
        File fileDestino = null;
        try {
            File carpetaDestino = JYOCUtilsv4.obtenerDirectorioEnUnidadExterna(DIRECTORIOEXTERNO, carpetadestino);


            if (carpetaDestino != null) {
                fileDestino = new File(carpetaDestino, nombreficherodestino);
                if (!fileDestino.exists())
                    fileDestino.createNewFile();  // por si acaso android en el futuro ..... hace algo raro

                OutputStream stream = new FileOutputStream(fileDestino);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                stream.flush();
                stream.close();
                return fileDestino;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * salvarStringEnEnDirectorioExterno
     * <p>
     * Si el directorio elegido no existe, intenta crearlo y devolverlo
     * guardarStringEnFicheroExterno
     *
     * @param DIRECTORIOEXTERNO    Ubicacion RAIZ del almacenamieno externo
     *                             El valor de DIRECTORIOEXTERNO que se puden usar con este metodo son (entre otros):
     *                             Environment.DIRECTORY_PICTURES
     *                             Environment.DIRECTORY_DOCUMENTS
     *                             Environment.DIRECTORY_DCIM
     *                             Environment.DIRECTORY_MOVIES
     *                             Environment.DIRECTORY_MUSIC
     *                             Environment.DIRECTORY_PODCASTS
     * @param carpetadestino
     * @param nombreficherodestino
     *
     * @return Devuelve un File que referencia directamente al fichero creado
     */
    public File salvarStringEnUnidadExterna(String DIRECTORIOEXTERNO, String contenido, String carpetadestino, String nombreficherodestino) {
        boolean res = true;
        File fileDestino = null;
        try {
            File carpetaDestino = JYOCUtilsv4.obtenerDirectorioEnUnidadExterna(DIRECTORIOEXTERNO, carpetadestino);
            if (carpetaDestino != null) {
                fileDestino = new File(carpetaDestino, nombreficherodestino);
                if (!fileDestino.exists())
                    fileDestino.createNewFile();  // por si acaso android en el futuro ..... hace algo raro
                PrintWriter pw = new PrintWriter(fileDestino);
                pw.println(contenido);
                pw.close();
                return fileDestino;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileDestino;
    }

    /**
     * obtenerFileDeFicheroEnDirectorioExterno
     * <p>
     * Obtiene un objeto file de un fichero en el almacenamiento externo, dado un directorio donde buscar y nombre del fichero
     *
     * @param DIRECTORIOEXTERNO Ubicacion RAIZ del almacenamieno externo
     *                          El valor de DIRECTORIOEXTERNO que se puden usar con este metodo son (entre otros):
     *                          Environment.DIRECTORY_PICTURES
     *                          Environment.DIRECTORY_DOCUMENTS
     *                          Environment.DIRECTORY_DCIM
     *                          Environment.DIRECTORY_MOVIES
     *                          Environment.DIRECTORY_MUSIC
     *                          Environment.DIRECTORY_PODCASTS     *
     * @param carpetaorigen
     * @param nombrefichero
     *
     * @return
     */
    public static File obtenerFileDeFicheroEnUnidadExterna(String DIRECTORIOEXTERNO, String carpetaorigen, String nombrefichero) {
        boolean res = true;
        File file = null;
        try {
            // se crea variable separada por conveniencia, para facilitar una depuracion
            String pathcompleto = Environment.getExternalStoragePublicDirectory(DIRECTORIOEXTERNO) + File.separator + carpetaorigen;
            file = new File(pathcompleto, nombrefichero);
            if (file.exists()) {
                return file;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * obtenerUriDesdeUnFile
     * <p>
     * Obtiene un objeto URI desde un objeto FILE
     *
     * @param file
     *
     * @return
     */
    public static Uri obtenerUriDesdeUnFile(File file) {
        Uri uriDelDFichero = Uri.fromFile(new File(file.getAbsolutePath()));
        return uriDelDFichero;
    }

    /**
     * obtenerUnBitmapDesdeUnaUriDelDirInternoDeImagenes
     * <p>
     * Obtiene un Bitmap desde una URI que apunta a una imagen del directorio de imagenes de la aplicacion (Internal Storage)
     *
     * @param ctx
     * @param uri
     *
     * @return
     */
    public static Bitmap obtenerBitmapDesdeUriDeUnidadInternaDeImagenes(Context ctx, Uri uri) {
        Bitmap bm = null;
        try {
            bm = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }

    /**
     * ponerImagenEnImageViewDesdeFicheroDeImagenesDeAplicacion
     * <p>
     * Pone en un ImageView el contenido de una imagen que esta en el directorio de imagenes de la aplicacion (Internal Storage)
     *
     * @param context
     * @param view
     * @param nombreficheroimagen
     *
     * @return
     */
    public static boolean ponerImagenEnImageViewDesdeFicheroDeUnidadInternaDeImagenes(Context context, ImageView view, String nombreficheroimagen) {
        File file = new File(new ContextWrapper(context).getDir("Images", MODE_PRIVATE), nombreficheroimagen);
        if (file.exists()) {
            Uri uri = JYOCUtilsv4.obtenerUriDesdeUnFile(file);
            Bitmap bitmapdesdedisco = JYOCUtilsv4.obtenerBitmapDesdeUriDeUnidadInternaDeImagenes(context, uri);
            view.setImageBitmap(bitmapdesdedisco);
            // -- Si se desea que quede imagen redonda usar esta linea en vez de la anterior:
            //ponerImagenConFormatoRedondoEnImageView(context, bitmapdesdedisco, view);
            return true;
        }
        return false;
    }


    /**
     * ponerImagenConFormatoRedondoEnImageView
     * <p>
     * Pone en un ImageView una imagen, pero el ImageVIew queda con formato circular
     *
     * @param context
     * @param bitmap
     * @param imageview
     */
    public static void ponerImagenConFormatoRedondoEnImageView(Context context, Bitmap bitmap, ImageView imageview) {
        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        circularBitmapDrawable.setCircular(true);
        imageview.setImageDrawable(circularBitmapDrawable);
        imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    /**
     * obtenerDirectorioEnCarpetaExterna
     * <p>
     * Si el directorio elegido no existe, intenta crearlo y devolverlo
     * Si no se puede crear devuelve null, si no, devuelve el directorio existente o creado
     *
     * @param DIRECTORIOEXTERNO Ubicacion RAIZ del almacenamieno externo
     *                          El valor de DIRECTORIOEXTERNO que se puden usar con este metodo son (entre otros):
     *                          Environment.DIRECTORY_PICTURES
     *                          Environment.DIRECTORY_DOCUMENTS
     *                          Environment.DIRECTORY_DCIM
     *                          Environment.DIRECTORY_MOVIES
     *                          Environment.DIRECTORY_MUSIC
     *                          Environment.DIRECTORY_PODCASTS
     * @param nuevaCarpeta
     *
     * @return Si no se puede crear devuelve null, si no, devuelve el directorio existente o creado
     */
    public static File obtenerDirectorioEnUnidadExterna(String DIRECTORIOEXTERNO, String nuevaCarpeta) {
        File file = new File(Environment.getExternalStoragePublicDirectory(DIRECTORIOEXTERNO), nuevaCarpeta);
        if (file.exists()) {
            return file;
        }
        if (!file.mkdirs()) {
            file = null;
        }
        return file;
    }


    /**
     * leerJSONDesdeUrl
     * <p>
     * Lee un JSON desde una direcion de Internet
     * Devuelve en un String el JSON leido
     *
     * @param urlALeer
     *
     * @return
     */
    public static String leerJSONDesdeUrl(String urlALeer) {
        URL url = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        BufferedReader br_delaurl = null;
        String todo = "";
        try {
            url = new URL(urlALeer);
            br_delaurl = new BufferedReader(new InputStreamReader(url.openStream()));
            String linea;
            while ((linea = br_delaurl.readLine()) != null) {
                todo += linea;
            }
        } catch (Exception e) {
            e.printStackTrace();
            todo = null;
        } finally {
            if (br_delaurl != null) {
                try {
                    br_delaurl.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    todo = null;
                }
            }
            return todo;
        }
    }


    /**
     * ***************************************************************************
     * ************            ENVIANDO A OTRAS APPS                **************
     * ***************************************************************************
     */


    /**
     * enviarSMS
     * <p>
     * Envia un SMS aun numero determinado
     *
     * @param actividad
     * @param texto
     * @param numerodestino
     */
    public static void enviarSMS(Activity actividad, String texto, String numerodestino) {
        Uri uri = Uri.parse("smsto:" + numerodestino);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.putExtra("sms_body", texto);
        //i.setPackage("com.whatsapp");
        actividad.startActivity(i);
    }

    /**
     * enviarWhatsapp
     * <p>
     * Abre whatsapp con un texto preparado y espera a que el usuario elija destinatario
     * <p>
     * Si se desea una accion personalizada si no existe Whatsapp instalado en el dispositivo,
     * añadir las lineas comentadas y cambiar la accion en el catch
     *
     * @param actividad
     * @param mensaje
     */
    public static void enviarWhatsapp(Activity actividad, String mensaje) {
        //--- Si se desea una accion personalizada si no existe Whatsapp instalado en el dispositivo,
        //--- añadir las lineas comentadas y cambiar la accion en el catch
        // PackageManager pm = actividad.getPackageManager();
        //  try {
        //PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage("com.whatsapp"); // Si no se especifica la app aqui, Android muetra el "application picker" o selector de aplicaciones.
        intent.putExtra(Intent.EXTRA_TEXT, mensaje);
        actividad.startActivity(Intent.createChooser(intent, "Compartiendo con whatsapp..."));
        //} catch (PackageManager.NameNotFoundException e) {
        //    Toast.makeText(actividad, "WhatsApp no esta instalado", Toast.LENGTH_SHORT).show();
        //}
    }

    /**
     * enviarMail
     * <p>
     * Version que no pregunta al usuario por la app que desea usar sino que usa la app por defecto del dispositivo
     *
     * @param actividad
     * @param asunto
     * @param mensaje
     * @param destinatariosSeparadosPorComas
     */
    public static void enviarMail(Activity actividad, String asunto, String mensaje, String destinatariosSeparadosPorComas) {
        Intent i = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + destinatariosSeparadosPorComas));
        // -- EXTRA_SUBJECT Es el asunto del correo y EXTRA_TEXT el cuerpo del correo
        i.putExtra(Intent.EXTRA_SUBJECT, asunto);
        i.putExtra(Intent.EXTRA_TEXT, mensaje);
        // -- EXTRA_HTML_TEXT se usa si el texto del correo tiene formato HTML
        // i.putExtra(Intent.EXTRA_HTML_TEXT, "<bold>TEXTO DEL MENSAJE</bold>");
        // -- En este caso el segundo parametro del createChooser es un mensaje que se muestra
        // -- por pantalla cuando se efectua el envio
        actividad.startActivity(Intent.createChooser(i, "Enviando mensaje"));
    }

    /**
     * enviarMailConFichero
     * <p>
     * Version que adjunta un fichero al mail
     * USA EL CONCEPTO DE FILE PROVIDER; ASI QUE NECESITA AÑADIR ESTO:
     * ------------------------
     * - una etiqueta <provider> en el Manifest
     * <provider
     *        android:name=".GenericFileProvider"
     *        android:authorities="${applicationId}.fileprovider"
     *        android:exported="false"
     *        android:grantUriPermissions="true">
     *        <meta-data
     *            android:name="android.support.FILE_PROVIDER_PATHS"
     *            android:resource="@xml/provider_paths"/>
     * </provider>
     * ------------------------
     * - un xml que defina un recurso path llamada provider_paths (que es como se llamo en el provider anterior)
     * <?xml version="1.0" encoding="utf-8"?>
     * <paths xmlns:android="http://schemas.android.com/apk/res/android">
     *     <external-path
     *         name="external_files"
     *         path="." />
     * </paths>
     * ------------------------
     * <p>
     * En un Intent.setType(String mimeType) el String representa el tipo de dato MIME
     * que se desea obtener como respuesta al lanzar un intent
     * Esta lista muesyras los tipos de ficheros mas habituales y su tipo MIME asociado
     * <p>
     * cualquier imagen -> image/*
     * cualquier texto -> text/*
     * cualquier audio -> audio/*
     * cualquier video -> video/*
     * .xml ->text/xml
     * .jpeg -> image/jpeg
     * .mpg -> audio/mpeg4-generic
     * .mpeg -> audio/mpeg
     * .aac -> audio/aac
     * .wav -> audio/wav
     * .ogg -> audio/ogg
     * .midi -> audio/midi
     * .wma -> audio/x-ms-wma
     * .mp4 -> video/mp4
     * .msv -> video/x-msvideo
     * -wmv -> video/x-ms-wmv
     * .png -> image/png
     * .jpeg .jpg -> image/jpeg
     * .gif -> image/gif
     * .txt -> text/plain
     * .cfg -> text/plain
     * .csv -> text/plain
     * .conf -> text/plain
     * .rc -> text/plain
     * .htm -> text/html
     * .html -> text/html
     * .pdf -> application/pdf
     * .apk -> application/vnd.android.package-archive
     *
     * @param actividad
     * @param asunto
     * @param mensaje
     * @param arrayDestinatarios
     */
    public static void enviarMailConFichero(Activity actividad, String asunto, String mensaje, String[] arrayDestinatarios) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"javayotrascosas@gmail.com"});
        // EJEMPLOS-
        //intent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
        //intent.putExtra(Intent.EXTRA_TEXT, "body text");
        //intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"javayotrascosas@gmail.com"});

        intent.putExtra(Intent.EXTRA_SUBJECT, asunto);
        intent.putExtra(Intent.EXTRA_TEXT,  mensaje);
        intent.putExtra(Intent.EXTRA_EMAIL, arrayDestinatarios);

        File file = obtenerFileDeFicheroEnUnidadExterna(Environment.DIRECTORY_DOCUMENTS, "miscosas", "prueba.txt");
        // -- Otro ejemplo, se lee directamente del directorio PICTURES y se envia una foto
        // File file = obtenerFileDeFicheroEnDirectorioExterno(Environment.DIRECTORY_PICTURES, "", "pperilla_logo_rojo.png");

        Context con = actividad.getApplicationContext();
        String str = actividad.getApplicationContext().getPackageName() + ".fileprovider";
        Uri uri = FileProvider.getUriForFile(con, str, file);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        actividad.startActivityForResult(Intent.createChooser(intent, "Enviando con adjunto..."), 3);
    }


    /**
     * tomarScreenShot
     *
     * Toma un ScreenShot de la pantalla actual y lo devuelve como un Bitmap
     * Para guadarlo en un fichero de disco, usar salvarBitmapEnEnDirectorioExternoApp() Ejemplo:
     *   Bitmap bitmap = JYOCUtilsv4.tomarScreenShot(mainactivity);
     *   File file = JYOCUtilsv4.salvarBitmapEnEnDirectorioExternoApp(Environment.DIRECTORY_PICTURES,bitmap,"ScreenShots","pantallazo.png");
     *
     * @param actividad
     */
    public static Bitmap tomarScreenShot(Activity actividad) {
        View rootView = actividad.getWindow().getDecorView().findViewById(android.R.id.content);
        View screenView = rootView.getRootView();
        Bitmap bitmap = Bitmap.createBitmap(screenView.getWidth(), screenView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = screenView.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        screenView.draw(canvas);
        return bitmap;
    }

    /**
     * Compartir
     * 
     * Abre una ventana para que el usuario comparta el fichero con las app del dispositivo
     * Recibe un objeto File del fichero que va a compartir
     * 
     * @param file
     * @param actividad
     */
    public static void Compartir(File file, Activity actividad) {
        Context con = actividad.getApplicationContext();
        String str = actividad.getApplicationContext().getPackageName() + ".fileprovider";
        Uri uri = FileProvider.getUriForFile(con, str, file);
        //Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("*/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            actividad.startActivity(Intent.createChooser(intent, "Compartir...."));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(actividad.getApplicationContext(), "No hay aplicacion instalada para esta accion", Toast.LENGTH_SHORT).show();
        }
    }
}