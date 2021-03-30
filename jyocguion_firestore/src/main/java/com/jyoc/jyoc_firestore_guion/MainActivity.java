package com.jyoc.jyoc_firestore_guion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements IAfterJyocFS_DAO {

    Button btAnadir, btConsultar;
    TextView tvResultado;
    JyocFS_DAO<Cosa> dao;
    String res = "";
    ArrayList<Cosa> listas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResultado = findViewById(R.id.tvResultado);
        btAnadir = findViewById(R.id.btAnadir);
        btConsultar = findViewById(R.id.btConsultar);

        dao = new JyocFS_DAO<Cosa>(this, "PruebaDeCosas", Cosa.class);
        Cosa c1 = new Cosa("1", "Avion", 6);
        Cosa c2 = new Cosa("2", "Tanque", 12);
        Cosa c3 = new Cosa("3", "Submarino", 2);
        Cosa c4 = new Cosa("4", "Fragata", 9);
        listas.add(c1);
        listas.add(c2);
        listas.add(c3);
        listas.add(c4);
    }

    public void onConsultarReg1(View v) {
        dao.readElementByEquality("id", listas.get(1).getId());
    }
    
    public void onConsultar(View v) {
        dao.readAllElements(100);
    }

    public void onBorrar(View v) {
        dao.deleteElement(listas.get(1).getId());
    }

    public void onBorrarTodo(View v) {
        dao.deleteAllElements();
    }

    public void onAnadir(View v) {

        // Asi se añaden elementos y se deja que la ClaveFirestore de cada uno la cree Firestore
        // Observar que el segundo parametro del método es NULL
        //for (Cosa c : listas) {
        //    dao.addElement(c, null);
        //}

        // Asi se añaden varios elementos y para la ClaveFirestore de cada uno se usa su campo id
        // Si queremos mantener para FireStore la misma clave existente en el propio objeto, 
        // el setter para obtenerla se ha de llamar setId(), y no es necesaria la linea siguiente
        // Dejar la linea siguiente si se desea que el id del objeto almacenado  
        // que genera en este momento, como una clave alfanumerica aleatoria 
        //         c.setId(JyocFS_DAO.generateRandomKey(20));
        //          dao.addElement(c, c.getId());
        
        for (Cosa c : listas) {
            c.setId(JyocFS_DAO.generateRandomKey(20));
            dao.addElement(c, c.getId());
        }
    }


    @Override
    public void afterReadAllElements(List c) {
        //Log.d("JyocFS_DAO CONSUL 1***>", c + "");
    }

    @Override
    public void afterReadELementByEquality(Object o) {
        //Cosa c = (Cosa) o;
        //Log.d("JyocFS_DAO CONSUL 2***>", c + "");
    }

    @Override
    public void afterAddElement(boolean result) {
        //Log.d("JyocFS_DAO ADD ***>", result + "");
    }

    @Override
    public void afterDeleteElements(int numElementsDeleted) {

    }
}
