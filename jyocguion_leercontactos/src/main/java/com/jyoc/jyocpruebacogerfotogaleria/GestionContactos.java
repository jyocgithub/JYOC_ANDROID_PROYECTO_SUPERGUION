package com.jyoc.jyocpruebacogerfotogaleria;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

public class GestionContactos {

    // ---------------- CONSTANTES DE ACCION DE CADA METODO ---------------------
    public static final int CONSTANTE_ELEGIR_TELEFONO_DEUN_CONTACTO = 11346;


    // =====================================================
    // ====     elegirTelefonoDeUnContacto            ======
    // =====================================================
    public static void elegirTelefonoDeUnContacto(Activity actividad) {
        Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        pickContact.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        actividad.startActivityForResult(pickContact, CONSTANTE_ELEGIR_TELEFONO_DEUN_CONTACTO);
    }
    
    
    public static Cursor getCursorContactoPorSuId(Activity actividad, String idDeUnContacto){
        Cursor cursorDeUnContacto = actividad.getContentResolver().query(ContactsContract.Data.CONTENT_URI,
                null,
                ContactsContract.Data.MIMETYPE + " = ? AND " + ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + " = ?",
                new String[]{ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE, Long.valueOf(idDeUnContacto).toString()},
                null);
        return cursorDeUnContacto;
    }

    public static void agregarContactoEnAPPContactos(Activity actividad, String pNombre, String pNumeroTelefono){
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        int rawContactInsertIndex = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null).build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, pNombre) // nombre del contacto
                .build());
        ops.add(ContentProviderOperation
                .newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                .withValue(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, pNumeroTelefono) // numero de telefono
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE).build()); // Tipo de numero de telefono
        try {
            ContentProviderResult[] res = actividad.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e("****JYOC", "Error añadiendo contacto");
        }
    }
    
    
    public void editarContactoEnAppContactos(Activity actividad, String id){

        // The Cursor that contains the Contact row
        Cursor mCursor =   GestionContactos.getCursorContactoPorSuId(actividad, "1" );
        mCursor.moveToFirst();
        // The index of the lookup key column in the cursor
        int lookupKeyIndex;
        // The index of the contact's _ID value
        int idIndex;
        // The lookup key from the Cursor
        String mCurrentLookupKey;
        // The _ID value from the Cursor
        long currentId;
        // A content URI pointing to the contact
        Uri selectedContactUri;

        /*
         * Once the user has selected a contact to edit,
         * this gets the contact's lookup key and _ID values from the
         * cursor and creates the necessary URI.
         */
        // Gets the lookup key column index
        lookupKeyIndex = mCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY);
        // Gets the lookup key value
        mCurrentLookupKey = mCursor.getString(lookupKeyIndex);
        // Gets the _ID column index
        idIndex = mCursor.getColumnIndex(ContactsContract.Contacts._ID);
        currentId = mCursor.getLong(idIndex);
        selectedContactUri =
                ContactsContract.Contacts.getLookupUri(currentId, mCurrentLookupKey);

        // Creates a new Intent to edit a contact
        Intent editIntent = new Intent(Intent.ACTION_EDIT);
        /*
         * Sets the contact URI to edit, and the data type that the
         * Intent must match
         */
        editIntent.setDataAndType(selectedContactUri, ContactsContract.Contacts.CONTENT_ITEM_TYPE);

                
                /*
                En Android 4.0 (API nivel 14) y versiones posteriores, un problema en la App de Contactos 
                lleva a una navegación incorrecta. Cuando tu app envía un intent a la App de Contactos, y los usuarios editan 
                y guardan un contacto, al hacer clic en Atrás, verán la pantalla de la lista de contactos. 
                Para volver a tu app, deberán hacer clic en Recientes y seleccionarla.

                Para solucionar este problema en Android 4.0.3 (API nivel 15) y versiones posteriores, 
                se agrega la clave de datos extendidos finishActivityOnSaveCompleted al intent, 
                con un valor de true. 
                Las versiones de Android anteriores a la 4.0 aceptan esta clave, pero no tiene ningún efecto. 
                Para configurar los datos extendidos se hace asi;
                 */
        editIntent.putExtra("finishActivityOnSaveCompleted", true);
                /*
                CUIDADO QUE AL VOLVER DE LA APP DE CONTACTOS NO SE ACTIVA EL onActivitResult, hay que controlar la vuelta 
                con el méotodo ONRESTART() 
                 */
                
                /*
                lanzamos el intent
                 */
        actividad.startActivity(editIntent);


    }
    

}
