package com.jyoc.jyocpruebacogerfotogaleria;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LeerContactosAsyncTask extends AsyncTask<String, Void, Void> {
    Context context;
    TextView txtProgress;
    int totalContactos = 0, totalContactosLeidos = 0;
    List<Contacto> listaContactos;

    LeerContactosAsyncTask(Context context, List<Contacto> listaContactos, TextView txtProgress) {
        this.context = context;
        this.txtProgress = txtProgress;
        this.listaContactos = listaContactos;
    }

    @Override
    protected Void doInBackground(String[] args) {
        String filtroDeSeleccionDeContactos = args[0];
        String idDeUnContacto = "", nombretodojunto = "", nombrepropio = "", segundonombrepropio = "", apellidos = "", direccion = "";
        String cumpleanos ="", calle ="", ciudad="", pais="",emailprincipal="";
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };

        // CURSOR CON TODOS LOS CONTACTOS ---------------------------------------
        Cursor cursorContactos;
        if (filtroDeSeleccionDeContactos.length() > 0) {
            cursorContactos = contentResolver.query(uri, projection, ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?",
                    new String[]{filtroDeSeleccionDeContactos}, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        } else {
            cursorContactos = contentResolver.query(uri, projection, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");
        }
        if (cursorContactos != null && cursorContactos.getCount() > 0) {
            totalContactos = cursorContactos.getCount();
            while (cursorContactos.moveToNext()) {
                if (Integer.parseInt(cursorContactos.getString(cursorContactos.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                    idDeUnContacto = cursorContactos.getString(cursorContactos.getColumnIndex(ContactsContract.Contacts._ID));

                    int tieneTelefonosSiEsteValorEsMayorQue0 = cursorContactos.getInt(cursorContactos.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    // CURSOR CON LOS DATOS PERSONALES DE UN CONTACTO ------------------------------------
                    Cursor cursorDeUnContacto = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                            null,
                            ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?",
                            new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, Long.valueOf(idDeUnContacto).toString()},
                            null);
                    if (cursorDeUnContacto != null) {
                        if (cursorDeUnContacto.moveToFirst()) {
                            nombretodojunto = cursorDeUnContacto.getString(cursorDeUnContacto.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            nombrepropio = cursorDeUnContacto.getString(cursorDeUnContacto.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME));
                            segundonombrepropio = cursorDeUnContacto.getString(cursorDeUnContacto.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME));
                            apellidos = cursorDeUnContacto.getString(cursorDeUnContacto.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME));  
                            // NO VA       String emailprincipal = cursorDeUnContacto.getString(cursorDeUnContacto.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                            // CURSOR CON LOS TELEFONOS DE UN CONTACTO ------------------------------------
                            Cursor cursorTelefonos = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{idDeUnContacto}, null);
                            List<Contacto.Telefono> listaTelefonos = new ArrayList<>();
                            if (cursorTelefonos != null && cursorTelefonos.getCount() > 0) {
                                while (cursorTelefonos.moveToNext()) {
                                    String iddeltelefono = cursorTelefonos.getString(cursorTelefonos.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
                                    //String cadaLabel = cursorTelefonos.getString(cursorTelefonos.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
                                    String tipotelefono = (String) ContactsContract.CommonDataKinds.Phone.getTypeLabel(context.getResources(),
                                            cursorTelefonos.getInt(cursorTelefonos.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)), null);
                                    String cadaNumero = cursorTelefonos.getString(cursorTelefonos.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    Contacto.Telefono tel = new Contacto.Telefono(tipotelefono, cadaNumero);
                                    listaTelefonos.add(tel);
                                }
                                cursorTelefonos.close();
                            }


                            // coger el cumpleaños
                            ContentResolver bd = context.getContentResolver();
                            Cursor bdc = bd.query(android.provider.ContactsContract.Data.CONTENT_URI,
                                    new String[]{ContactsContract.CommonDataKinds.Event.DATA},
                                    android.provider.ContactsContract.Data.CONTACT_ID + " = " +
                                            idDeUnContacto + " AND " + ContactsContract.Data.MIMETYPE + " = '" +
                                            ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "' AND " +
                                            ContactsContract.CommonDataKinds.Event.TYPE + " = " +
                                            ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY,
                                    null, android.provider.ContactsContract.Data.DISPLAY_NAME);
                            if (bdc.getCount() > 0) {
                                while (bdc.moveToNext()) {
                                     cumpleanos = bdc.getString(0);
                                    // now "id" is the user's unique ID, "name" is his full name and "birthday" is the date and time of his birth
                                }
                            }


                            // coger la direccion
                            Uri postal_uri = ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI;
                            Cursor postal_cursor = context.getContentResolver().query(postal_uri, null, ContactsContract.Data.CONTACT_ID + "=" + idDeUnContacto.toString(), null, null);
                            while (postal_cursor.moveToNext()) {
                                calle = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                                ciudad = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                                pais = postal_cursor.getString(postal_cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                            }
                            postal_cursor.close();

                            // coger el email
                            ContentResolver cr = context.getContentResolver();
                            Cursor cur1 = cr.query(
                                    ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                                    new String[]{idDeUnContacto}, null);
                            while (cur1.moveToNext()) {
                                //to get the contact names
                                String nameQueAquiNoUso = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                emailprincipal = cur1.getString(cur1.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                                Log.e("Email", emailprincipal);
                            }
                            cur1.close();

                            // LA FOTO DE UN CONTACTO ------------------------------------
                            Bitmap photo = null;
                            try {
                                InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                                        ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.valueOf(idDeUnContacto)));

                                if (inputStream != null) {
                                    photo = BitmapFactory.decodeStream(inputStream);
                                }
                                if (inputStream != null)
                                    inputStream.close();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (apellidos == null)
                                apellidos = "";
                            if (nombrepropio == null)
                                apellidos = "";
                            if (segundonombrepropio == null)
                                apellidos = "";
                            if (calle == null)
                                calle = "";
                            if (ciudad == null)
                                ciudad = "";
                            if (pais == null)
                                pais = "";
                            if (cumpleanos == null)
                                cumpleanos = "";

                            listaContactos.add(new Contacto(idDeUnContacto, nombrepropio, segundonombrepropio, apellidos, calle,ciudad,pais,cumpleanos, emailprincipal,listaTelefonos, photo));
                        }
                        cursorDeUnContacto.close();
                    }
                }

                totalContactosLeidos++;
                publishProgress();
            }
            cursorContactos.close();
        }else{
            totalContactos = 0;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void[] v) {
        if (txtProgress != null) {
            txtProgress.setVisibility(View.VISIBLE);
            txtProgress.setText("Leyendo...(" + totalContactosLeidos + "/" + totalContactos + ")");
        }
    }

    @Override
    protected void onPostExecute(Void v) {
        if (txtProgress != null) {
            txtProgress.setText("");
            txtProgress.setVisibility(View.GONE);
        }
        ((MainActivity) context).mostrarContactosTrasHiloLectura();
    }
}
