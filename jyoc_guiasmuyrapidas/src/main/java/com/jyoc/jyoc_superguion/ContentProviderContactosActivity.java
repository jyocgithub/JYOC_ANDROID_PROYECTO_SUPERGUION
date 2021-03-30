package com.jyoc.jyoc_superguion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class ContentProviderContactosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_provider_contactos);
        
        
        
        
    }

    public List<MiContacto> contentProviderContactos(){
        // *****************  CONTENT PROVIDER CONTACTOS
        List<MiContacto> listaContactos = new ArrayList<>();

        // -- definir campos que extraemos
        String campos[] = {ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER};

        // -- definir filtro con variables paremetrizadas
        String filtro = ContactsContract.Contacts.DISPLAY_NAME + " like ?";

        // -- definir parametros del filtro que extraemos
        String textoABUscar = "a";
        String[] params_filtro = {"%" + textoABUscar + "%"};

        // extraer el cursor con una query en ek content resolver
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                campos,
                filtro,
                params_filtro,
                null);

        while (cur.moveToNext()) {
            // obtener id de contacto 
            String idContacto = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
            // obtener nombre de contacto
            String nombreContacto = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            // si tiene teléfono, lo buscamos. Necesita una segunda consulta
            boolean hayNumero = Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0;
            if (hayNumero) {
                ContentResolver cr2 = getContentResolver();

                // -- definir campos que extraemos
                String campos2[] = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                // -- definir filtro con variables paremetrizadas
                String filtro2 = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";

                // -- definir parametros del filtro que extraemos
                String[] params_filtro2 = {idContacto};

                Cursor cur2 = cr2.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        campos2,
                        filtro2,
                        params_filtro2,
                        null);

                ArrayList<String> lissnums = new ArrayList<>();
                while (cur2.moveToNext()) {
                    String unTelefono = cur2.getString(cur2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    lissnums.add(unTelefono);
                }
                cur2.close();

                MiContacto m = new MiContacto(idContacto, nombreContacto, lissnums);
                listaContactos.add(m);
            }

        }
        cur.close();
        return listaContactos;
    }

}


class MiContacto{
    String id;
    String nombre;
    ArrayList<String> listanumeros ;

    public MiContacto(String id, String nombre, ArrayList<String> listanumeros) {
        this.id = id;
        this.nombre = nombre;
        this.listanumeros = listanumeros;
    }
}